package com.servPet.sitterFav.controller;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.servPet.sitterFav.model.FavoRedisService;
import com.servPet.sitterFav.model.SitterFavRepository;
import com.servPet.sitterFav.model.SitterFavService;

@Controller
@RequestMapping("/favorites")
public class SitterFavController {

    @Autowired
    private SitterFavService sitterFavService;
    
    @Autowired
    private SitterFavRepository sitterFavRepository;
    
    
    @Autowired
    private FavoRedisService favoRedisService;

    
    
    /**
     * 顯示收藏頁面
     */
    @GetMapping("/psfav")
    public String showFavoritesPage() {
        return "front_end/psfav"; // 返回頁面路徑
    }

    /**
     * 加入收藏
     */
    @PostMapping("/add")
    public ResponseEntity<String> addFavorite(@RequestParam Integer mebId, @RequestParam Integer psId) {
        boolean isAdded = sitterFavService.addFavorite(mebId, psId);
        return isAdded
            ? ResponseEntity.ok("收藏已成功加入！")
            : ResponseEntity.status(400).body("收藏加入失敗，可能已存在！");
    }

    /**
     * 獲取收藏列表（包含詳細信息）
     */
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getFavoritesWithDetails(@RequestParam Integer mebId) {
        List<Map<String, Object>> favorites = sitterFavService.getFavoritesWithDetails(mebId);
        return ResponseEntity.ok(favorites);
    }

    /**
     * 移除收藏並記錄到 Redis
     */
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFavorite(@RequestParam Integer sitterFavoId, @RequestParam Integer mebId) {
        boolean isRemoved = sitterFavService.removeFavoriteById(sitterFavoId, mebId);
        return isRemoved
            ? ResponseEntity.ok("收藏已成功刪除並記錄到 Redis！")
            : ResponseEntity.status(404).body("收藏刪除失敗，未找到對應的 ID！");
    }

    /**
     * 獲取已移除收藏列表
     */
    public List<Map<String, Object>> getFavoritesDetailsByPsIds(List<Integer> psIds) {
        if (psIds.isEmpty()) {
            return List.of(); // 如果 psIds 為空，返回空列表
        }

        // 調用倉庫層查詢保母詳情
        return sitterFavRepository.findDetailsByPsIds(psIds).stream().peek(fav -> {
            // 將圖片轉換為 Base64
            byte[] psLicenses = (byte[]) fav.get("psLicenses");
            if (psLicenses != null) {
                fav.put("psLicenses", Base64.getEncoder().encodeToString(psLicenses));
            }
        }).collect(Collectors.toList());
    }

    
    @GetMapping("/removed")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getRemovedFavorites(@RequestParam Integer mebId) {
        // 從 Redis 中獲取移除的收藏 PS ID 列表
        List<Integer> removedPsIds = favoRedisService.getRemovedFavorites(mebId).stream()
        		.map(obj -> Integer.parseInt(obj.toString()))  // 將 Redis 中的 Set<Object> 轉換為 List<Integer>
                .collect(Collectors.toList());

        if (removedPsIds.isEmpty()) {
            return ResponseEntity.ok(List.of()); // 如果沒有移除的記錄，返回空列表
        }

        // 從數據庫查詢完整保母信息
        List<Map<String, Object>> removedFavorites = sitterFavService.getFavoritesDetailsByPsIds(removedPsIds);

        return ResponseEntity.ok(removedFavorites);
    }


    /**
     * 恢復收藏並從 Redis 中移除紀錄
     */
    @PostMapping("/restore")
    public ResponseEntity<String> restoreFavorite(@RequestBody Map<String, Integer> data) {
        Integer mebId = data.get("mebId");
        Integer psId = data.get("psId");

        boolean isRestored = sitterFavService.restoreFavorite(mebId, psId);
        return isRestored
            ? ResponseEntity.ok("收藏已成功恢復！")
            : ResponseEntity.status(400).body("收藏恢復失敗，可能已存在或不在移除列表中！");
    }

}
