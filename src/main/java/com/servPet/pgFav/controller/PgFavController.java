package com.servPet.pgFav.controller;

import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;
import com.servPet.pgFav.model.PgFavService;

@Controller
@RequestMapping("/pgFav")
public class PgFavController {

    @Autowired
    private PgFavService pgFavService;
    
    @Autowired
    private MebService mebService;
    
    // 查詢全部美容師收藏
    @GetMapping("/list")
    public String listFavorites(Model model, Principal principal) {
        boolean isLoggedIn = (principal != null);
        model.addAttribute("pgFavList", pgFavService.getAllFavorites());
        return "front_end/pgFav/listAllPgFav"; // 對應 /templates/front_end//pdFav/listAllPgFav.html
    }

//    @PostMapping("/add")
//    @ResponseBody
//    public String addFavorite(HttpSession session, @RequestParam Integer pgId,Principal principal) {
//    	
//    	/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
////        // 假設已取得 mebVO 和 pgVO，並傳遞進來
////        MebVO mebVO = new MebVO();
////        PgVO pgVO = new PgVO();
////        mebVO.setMebId(mebId);
////        pgVO.setPgId(pgId);
//    	
//    	// 從 session 中取得已登入的會員資訊
////    	MebVO mebVO = (MebVO) session.getAttribute("mebVO");
//    	Optional<MebVO> OptionalmebVO = mebService.findMemberByEmail(principal.getName());
//    	// 檢查 session 是否有會員資訊
//        if (OptionalmebVO == null) {
//    		System.out.println("未登入用戶觸發收藏，pgId: " + pgId);
//    		return "請先登入會員";
//        }
////    	System.out.println("已登入會員ID: " + mebVO.getMebId() + ", 美容師ID: " + pgId);
//        
//        /*************************** 2.開始新增資料 *****************************************/
////        String result = pgFavService.addFavorite(mebVO, pgVO);
//        MebVO mebVO =OptionalmebVO.get();
//     // pgId 已經從請求參數中獲得，直接傳入 Service
//        String result = pgFavService.addFavorite(mebVO.getMebId(), pgId);
//        
//        /*************************** 3.新增完成,準備返回(Send the Success view) **************/
//        System.out.println("收藏結果: " + result);
//        return result;  // 回傳新增成功或失敗的訊息
//    }
    
    @PostMapping("/add")
    @ResponseBody
    public String addFavorite(HttpSession session, @RequestParam Integer pgId, Principal principal) {
        Optional<MebVO> optionalMebVO = mebService.findMemberByEmail(principal.getName());
        if (!optionalMebVO.isPresent()) {
            return "請先登入會員";
        }
        MebVO mebVO = optionalMebVO.get();
        String result = pgFavService.addFavorite(mebVO.getMebId(), pgId);
        return result; // 傳回成功或失敗的訊息
    }

    
    
    @PostMapping("/deleteFavorite")
    public String deleteFavorite(@RequestParam Integer pgFavId, RedirectAttributes redirectAttributes) {
        if (pgFavId != null) {
            pgFavService.deleteFavoriteById(pgFavId);
            redirectAttributes.addFlashAttribute("successMessage", "已成功取消收藏！");
        }
        return "redirect:/pgFav/list";
    }

    
    
    @GetMapping("/isFavorite")
    @ResponseBody
    public boolean isFavorite(@RequestParam Integer mebId, @RequestParam Integer pgId) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        // 驗證傳入的參數是否有效
        if (mebId == null || pgId == null) {
            return false;
        }

        /*************************** 2.開始查詢資料 *****************************************/
        boolean isFavorite = pgFavService.checkIfFavorite(mebId, pgId).isPresent();

        /*************************** 3.查詢完成,返回結果(Send the Success view) **************/
        return isFavorite;
    }
}