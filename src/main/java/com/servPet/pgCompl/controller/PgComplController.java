package com.servPet.pgCompl.controller;

import com.servPet.pgCompl.model.PgComplVO;
import com.servPet.admin.model.AdminService;
import com.servPet.admin.model.AdminVO;
import com.servPet.adminPer.model.AdminPerService;
import com.servPet.adminPer.model.AdminPerVO;
import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;
import com.servPet.pg.model.PgRepository;
import com.servPet.pg.model.PgService;
import com.servPet.pg.model.PgVO;
import com.servPet.pgCompl.model.PgComplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/pgCompl")
public class PgComplController {

	@Autowired
	private PgComplService pgComplSvc;

	@Autowired
	private AdminService adminSvc;

	@Autowired
	private PgService pgSvc;

	@Autowired
	private MebService mebSvc;

	@Autowired
    private AdminPerService adminPerSvc;

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

	@GetMapping("/select_pgCompl_page")
	public String select_pgCompl_page(Model model,HttpSession session) {
		SaveSession(model, session);
		return "back_end/pgCompl/select_pgCompl_page"; // 返回模板的相對路徑
	}

	@ModelAttribute("pgComplListData") // for select_pgCompl_page.html 第97 109行用 // for listAllPgCompl.html 第85行用
	protected List<PgComplVO> referenceListDataPGCompl(Model model,HttpSession session) {
		SaveSession(model, session);

		List<PgComplVO> list = pgComplSvc.getAll();
		return list;
	}

	// 顯示新增檢舉頁面
	@GetMapping("addPgCompl")
	public String addPgCompl(ModelMap model,HttpSession session) {
		SaveSession(model, session);
		// 修改为传递正确的属性名称，确保表单能够绑定
		model.addAttribute("pgComplVO", new PgComplVO<Object>());
		return "back_end/pgCompl/addPgCompl"; // 返回新增頁面
	}

	@PostMapping("insert")
	public String insertPgCompl(@Valid PgComplVO pgComplVO, BindingResult result,
			@RequestParam(value = "upFiles", required = false) MultipartFile[] files,
			@RequestParam(value = "mebId", required = true) Integer mebId, // 添加 mebId 參數
			@RequestParam(value = "pgId", required = true) Integer pgId, // 添加 pgId 參數
			Model model,HttpSession session) {
			SaveSession(model, session);

		// 如果表單驗證失敗，返回新增頁面並顯示錯誤信息
		if (result.hasErrors()) {
			model.addAttribute("error", "檢舉單提交失敗！");
			model.addAttribute("errors", result.getAllErrors());
			return "back_end/pgCompl/addPgCompl"; // 返回新增頁面
		}

		// 設置檢舉日期與處理日期為當前時間
		LocalDateTime currentTime = LocalDateTime.now();
		pgComplVO.setPgComplDate(currentTime); // 設定檢舉日期
		pgComplVO.setPgComplResDate(currentTime); // 設定處理日期

		try {
			// 設置 PgComplVO 中的 pgId 和 mebId
			pgComplVO.setPgId(pgId); // 設定 PgVO 的 pgId
			pgComplVO.setMebId(mebId); // 設定 MebVO 的 mebId
		} catch (Exception e) {
			model.addAttribute("error", "無法找到指定的會員或保母！");
			return "back_end/pgCompl/addPgCompl"; // 返回新增頁面並顯示錯誤信息
		}

		// 如果有上傳檔案，處理檔案保存到對應的屬性
		try {
			if (files != null && files.length > 0) {
				// 調用 Service 層的檔案處理方法
				pgComplSvc.handleFileUpload(pgComplVO, files);
			}
		} catch (Exception e) {
			model.addAttribute("error", "檢舉圖片上傳失敗！");
			return "back_end/pgCompl/addPgCompl"; // 返回新增頁面並顯示錯誤信息
		}

		// 調用 Service 層的新增方法，將資料保存到資料庫
		pgComplSvc.addPgCompl(pgComplVO);
		model.addAttribute("success", "檢舉提交成功");
		model.addAttribute("pgComplListData", pgComplSvc.getAll());
		return "redirect:/pgCompl/listAllPgCompl"; // 成功後跳轉到檢舉列表頁面
	}

	// 顯示單一檢舉資料
	@PostMapping("getOne_For_Display")
	public String getOneForDisplay(@RequestParam("pgComplId") String pgComplIdStr, ModelMap model,HttpSession session) {
		SaveSession(model, session);
		if (pgComplIdStr == null || !pgComplIdStr.matches("\\d+")) {
			model.addAttribute("errorMessage", "請提供有效的檢舉編號");
			return "back_end/pgCompl/select_pgCompl_page"; // 返回檢舉列表頁面
		}
		Integer pgComplId = Integer.parseInt(pgComplIdStr);

		// 查詢檢舉資料
		PgComplVO pgComplVO = pgComplSvc.getOnePgCompl(pgComplId);
		if (pgComplVO == null) {
			model.addAttribute("errorMessage", "無此檢舉資料");
			return "back_end/pgCompl/select_pgCompl_page"; // 無此檢舉資料返回列表頁面
		}

		model.addAttribute("pgComplVO", pgComplVO);
		return "back_end/pgCompl/listOnePgCompl"; // 返回顯示單一檢舉資料頁面
	}

	// 顯示更新檢舉頁面
	@PostMapping("getOne_For_Update")
	public String getOneForUpdate(@RequestParam("pgComplId") String pgComplId, ModelMap model,HttpSession session) {
		SaveSession(model, session);
		PgComplVO pgComplVO = pgComplSvc.getOnePgCompl(Integer.valueOf(pgComplId));
		if (pgComplVO != null) {
			// 在這裡將當前的 pgComplStatus 先保存到 previousPgComplStatus，這樣可以在後面進行比較
			pgComplVO.setPreviousPgComplStatus(pgComplVO.getPgComplStatus());
		}
		model.addAttribute("pgComplVO", pgComplVO); // 修改为正确的属性名称
		return "back_end/pgCompl/update_pgCompl_input"; // 返回更新檢舉頁面
	}
	
	//============ 更 新 ============//
	@PostMapping("update")
	public String update(@Valid PgComplVO pgComplVO, BindingResult result, 
	        ModelMap model,HttpSession session,RedirectAttributes redirectAttributes) {
	    
	    SaveSession(model, session);

	    // 從資料庫重新載入 PgComplVO，確保取得完整的 pgComplDate 等資料
	    PgComplVO updatedPgComplVO = pgComplSvc.getOnePgCompl(pgComplVO.getPgComplId());
	    if (updatedPgComplVO == null) {
	        model.addAttribute("error", "找不到檢舉資料，請稍後再試！");
	        return "back_end/pgCompl/list"; // 如果找不到資料，跳轉回列表頁面
	    }
	    
	    if (pgComplVO.getPgComplUpfiles1() == null || pgComplVO.getPgComplUpfiles1()==null) {
	        pgComplVO.setPgComplUpfiles1(updatedPgComplVO.getPgComplUpfiles1());  // 保持資料庫中的圖片路徑
	    }
	    
	    if (pgComplVO.getPgComplUpfiles2() == null || pgComplVO.getPgComplUpfiles2()==null) {
	        pgComplVO.setPgComplUpfiles2(updatedPgComplVO.getPgComplUpfiles2());  // 保持資料庫中的圖片路徑
	    }
	    
	    if (pgComplVO.getPgComplUpfiles3() == null || pgComplVO.getPgComplUpfiles3()==null) {
	        pgComplVO.setPgComplUpfiles3(updatedPgComplVO.getPgComplUpfiles3());  // 保持資料庫中的圖片路徑
	    }
	    
	    if (pgComplVO.getPgComplUpfiles4() == null || pgComplVO.getPgComplUpfiles4()==null) {
	        pgComplVO.setPgComplUpfiles4(updatedPgComplVO.getPgComplUpfiles4());  // 保持資料庫中的圖片路徑
	    }

	    // 如果表單驗證失敗，返回更新頁面並保留現有的錯誤信息與資料
	    if (result.hasErrors()) {
	        model.addAttribute("pgComplVO", updatedPgComplVO);
	        model.addAttribute("pgComplVO", pgComplVO); // 保留錯誤信息與使用者輸入的資料
	        return "back_end/pgCompl/update_pgCompl_input"; // 返回更新頁面
	    }

	    // 保證 pgComplDate 不變，除非使用者明確修改
	    pgComplVO.setPgComplDate(updatedPgComplVO.getPgComplDate()); // 保持資料庫中的 pgComplDate 值不變

	    // 設定更新處理時間
	    pgComplVO.setPgComplResDate(LocalDateTime.now());

	    // ------------------ 添加記點更新邏輯 ------------------
	    // 取得目前的 pgId
	    Integer pgId = pgComplVO.getPgId();

	    // 取得目前 PG 物件
	    PgVO pgVO = pgSvc.getOnePg(pgId);

	    if (pgVO != null) {
	        // 取得原本的 REPORT_TIMES（即原始的記點）
	        int originalPoints = pgVO.getReportTimes(); // 假設 REPORT_TIMES 是 pgVO 的一個欄位
	        // 取得原始的懲處狀態
	        String previousStatus = updatedPgComplVO.getPgComplStatus(); // 假設之前的懲處狀態保存在 updatedPgComplVO 中

	        // 更新檢舉資料
	        pgComplSvc.updatePgCompl(pgComplVO);

	        // 取得新的懲處狀態
	        String currentStatus = pgComplVO.getPgComplStatus(); // 當前狀態來自於提交的表單

	        // 根據狀態變更計算點數變化
	        int pointsToChange = calculatePointsDifference(previousStatus, currentStatus);

	        // 更新 REPORT_TIMES（記點）
	        int newPoints = originalPoints + pointsToChange;
	        pgVO.setReportTimes(newPoints); // 更新 PG 的 REPORT_TIMES
	        
	        // 計算是否需要改變狀態
	        if (newPoints >= 5 && originalPoints < 5) {
	            pgVO.setPgStatus("2");  // 停權
	            adminSvc.sendSuspensionEmailPg(pgVO); // 發送停權通知郵件
	            model.addAttribute("message", "已發送停權通知郵件");
	        } else if (originalPoints >= 5 && newPoints < 5) {
	            pgVO.setPgStatus("0");  // 休業
	        }

	        // 呼叫 adminService 更新會員點數
	        adminSvc.updatePgReportTimes(pgVO); // 呼叫 adminService 來更新 PG 的報告點數
	    }

	    // 顯示修改成功訊息
	    model.addAttribute("success", "- (修改成功)");

	    // 更新後重新查詢並保持 pgComplVO 到模型中
	    pgComplVO = pgComplSvc.getOnePgCompl(pgComplVO.getPgComplId());
	    model.addAttribute("pgComplVO", pgComplVO); // 保持 pgComplVO 到模型中
	    
	    // 跳轉到顯示單一檢舉頁面，並帶上 pgComplId
	    return "back_end/pgCompl/listOnePgCompl";
	}

	// 計算點數差異的方法
	private int calculatePointsDifference(String previousStatus, String currentStatus) {
	    int previousPoints = getStatusPoints(previousStatus);
	    int currentPoints = getStatusPoints(currentStatus);
	    System.out.println("currentPoints=" + currentPoints);
	    System.out.println("previousPoints=" + previousPoints);
	    return currentPoints - previousPoints; // 返回點數差異
	}

	// 狀態映射
	private int getStatusPoints(String status) {
		switch (status) {
		case "0":
			return 0;
		case "1":
			return 0;
		case "2":
			return 1;
		case "3":
			return 3;
		case "4":
			return 5;
		default:
			return 0;
		}
	}
	
	// 查詢所有檢舉資料
	@GetMapping("/listAllPgCompl")
	public String listAllPgCompl(Model model,HttpSession session) {
		SaveSession(model, session);
		List<PgComplVO> pgComplVOList = pgComplSvc.getAll();
		// 將 adminList 加入到模型中
		PgComplVO pgComplVO = (PgComplVO) session.getAttribute("pgComplVO");
		model.addAttribute("pgComplListData", pgComplVOList);
		return "back_end/pgCompl/listAllPgCompl"; // 顯示所有檢舉資料
	}

	@PostMapping("listPgCompl_ByCompositeQuery")
	public String listAllPgCompl(HttpServletRequest req, Model model, HttpSession session) {
		SaveSession(model, session);
		Map<String, String[]> map = req.getParameterMap();
		List<PgComplVO> list = pgComplSvc.getAll(map);
		model.addAttribute("pgComplListData", list);
		return "back_end/pgCompl/listAllPgCompl"; // 顯示所有管理員
	}

}
