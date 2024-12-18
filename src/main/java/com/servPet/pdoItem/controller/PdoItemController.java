package com.servPet.pdoItem.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.servPet.admin.model.AdminVO;
import com.servPet.adminPer.model.AdminPerService;
import com.servPet.adminPer.model.AdminPerVO;
import com.servPet.cartDetails.model.CartDetailsService;
import com.servPet.pdDetails.model.PdDetailsService;
import com.servPet.pdDetails.model.PdDetailsVO;
import com.servPet.pdo.model.PdoRepository;
import com.servPet.pdo.model.PdoService;
import com.servPet.pdo.model.PdoVO;
import com.servPet.pdoItem.model.PdoItemRepository;
import com.servPet.pdoItem.model.PdoItemService;
import com.servPet.pdoItem.model.PdoItemVO;

@Controller
@RequestMapping("/pdoItem")
public class PdoItemController {

	@Autowired
	PdoItemRepository repository;

	@Autowired
	PdoItemService pdoItemSvc;

	@Autowired
	PdDetailsService pdDetailsSvc;
	
    @Autowired
    PdoService pdoSvc;

    @Autowired
    CartDetailsService cartDetailsSvc;
    
    @Autowired
    PdoRepository pdoRepository;
    
    @Autowired
	AdminPerService adminPerSvc;	
	
	
	// 公共方法來檢查 admin 是否存在於 session 中
	private boolean isAdminLoggedIn(HttpSession session) {
	    if (isSessionExpired(session)) {
	        return false;
	    }
	    return session.getAttribute("adminVO") != null;
	}
	
	private boolean isSessionExpired(HttpSession session) {
	    // 可以嘗試獲取一個Session屬性，如果拋出異常，則認為Session已失效
	    try {
	        session.getAttribute("anyAttribute"); // 獲取任意一個屬性，用於觸發Session失效異常
	        return false; // 如果沒有拋出異常，表示Session有效
	    } catch (IllegalStateException e) {
	        return true; // Session已失效
	    }
	}

	// ===<< 將admin存於 session 中一起送出 >>===/
	public String SaveSession(Model model, HttpSession session) {
		if (!isAdminLoggedIn(session)) {
			return "back_end/login"; 
		}
		AdminVO admin = (AdminVO) session.getAttribute("adminVO");
		model.addAttribute("adminVO", new AdminVO());
		model.addAttribute("admin", admin);
		List<AdminPerVO> adminPer = adminPerSvc.getPerByAdminId(admin.getAdminId());
        
        // 只取出該管理員有權限的功能 ID
        Set<Integer> allowedFunctionIds = adminPer.stream()
            .map(adminPerVO -> adminPerVO.getFncVO().getFncId())
            .collect(Collectors.toSet());
        model.addAttribute("allowedFunctionIds", allowedFunctionIds);
        
		return null;
	}

	// ===<< 將admin存於 session 中一起送出 >>===/
	public String SaveSession(ModelMap model, HttpSession session) {
		if (!isAdminLoggedIn(session)) {
			return "back_end/login"; 
		}
		AdminVO admin = (AdminVO) session.getAttribute("adminVO");
		model.addAttribute("adminVO", new AdminVO());
		model.addAttribute("admin", admin);
		List<AdminPerVO> adminPer = adminPerSvc.getPerByAdminId(admin.getAdminId());
        
        // 只取出該管理員有權限的功能 ID
        Set<Integer> allowedFunctionIds = adminPer.stream()
            .map(adminPerVO -> adminPerVO.getFncVO().getFncId())
            .collect(Collectors.toSet());
        model.addAttribute("allowedFunctionIds", allowedFunctionIds);
        
		return null;
	}

	// ===========<< 導向addAdmin >>============/
	@GetMapping("addAdmin")
	public String addAdmin(Model model, HttpSession session) {
		SaveSession(model, session);
		return "back_end/admin/addAdmin";
	}
	
	
	//=======================
    
    
    @GetMapping("/listPdoItems")
    public String listPdoItems(@RequestParam("pdoId") Integer pdoId, Model model, HttpSession session) {
    	SaveSession(model, session);
    	
    	// 根據 PDO_ID 查詢所有商品內容
        List<PdoItemVO> pdoItems = pdoItemSvc.getOrderItemsByPdoId(pdoId);

        // 將數據傳遞到前端
        model.addAttribute("pdoId", pdoId);
        model.addAttribute("pdoItemsList", pdoItems);

        return "back_end/listPdoItems"; // 新的 Thymeleaf 頁面名稱
    }
    
    @PostMapping("/addOrderItems")
    public String addOrderItems(@RequestParam("pdoId") Integer pdoId,
                                @RequestParam("pdIds") List<Integer> pdIds,
                                @RequestParam("quantities") List<Integer> quantities,
                                RedirectAttributes redirectAttributes, Model model, HttpSession session) {
    	SaveSession(model, session);
    	
    	// 1. 查詢 PdoVO 訂單物件
        PdoVO pdoVO = pdoRepository.findById(pdoId)
                .orElseThrow(() -> new RuntimeException("訂單不存在，ID: " + pdoId));

        // 2. 準備訂單細項列表
        List<PdoItemVO> pdoItems = new ArrayList<>();
        for (int i = 0; i < pdIds.size(); i++) {
            PdoItemVO item = new PdoItemVO();
            PdDetailsVO product = new PdDetailsVO();
            product.setPdId(pdIds.get(i));

            item.setPdDetailsVO(product); // 設置商品編號
            item.setPdQty(quantities.get(i)); // 設置商品數量
            item.setPdoVO(pdoVO); // 設置訂單關聯

            pdoItems.add(item);
        }

        // 3. 調用 Service 方法
        try {
            pdoItemSvc.createOrderItemsAndUpdateStock(pdoVO, pdoItems);
            redirectAttributes.addFlashAttribute("successMessage", "訂單細項新增成功，庫存已更新");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/pdoItem/listPdoItems?pdoId=" + pdoId;
    }



}