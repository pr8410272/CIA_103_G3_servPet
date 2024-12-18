package com.servPet.pgSvcItem.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.servPet.pg.model.PgVO;
import com.servPet.pgSvcItem.model.PgSvcItemService;
import com.servPet.pgSvcItem.model.PgSvcItemVO;

@Controller
@RequestMapping("/pg")
public class PgSvcItemController {

	@Autowired
	private PgSvcItemService pgSvcItemSvc;
	// 公共方法來檢查 pg是否存在於 session 中
		private boolean isPgLoggedIn(HttpSession session) {
			return session.getAttribute("pgVO") != null;
		}

		// ===<< 將pg存於 session 中一起送出 >>===/
		public String SaveSession(Model model, HttpSession session) {
			PgVO pg = (PgVO) session.getAttribute("pgVO");
			model.addAttribute("pgVO", new PgVO());
			model.addAttribute("pg", pg);
			return null;
		}

		// ===<< 將pg存於 session 中一起送出 >>===/
		public String SaveSession(ModelMap model, HttpSession session) {
			PgVO pg = (PgVO) session.getAttribute("pgVO");
			model.addAttribute("pgVO", new PgVO());
			model.addAttribute("pg", pg);
			return null;
		}

	
	//刪除美容師服務項目
	@GetMapping("deletePgSvcItem")
	public String deletePgSvcItem(@RequestParam("svcId") Integer svcId, ModelMap model) {
		
		pgSvcItemSvc.deletePgSvcItemById(Integer.valueOf(svcId));
		List<PgSvcItemVO> list = pgSvcItemSvc.getRealAll();
		model.addAttribute("list", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back_end/pg/pgSvcItemManagement"; 
	}
	

	// 瀏覽所有"已顯示"美容師服務項目
	@GetMapping("/showAllPgSvcItem")
	public String listAllPgSvcItem(ModelMap model) {
		List<PgSvcItemVO> visibleItems = pgSvcItemSvc.getAllVisible();
		model.addAttribute("list", visibleItems);
		return "front_end/pg/showSvcDetail"; // Thymeleaf 頁面名稱
	}

	// 瀏覽選定的服務項目資訊
	@GetMapping("/showServiceDetail")
	public String showServiceDetail(@RequestParam(value = "svcName", required = false) String svcName, ModelMap model) {
		// 獲取所有服務項目數據
		List<PgSvcItemVO> services = pgSvcItemSvc.getAllVisible();
		model.addAttribute("list", services);

		// 如果 svcName 不為空，查找對應的服務項目
		if (svcName != null && !svcName.isEmpty()) {
			PgSvcItemVO selectedService = pgSvcItemSvc.getServiceByName(svcName);
			model.addAttribute("selectedService", selectedService);
		} else {
			model.addAttribute("selectedService", null); // 未選擇任何服務項目
		}

		return "front_end/pg/showSvcDetail"; // Thymeleaf 頁面名稱
	}

	// 編輯
	@GetMapping("/editPgSvcItem")
	public String editPgSvcItem(@RequestParam("svcId") Integer svcId, ModelMap model) {
		// 獲取該服務項目的數據
		PgSvcItemVO item = pgSvcItemSvc.getById(svcId);
		model.addAttribute("item", item);
		return "back_end/pg/editPgSvcItem"; // 返回編輯頁面
	}

	// 編輯-處理表單提交
	@PostMapping("/savePgSvcItem")
	public String savePgSvcItem(@ModelAttribute PgSvcItemVO pgSvcItemVO) {
		// 更新服務項目數據
		pgSvcItemSvc.updatePgSvcItem(pgSvcItemVO);
		return "redirect:/pg/listRealAllPgSvcItem"; // 保存後重定向到列表頁面
	}
	
	@PostMapping("/updateAllPgSvcItemStatus")
	public String updateAllPgSvcItemStatus(@RequestParam("svcIsDeleted") String svcIsDeleted) {
	    // 調用服務層批量更新服務項目狀態
	    pgSvcItemSvc.updateAllSvcItemStatus(svcIsDeleted);
	    // 重定向回管理頁面
	    return "redirect:/pg/listRealAllPgSvcItem";
	}


	// 調整服務項目(顯示/隱藏)
	@PostMapping("/updatePgSvcItemStatus")
	public String updatePgSvcItemStatus(@RequestParam("svcId") Integer svcId,
			@RequestParam("svcIsDeleted") String svcIsDeleted) {
		// 調用服務層更新狀態
		pgSvcItemSvc.updateSvcItemStatus(svcId, svcIsDeleted);
		// 重定向回管理頁面
		return "redirect:/pg/listRealAllPgSvcItem";
	}

	// 導向新增表單
	@GetMapping("/addPgSvcItem")
	public String createPgSvcItemForm(ModelMap model) {
		model.addAttribute("pgSvcItemVO", new PgSvcItemVO());
		return "back_end/pg/addPgSvcItem";
	}

	// 新增-處理表單提交
	@PostMapping("/createPgSvcItem")
	public String createPgSvcItem(@Valid PgSvcItemVO pgSvcItemVO, BindingResult result, 
			RedirectAttributes redirectAttributes) {
	    if (result.hasErrors()) {
	        return "back_end/pg/addPgSvcItem"; // 返回表單頁面，顯示驗證錯誤
	    }

	    // 保存到資料庫
	    pgSvcItemSvc.createPgSvcItem(pgSvcItemVO);
	    
	    // 使用 RedirectAttributes 傳遞成功消息
	    redirectAttributes.addFlashAttribute("successMessage", "美容師服務項目新增成功！");
	    
	    // 重定向到列表頁，這時 successMessage 會被保存到 session 中，頁面重載後依然可用
	    return "redirect:/pg/listRealAllPgSvcItem";
	}

	// 瀏覽所有美容師服務項目(含隱藏中)
	@GetMapping("/listRealAllPgSvcItem")
	public String listRealAllPgSvcItem(ModelMap model) {
		model.addAttribute("list", pgSvcItemSvc.getRealAll());
		return "back_end/pg/pgSvcItemManagement"; // Thymeleaf 頁面名稱
	}

}


