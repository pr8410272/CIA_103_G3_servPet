package com.servPet.pgSvc.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.servPet.pg.model.PgService;
import com.servPet.pg.model.PgVO;
import com.servPet.pgSvc.model.PgServiceDetailsDTO;
import com.servPet.pgSvc.model.PgSvcService;
import com.servPet.pgSvc.model.PgSvcVO;
import com.servPet.pgSvcItem.model.PgSvcItemService;
import com.servPet.pgSvcItem.model.PgSvcItemVO;

@Controller
@RequestMapping("/pg")
public class PgSvcController {

	@Autowired
	private PgSvcService pgSvcSvc;
	@Autowired
	private PgService pgSvc;
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
		

	// 顯示新增服務項目表單，並附帶所有可用服務項目
	@GetMapping("/addService")
	public String showAddServiceForm(@RequestParam(value = "svcName", required = false) String svcName,HttpSession session, Model model) {
		SaveSession(model, session);
		// 獲取所有"已顯示"的服務項目
		List<PgSvcItemVO> serviceItems = pgSvcItemSvc.getAllVisible();
		model.addAttribute("serviceItems", serviceItems);
		System.out.println(serviceItems);

		// 如果 svcName 不為空，根據 svcName 獲取詳細資訊
		if (svcName != null && !svcName.isEmpty()) {
			PgSvcItemVO selectedService = pgSvcItemSvc.getServiceByName(svcName);
			model.addAttribute("selectedService", selectedService);
		} else {
			model.addAttribute("selectedService", null);
		}

		// 添加空的 PgSvcVO 以綁定表單數據
		model.addAttribute("pgSvcVO", new PgSvcVO());
		return "back_end/pg/pgAddService"; // 返回新增服務表單頁面
	}

	@PostMapping("/addService")
	public String addService(@ModelAttribute("pgSvcVO") PgSvcVO pgSvcVO, @RequestParam("svcId") Integer svcId, 																							// SVC_ID
			HttpSession session, Model model) {
		
		// 檢查登錄狀態 
	    if (!isPgLoggedIn(session)) {
	        return "redirect:/back_end/toPgLogin"; // 如果未登入，重導到登入頁
	    }
		SaveSession(model, session);
		System.out.println("成功新增美容項目");
		PgVO pg = (PgVO) session.getAttribute("pgVO");
		Integer pgId = pg.getPgId();
		// 設置 PG_ID 和 SVC_ID
				pgSvcVO.setPgId(pgId);
				pgSvcVO.setSvcId(svcId);

		

		try {
			pgSvcSvc.addService(pgSvcVO);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "新增服務時發生錯誤：" + e.getMessage());
			return "back_end/pg/addServiceForm"; // 返回新增頁面
		}

		return "redirect:/pg/pgSvcListManagement?pgId=" + pg.getPgId() ;
	}

	// 刪除服務項目
	@GetMapping("/deletePgSvc")
	public String deletePgSvc(@RequestParam("pgSvcId") Integer pgSvcId, ModelMap model) {

		// 根據 pgSvcId 查詢對應的 pgId
		PgSvcVO deletedPgSvc = pgSvcSvc.getPgSvcById(pgSvcId);
		Integer pgId = deletedPgSvc.getPgId();
		// 刪除服務項目
		pgSvcSvc.deletePgSvcByPgSvcId(pgSvcId);

		// 重新查詢美容師及其服務項目
		PgVO pgVO = pgSvc.getOnePg(pgId);
		List<PgServiceDetailsDTO> services = pgSvcSvc.getServicesWithDetails(pgId);

		model.addAttribute("pgVO", pgVO);
		model.addAttribute("services", services);

		// 重定向至服務列表管理頁面
		return "redirect:/pg/pgSvcListManagement?pgId=" + pgId;

	}

	// 編輯
	@GetMapping("/editPgSvcList")
	public String editPgSvcItem(@RequestParam("pgSvcId") Integer pgSvcId, ModelMap model, HttpSession session) {
		SaveSession(model, session);
		// 獲取該服務項目的數據
		PgServiceDetailsDTO content = pgSvcSvc.getById(pgSvcId);

		model.addAttribute("content", content);
		return "back_end/pg/editPgSvcList"; // 返回編輯頁面
	}

	// 編輯美容師服務項目
	@PostMapping("/updatePgSvc")
	public String updatePgSvc(@RequestParam("pgSvcId") Integer pgSvcId, @RequestParam("svcType") String svcType,
			@RequestParam("svcPrice") Integer svcPrice, ModelMap model) {
		// 獲取原始服務項目
		PgServiceDetailsDTO content = pgSvcSvc.getById(pgSvcId);

		if (content == null) {
			model.addAttribute("errorMessage", "無法找到對應的服務項目");
			return "back_end/error"; // 返回錯誤頁面
		}

		// 更新可編輯欄位
		PgSvcVO pgSvcVO = new PgSvcVO();
		pgSvcVO.setPgSvcId(content.getPgSvcId());
		pgSvcVO.setPgId(content.getPgId());
		pgSvcVO.setSvcId(content.getSvcId());
		pgSvcVO.setSvcType(svcType); // 更新體型
		pgSvcVO.setSvcPrice(svcPrice); // 更新價格

		// 保存更新到數據庫
		pgSvcSvc.update(pgSvcVO);

		// 重定向回服務項目列表頁面
		return "redirect:/pg/pgSvcListManagement?pgId=" + content.getPgId();
	}

	// 連結美容師個人資訊瀏覽服務項目清單
	@GetMapping("/pgSvcListManagement")
	public String getPgSvcDetails(@RequestParam("pgId") Integer pgId, ModelMap model,HttpSession session) {
		SaveSession(model, session);
		
		// 查詢美容師的基本資料
		PgVO pgVO = pgSvc.getOnePg(pgId);

		// 查詢屬於該美容師的服務清單
		List<PgServiceDetailsDTO> services = pgSvcSvc.getServicesWithDetails(pgId);

		// 將美容師資料和清單列表添加到模型中

		model.addAttribute("pgVO", pgVO);
		model.addAttribute("services", services);

		// 返回對應的視圖頁面
		return "back_end/pg/pgSvcListManagement";
	}

}

