package com.servPet.psCompl.controller;

import com.servPet.psCompl.model.PsComplVO;
import com.servPet.admin.model.AdminService;
import com.servPet.admin.model.AdminVO;
import com.servPet.adminPer.model.AdminPerService;
import com.servPet.adminPer.model.AdminPerVO;
import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;
import com.servPet.ps.model.PsRepository;
import com.servPet.ps.model.PsService;
import com.servPet.ps.model.PsVO;
import com.servPet.psCompl.model.PsComplService;
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
@RequestMapping("/psCompl")
public class PsComplController {

	@Autowired
	private PsComplService psComplSvc;

	@Autowired
	private AdminService adminSvc;

	@Autowired
	private PsService psSvc;

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

	@GetMapping("/select_psCompl_page")
	public String select_psCompl_page(Model model,HttpSession session) {
		SaveSession(model, session);
		return "back_end/psCompl/select_psCompl_page"; // 返回模板的相對路徑
	}

	@ModelAttribute("psComplListData") // for select_psCompl_page.html 第97 109行用 // for listAllPsCompl.html 第85行用
	protected List<PsComplVO> referenceListDataPSCompl(Model model,HttpSession session) {
		SaveSession(model, session);

		List<PsComplVO> list = psComplSvc.getAll();
		return list;
	}

	// 顯示新增檢舉頁面
	@GetMapping("addPsCompl")
	public String addPsCompl(ModelMap model,HttpSession session) {
		SaveSession(model, session);
		// 修改为传递正确的属性名称，确保表单能够绑定
		model.addAttribute("psComplVO", new PsComplVO<Object>());
		return "back_end/psCompl/addPsCompl"; // 返回新增頁面
	}

	@PostMapping("insert")
	public String insertPsCompl(@Valid PsComplVO<?> psComplVO, BindingResult result,
			@RequestParam(value = "upFiles", required = false) MultipartFile[] files,
			@RequestParam(value = "mebId", required = true) Integer mebId, // 添加 mebId 參數
			@RequestParam(value = "psId", required = true) Integer psId, // 添加 psId 參數
			Model model,HttpSession session) {
			SaveSession(model, session);

		// 如果表單驗證失敗，返回新增頁面並顯示錯誤信息
		if (result.hasErrors()) {
			model.addAttribute("error", "檢舉單提交失敗！");
			model.addAttribute("errors", result.getAllErrors());
			return "back_end/psCompl/addPsCompl"; // 返回新增頁面
		}

		// 設置檢舉日期與處理日期為當前時間
		LocalDateTime currentTime = LocalDateTime.now();
		psComplVO.setPsComplDate(currentTime); // 設定檢舉日期
		psComplVO.setPsComplResDate(currentTime); // 設定處理日期

		try {
			// 設置 PsComplVO 中的 psId 和 mebId
			psComplVO.setPsId(psId); // 設定 PsVO 的 psId
			psComplVO.setMebId(mebId); // 設定 MebVO 的 mebId
		} catch (Exception e) {
			model.addAttribute("error", "無法找到指定的會員或保母！");
			return "back_end/psCompl/addPsCompl"; // 返回新增頁面並顯示錯誤信息
		}

		// 如果有上傳檔案，處理檔案保存到對應的屬性
		try {
			if (files != null && files.length > 0) {
				// 調用 Service 層的檔案處理方法
				psComplSvc.handleFileUpload(psComplVO, files);
			}
		} catch (Exception e) {
			model.addAttribute("error", "檢舉圖片上傳失敗！");
			return "back_end/psCompl/addPsCompl"; // 返回新增頁面並顯示錯誤信息
		}

		// 調用 Service 層的新增方法，將資料保存到資料庫
		psComplSvc.addPsCompl(psComplVO);
		model.addAttribute("success", "檢舉提交成功");
		model.addAttribute("psComplListData", psComplSvc.getAll());
		return "redirect:/psCompl/listAllPsCompl"; // 成功後跳轉到檢舉列表頁面
	}

	// 顯示單一檢舉資料
	@PostMapping("getOne_For_Display")
	public String getOneForDisplay(@RequestParam("psComplId") String psComplIdStr, ModelMap model,HttpSession session) {
		SaveSession(model, session);
		if (psComplIdStr == null || !psComplIdStr.matches("\\d+")) {
			model.addAttribute("errorMessage", "請提供有效的檢舉編號");
			return "back_end/psCompl/select_psCompl_page"; // 返回檢舉列表頁面
		}
		Integer psComplId = Integer.parseInt(psComplIdStr);

		// 查詢檢舉資料
		PsComplVO<?> psComplVO = psComplSvc.getOnePsCompl(psComplId);
		if (psComplVO == null) {
			model.addAttribute("errorMessage", "無此檢舉資料");
			return "back_end/psCompl/select_psCompl_page"; // 無此檢舉資料返回列表頁面
		}

		model.addAttribute("psComplVO", psComplVO);
		return "back_end/psCompl/listOnePsCompl"; // 返回顯示單一檢舉資料頁面
	}

	// 顯示更新檢舉頁面
	@PostMapping("getOne_For_Update")
	public String getOneForUpdate(@RequestParam("psComplId") String psComplId, ModelMap model,HttpSession session) {
		SaveSession(model, session);
		PsComplVO<?> psComplVO = psComplSvc.getOnePsCompl(Integer.valueOf(psComplId));
		if (psComplVO != null) {
			// 在這裡將當前的 pgComplStatus 先保存到 previousPgComplStatus，這樣可以在後面進行比較
			psComplVO.setPreviousPsComplStatus(psComplVO.getPsComplStatus());
		}
		model.addAttribute("psComplVO", psComplVO); // 修改为正确的属性名称
		return "back_end/psCompl/update_psCompl_input"; // 返回更新檢舉頁面
	}

	// 更新檢舉資料
		@PostMapping("update")
		public String update(@Valid PsComplVO psComplVO, BindingResult result, 
				ModelMap model,HttpSession session,RedirectAttributes redirectAttributes) {
		    SaveSession(model, session);
		    System.out.println("Before update PsComplDate: " + psComplVO.getPsComplDate());

		    // 從資料庫重新載入 PsComplVO，確保取得完整的 pgComplDate 等資料
		    PsComplVO updatedPsComplVO = psComplSvc.getOnePsCompl(psComplVO.getPsComplId());
		    if (updatedPsComplVO == null) {
		        model.addAttribute("error", "找不到檢舉資料，請稍後再試！");
		        return "back_end/psCompl/list"; // 如果找不到資料，跳轉回列表頁面
		    }
		    
		    if (psComplVO.getPsComplUpfiles1() == null || psComplVO.getPsComplUpfiles1()==null) {
		        psComplVO.setPsComplUpfiles1(updatedPsComplVO.getPsComplUpfiles1());  // 保持資料庫中的圖片路徑
		    }
		    
		    if (psComplVO.getPsComplUpfiles2() == null || psComplVO.getPsComplUpfiles2()==null) {
		        psComplVO.setPsComplUpfiles2(updatedPsComplVO.getPsComplUpfiles2());  // 保持資料庫中的圖片路徑
		    }
		    
		    if (psComplVO.getPsComplUpfiles3() == null || psComplVO.getPsComplUpfiles3()==null) {
		        psComplVO.setPsComplUpfiles3(updatedPsComplVO.getPsComplUpfiles3());  // 保持資料庫中的圖片路徑
		    }
		    
		    if (psComplVO.getPsComplUpfiles4() == null || psComplVO.getPsComplUpfiles4()==null) {
		        psComplVO.setPsComplUpfiles4(updatedPsComplVO.getPsComplUpfiles4());  // 保持資料庫中的圖片路徑
		    }

		    // 如果表單驗證失敗，返回更新頁面並保留現有的錯誤信息與資料
		    if (result.hasErrors()) {
		        model.addAttribute("psComplVO", updatedPsComplVO);
		        model.addAttribute("psComplVO", psComplVO); // 保留錯誤信息與使用者輸入的資料
		        return "back_end/psCompl/update_psCompl_input"; // 返回更新頁面
		    }

		    // 保證 pgComplDate 不變，除非使用者明確修改
		    psComplVO.setPsComplDate(updatedPsComplVO.getPsComplDate()); // 保持資料庫中的 pgComplDate 值不變

		    // 設定更新處理時間
		    psComplVO.setPsComplResDate(LocalDateTime.now());

		    // ------------------ 添加記點更新邏輯 ------------------
		    // 取得目前的 pgId
		    Integer psId = psComplVO.getPsId();

		    // 取得目前 PG 物件
		    PsVO psVO = psSvc.getOnePs(psId);

		    if (psVO != null) {
		        // 取得原本的 REPORT_TIMES（即原始的記點）
		    	int originalPoints = psVO.getReportTimes();
		    	
		        // 取得原始的懲處狀態
		        String previousStatus = updatedPsComplVO.getPsComplStatus(); // 假設之前的懲處狀態保存在 updatedPgComplVO 中

		        // 更新檢舉資料
		        psComplSvc.updatePsCompl(psComplVO);

		        // 取得新的懲處狀態
		        String currentStatus = psComplVO.getPsComplStatus(); // 當前狀態來自於提交的表單

		        // 根據狀態變更計算點數變化
		        int pointsToChange = calculatePointsDifference(previousStatus, currentStatus);

		        // 更新 REPORT_TIMES（記點）
		        int newPoints = originalPoints + pointsToChange;
		        psVO.setReportTimes(newPoints); // 更新 PG 的 REPORT_TIMES
		        
		        // 計算是否需要改變狀態
		        if (newPoints >= 5 && originalPoints < 5) {
		            psVO.setPsStatus("2");  // 停權
		            adminSvc.sendSuspensionEmailPs(psVO); // 發送停權通知郵件
		            model.addAttribute("message", "已發送停權通知郵件");
		        } else if (originalPoints >= 5 && newPoints < 5) {
		            psVO.setPsStatus("0");  // 休業
		        }

		        // 呼叫 adminService 更新會員點數
		        adminSvc.updatePsReportTimes(psVO); // 呼叫 adminService 來更新 PG 的報告點數
		    }

		    // 顯示修改成功訊息
		    model.addAttribute("success", "- (修改成功)");

		    // 更新後重新查詢並保持 pgComplVO 到模型中
		    psComplVO = psComplSvc.getOnePsCompl(psComplVO.getPsComplId());
		    model.addAttribute("psComplVO", psComplVO); // 保持 pgComplVO 到模型中
		    
		    // 跳轉到顯示單一檢舉頁面，並帶上 pgComplId
		    return "back_end/psCompl/listOnePsCompl";
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
		@GetMapping("/listAllPsCompl")
		public String listAllPsCompl(Model model,HttpSession session) {
			SaveSession(model, session);
			List<PsComplVO> psComplVOList = psComplSvc.getAll();
			// 將 adminList 加入到模型中
			PsComplVO psComplVO = (PsComplVO) session.getAttribute("psComplVO");
			model.addAttribute("psComplListData", psComplVOList);
			return "back_end/psCompl/listAllPsCompl"; // 顯示所有檢舉資料
		}

		@PostMapping("listPsCompl_ByCompositeQuery")
		public String listAllPsCompl(HttpServletRequest req, Model model, HttpSession session) {
			SaveSession(model, session);
			Map<String, String[]> map = req.getParameterMap();
			List<PsComplVO> list = psComplSvc.getAll(map);
			model.addAttribute("psComplListData", list);
			return "back_end/psCompl/listAllPsCompl"; // 顯示所有管理員
		}

}
