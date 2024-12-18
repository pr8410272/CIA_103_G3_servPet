package com.servPet.sitterFav.model;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servPet.sitterFav.model.FavoRedisService;
import com.servPet.sitterFav.model.SitterFavVO;
import com.servPet.sitterFav.model.SitterFavRepository;

@Service
public class SitterFavService {

	@Autowired
	private SitterFavRepository sitterFavRepository;
	
	@Autowired
	private FavoRedisService favoRedisService;
	
	
	
	private static final String REDIS_REMOVED_KEY_PREFIX = "removed_favorites:";

	/**  新增收藏  **/
	 @Transactional
	    public boolean addFavorite(Integer mebId, Integer psId) {
	        // 檢查是否已存在收藏
	        if (sitterFavRepository.existsByMebIdAndPsId(mebId, psId)) {
	            return false; // 收藏已存在
	        }

	        // 新增收藏
	        SitterFavVO newFavorite = new SitterFavVO();
	        newFavorite.setMebId(mebId);
	        newFavorite.setPsId(psId);
	        sitterFavRepository.save(newFavorite);
	        return true; // 收藏加入成功
	    }

	 
	 
	 
	 /** 根據收藏ID刪除收藏並記錄到 Redis **/
	    @Transactional
	    public boolean removeFavoriteById(Integer sitterFavoId, Integer mebId) {
	        if (sitterFavRepository.existsBySitterFavoId(sitterFavoId)) {
	            // 查找收藏記錄
	            SitterFavVO favorite = sitterFavRepository.findById(sitterFavoId).orElse(null);
	            if (favorite == null) {
	                return false;
	            }

	            // 刪除 MySQL 記錄
	            sitterFavRepository.deleteBySitterFavoId(sitterFavoId);

	            // 添加到 Redis 的移除列表中
	            favoRedisService.addRemovedFavorite(mebId, favorite.getPsId());
	            return true; // 刪除成功
	        } else {
	            return false; // 收藏ID不存在
	        }
	    }

	    /** 恢復收藏 **/
	    @Transactional
	    public boolean restoreFavorite(Integer mebId, Integer psId) {
	        // 檢查 Redis 中是否有這個收藏
	        if (!favoRedisService.isRemovedFavorite(mebId, psId)) {
	            return false; // 該收藏不在已移除列表中
	        }

	        // 恢復到 MySQL
	        if (!sitterFavRepository.existsByMebIdAndPsId(mebId, psId)) {
	            SitterFavVO restoredFavorite = new SitterFavVO();
	            restoredFavorite.setMebId(mebId);
	            restoredFavorite.setPsId(psId);
	            sitterFavRepository.save(restoredFavorite);

	            // 從 Redis 中移除該記錄
	            favoRedisService.restoreFavorite(mebId, psId);
	            return true; // 恢復成功
	        }

	        return false; // 收藏已存在於 MySQL 中
	    }

	    /** 獲取 Redis 中的移除收藏列表 **/
	    public List<Integer> getRemovedFavorites(Integer mebId) {
	        // 將 Redis 中的 Set<Object> 轉換為 List<Integer>
	        return favoRedisService.getRemovedFavorites(mebId).stream()
	                .map(obj -> (Integer) obj)
	                .collect(Collectors.toList());
	    }
	
	    public List<Map<String, Object>> getFavoritesWithDetails(Integer mebId) {
	        // 調用倉庫層方法返回數據
	        return sitterFavRepository.findFavoritesWithDetailsByMebId(mebId);
	    }
	    
	    
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


	
}
