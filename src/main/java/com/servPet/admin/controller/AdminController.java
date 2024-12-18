package com.servPet.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.servPet.admin.model.AdminService;
import com.servPet.admin.model.AdminVO;
//==============以下報錯直接註解===============//
import com.servPet.adminPer.model.AdminPerService;
import com.servPet.adminPer.model.AdminPerVO;
import com.servPet.fnc.model.FncService;
import com.servPet.fnc.model.FncVO;
import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;
import com.servPet.pg.model.PgService;
import com.servPet.pg.model.PgVO;
import com.servPet.pgOrder.model.PgOrderService;
import com.servPet.pgOrder.model.PgOrderVO;
import com.servPet.ps.model.PsService;
import com.servPet.ps.model.PsVO;
import com.servPet.psOrder.model.PsOrderService;
import com.servPet.psOrder.model.PsOrderVO;


import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminService adminSvc;
	
	@Autowired
	AdminPerService adminPerSvc;
	
	@Autowired
	FncService fncSvc;

	@Autowired
	MebService mebSvc;

	@Autowired
	PgService pgSvc;

	@Autowired
	PsService psSvc;

	@Autowired
	PgOrderService pgOrderSvc;

	@Autowired
	PsOrderService psOrderSvc;

	private boolean isAdminLoggedIn(HttpSession session) {
	    if (isSessionExpired(session)) {
	        // 會話過期的情況，返回 false，不進行錯誤頁面處理
	        return false;
	    }
	    // 確保 session 中有 "adminVO" 屬性
	    return session.getAttribute("adminVO") != null;
	}

	// 檢查 session 是否過期
	private boolean isSessionExpired(HttpSession session) {
	    // 會話是否過期，這裡不拋出異常，直接返回過期狀態
	    return session.getAttribute("adminVO") == null;
	}

	// ===<< 將admin存於 session 中一起送出 >>===/
	public String SaveSession(Model model, HttpSession session) {
		if (!isAdminLoggedIn(session)) {
			return "back_end/login"; 
		}
		AdminVO admin = (AdminVO) session.getAttribute("adminVO");
		model.addAttribute("adminVO", new AdminVO());
		model.addAttribute("admin", admin);
		
		
		// 預設 allowedFunctionIds 為空集合
		Set<Integer> allowedFunctionIds = Collections.emptySet();

		if (admin.getAdminId() != null) {
		    // 根據 adminId 查詢該管理員有權限的功能列表
		    List<AdminPerVO> adminPer = adminPerSvc.getPerByAdminId(admin.getAdminId());

		    // 檢查 adminPer 是否為 null 或空集合
		    if (adminPer != null && !adminPer.isEmpty()) {
		        // 如果有權限資料，將其轉換為 Set
		        allowedFunctionIds = adminPer.stream()
		            .map(adminPerVO -> adminPerVO.getFncVO().getFncId())
		            .collect(Collectors.toSet());
		    }
		}
		
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

	// ===========<< Insert 新增單一管理員資料 Insert >>============/
	@PostMapping("insert")
	public String insert(@Valid AdminVO adminVO, BindingResult result, ModelMap model,
			@RequestParam("upFiles") MultipartFile[] parts, HttpSession session) throws IOException {
		SaveSession(model, session);
		result = removeFieldError(adminVO, result, "upFiles");
		
		if (result.hasErrors() || parts == null || parts.length == 0 || parts[0].isEmpty()) {
			model.addAttribute("errorMessage", "管理員照片: 請上傳照片");
			System.out.println(result);
			System.out.println(parts);
			return "back_end/admin/addAdmin"; // 返回新增頁面，並將錯誤訊息顯示在表單中
		}

		// 處理文件上傳
	    if (parts != null && parts.length > 0 && !parts[0].isEmpty()) {
	        byte[] buf = parts[0].getBytes();  // 假設只處理單張上傳圖片
	        adminVO.setUpFiles(buf);
	    } else {
	        model.addAttribute("errorMessage", "管理員照片: 請上傳照片");
	        return "back_end/admin/addAdmin";  // 返回新增頁面，並將錯誤訊息顯示在表單中
	    }
	    
		// 新增管理員資料
		adminSvc.addAdmin(adminVO);
		// 顯示成功訊息並列出所有管理員
		
		// 如果管理員角色為 1，為其分配功能
		if (adminVO.getAdminRole().equals("1")) {
	        assignFunctionsToAdmin1(adminVO.getAdminId());  // 進行功能分配
	    }else if(adminVO.getAdminRole().equals("2")) {
	    	assignFunctionsToAdmin2(adminVO.getAdminId());  // 進行功能分配
	    }else if(adminVO.getAdminRole().equals("3")) {
	    	assignFunctionsToAdmin3(adminVO.getAdminId());
	    }
		model.addAttribute("adminListData", adminSvc.getAll());
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/admin/listAllAdmin"; // 新增後重導至列表頁面
	}
	
	
	private void assignFunctionsToAdmin1(int adminId) {
	    List<Integer> fncIds = List.of(16001, 16002, 16003, 16004, 16005, 16006, 16007, 
	                                    16008, 16009, 16010, 16011, 16012, 16013, 16014, 
	                                    16015, 16016, 16017, 16020, 16021, 16022);
	    
	    for (Integer fncId : fncIds) {
	        FncVO fncVO = fncSvc.getOneFnc(fncId);
	        if (fncVO != null) {
	            AdminVO adminVO = adminSvc.getOneAdmin(adminId);  // 從資料庫獲取管理員
	            if (adminVO != null) {
	                AdminPerVO adminPerVO = new AdminPerVO();
	                adminPerVO.setAdminVO(adminVO);  // 設定管理員
	                adminPerVO.setFncVO(fncVO);  // 設定功能
	                adminPerSvc.addAdminPer(adminPerVO);  // 儲存功能權限資料
	                
	                // 調試訊息
	                System.out.println("為管理員 " + adminId + " 分配功能 ID: " + fncId);
	            } else {
	                System.out.println("找不到管理員 ID: " + adminId);
	            }
	        } else {
	            System.out.println("找不到功能 ID: " + fncId);
	        }
	    }
	}
	
	private void assignFunctionsToAdmin2(int adminId) {
	    List<Integer> fncIds = List.of(16004, 16005, 16006, 16007, 
	                                    16008, 16009, 16010, 16011, 16012, 16013, 16014, 
	                                    16015, 16016, 16017, 16020, 16021, 16022);
	    
	    for (Integer fncId : fncIds) {
	        FncVO fncVO = fncSvc.getOneFnc(fncId);
	        if (fncVO != null) {
	            AdminVO adminVO = adminSvc.getOneAdmin(adminId);  // 從資料庫獲取管理員
	            if (adminVO != null) {
	                AdminPerVO adminPerVO = new AdminPerVO();
	                adminPerVO.setAdminVO(adminVO);  // 設定管理員
	                adminPerVO.setFncVO(fncVO);  // 設定功能
	                adminPerSvc.addAdminPer(adminPerVO);  // 儲存功能權限資料
	                
	                // 調試訊息
	                System.out.println("為管理員 " + adminId + " 分配功能 ID: " + fncId);
	            } else {
	                System.out.println("找不到管理員 ID: " + adminId);
	            }
	        } else {
	            System.out.println("找不到功能 ID: " + fncId);
	        }
	    }
	}
	
	private void assignFunctionsToAdmin3(int adminId) {
	    List<Integer> fncIds = List.of(16006, 16007, 16008, 16009);
	    
	    for (Integer fncId : fncIds) {
	        FncVO fncVO = fncSvc.getOneFnc(fncId);
	        if (fncVO != null) {
	            AdminVO adminVO = adminSvc.getOneAdmin(adminId);  // 從資料庫獲取管理員
	            if (adminVO != null) {
	                AdminPerVO adminPerVO = new AdminPerVO();
	                adminPerVO.setAdminVO(adminVO);  // 設定管理員
	                adminPerVO.setFncVO(fncVO);  // 設定功能
	                adminPerSvc.addAdminPer(adminPerVO);  // 儲存功能權限資料
	                
	                // 調試訊息
	                System.out.println("為管理員 " + adminId + " 分配功能 ID: " + fncId);
	            } else {
	                System.out.println("找不到管理員 ID: " + adminId);
	            }
	        } else {
	            System.out.println("找不到功能 ID: " + fncId);
	        }
	    }
	}

	// ===========<< 查詢單一管理員資料 >>============/
	@PostMapping("getOne_For_Display")
	public String getOneForDisplay(@RequestParam("adminId") String adminIdStr, ModelMap model, HttpSession session) {
		SaveSession(model, session);
		// 使用正則表達式檢查 adminId 是否為有效的數字
		if (adminIdStr == null || !adminIdStr.matches("\\d+")) {
			model.addAttribute("errorMessage", "請提供有效的管理員編號");
			return "back_end/admin/select_admin_page"; // 返回管理員列表頁面
		}
		Integer adminId = Integer.parseInt(adminIdStr);
		AdminVO adminVO = adminSvc.getOneAdmin(adminId);
		if (adminVO == null) {
			model.addAttribute("errorMessage", "查無此管理員資料");
			return "back_end/admin/select_admin_page"; // 無此管理員則返回管理員管理頁面
		}
		model.addAttribute("adminVO", adminVO);
		return "back_end/admin/listOneAdmin"; // 返回顯示管理員資料的頁面
	}

	// ===========<< 導向update頁面 >>============/
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("adminId") String adminId, ModelMap model, HttpSession session) {
		SaveSession(model, session);
		AdminVO adminVO = adminSvc.getOneAdmin(Integer.valueOf(adminId));
		if (adminVO == null) {
			model.addAttribute("errorMessage", "無此管理員資料");
			return "back_end/admin/select_admin_page"; // 返回管理員查詢頁面
		}
		model.addAttribute("adminVO", adminVO);
		return "back_end/admin/update_admin_input"; // 查詢完成後轉交更新頁面
	}

	// ===========<< 更新單一管理員資料 >>============/
	@PostMapping("update")
	public String update(@Valid AdminVO adminVO, BindingResult result, ModelMap model,
	                     @RequestParam("upFiles") MultipartFile[] parts, HttpSession session) throws IOException {
	    SaveSession(model, session);
	    
	    // 去除BindingResult中upFiles欄位的FieldError紀錄
	    result = removeFieldError(adminVO, result, "upFiles");
	    
	    // 處理文件上傳
	    if (parts.length == 0 || parts[0].isEmpty()) {
	        // 如果沒有選擇新圖片，保留舊圖片
	        byte[] upFiles = adminSvc.getOneAdmin(adminVO.getAdminId()).getUpFiles();
	        adminVO.setUpFiles(upFiles);
	    } else {
	        // 上傳新圖片
	        try {
	            adminVO.setUpFiles(parts[0].getBytes()); // 假設只處理單張上傳圖片
	        } catch (IOException e) {
	            model.addAttribute("errorMessage", "圖片上傳錯誤，請重試。");
	            return "back_end/admin/update_admin_input";
	        }
	    }

	    // 如果驗證錯誤，返回更新頁面並重新查詢該筆資料
	    if (result.hasErrors()) {
	        model.addAttribute("errorMessage", "表單驗證失敗，請檢查您的輸入。");
	        // 重新查詢該筆資料
	        AdminVO currentAdmin = adminSvc.getOneAdmin(adminVO.getAdminId());
	        model.addAttribute("adminVO", currentAdmin);
	        
	        // 顯示具體的錯誤欄位
	        List<FieldError> fieldErrors = result.getFieldErrors();
	        for (FieldError fieldError : fieldErrors) {
	            model.addAttribute(fieldError.getField() + ".error", fieldError.getDefaultMessage());
	        }

	        return "back_end/admin/update_admin_input";
	    }

	    // 更新管理員資料
	    try {
	        adminSvc.updateAdmin(adminVO);
	    } catch (Exception e) {
	        model.addAttribute("errorMessage", "更新失敗，請稍後再試。");
	        AdminVO currentAdmin = adminSvc.getOneAdmin(adminVO.getAdminId());
	        model.addAttribute("adminVO", currentAdmin);
	        return "back_end/admin/update_admin_input";
	    }

	    // 顯示更新成功訊息並重新查詢資料
	    model.addAttribute("success", "- (修改成功)");
	    AdminVO updatedAdmin = adminSvc.getOneAdmin(adminVO.getAdminId());
	    model.addAttribute("adminVO", updatedAdmin);
	    return "back_end/admin/listOneAdmin"; // 更新後轉到管理員詳細頁面
	}


	// 去除BindingResult中某個欄位的FieldError紀錄
	private BindingResult removeFieldError(AdminVO adminVO, BindingResult result, String removedFieldname) {
		List<org.springframework.validation.FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(java.util.stream.Collectors.toList());
		result = new org.springframework.validation.BeanPropertyBindingResult(adminVO, "adminVO");
		for (org.springframework.validation.FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

	// ========<< 一鍵切換管理員狀態 >>=========//
	@PostMapping("toggleStatus")
	public @ResponseBody Map<String, String> toggleStatus(@RequestParam("adminId") String adminIdStr) {
		Map<String, String> response = new HashMap<>();
		// 檢查 adminId 是否為有效數字
		if (adminIdStr == null || !adminIdStr.matches("\\d+")) {
			response.put("status", "error");
			return response; // 如果 adminId 不是數字，返回錯誤
		}
		Integer adminId = Integer.parseInt(adminIdStr);
		AdminVO adminVO = adminSvc.getOneAdmin(adminId);
		if (adminVO == null) {
			response.put("status", "error");
			return response; // 如果找不到該管理員，返回錯誤
		}
		System.out.println("Received adminId: " + adminId);
		String newStatus = adminVO.getAdminAccStatus().equals("1") ? "0" : "1";
		adminVO.setAdminAccStatus(newStatus); // 更新狀態
		adminSvc.updateAdmin(adminVO); // 儲存更新
		System.out.println("Updated status to: " + newStatus); // 日誌輸出

		// 返回更新後的狀態
		response.put("status", "success");
		response.put("newStatus", newStatus);
		return response;
	}

	// ========<< 導向select_admin_page >>=========//
	@GetMapping("select_admin_page")
	public String goSelectPage(Model model, HttpSession session) {
		SaveSession(model, session);
		return "back_end/admin/select_admin_page"; // 返回選擇頁面
	}

	// ========<< 查詢所有管理員 >>=========//
	@GetMapping("/listAllAdmin")
	public String listAllAdmin(Model model, HttpSession session) {
		SaveSession(model, session);
		// 這裡假設 adminService.getAllAdmins() 是一個從資料庫中獲取所有管理員資料的方法
		List<AdminVO> adminList = adminSvc.getAll();
		// 將 adminList 加入到模型中
		AdminVO admin = (AdminVO) session.getAttribute("adminVO");
		model.addAttribute("adminListData", adminList);
		// 返回對應的視圖名稱，即 'back_end/admin/listAllAdmin'
		return "back_end/admin/listAllAdmin";
	}
	
	// ===========<< 複合查詢管理員資料 >>============/
		@PostMapping("listAdmins_ByCompositeQuery")
		public String listAllAdmin(HttpServletRequest req, Model model, HttpSession session) {
			SaveSession(model, session);
			Map<String, String[]> map = req.getParameterMap();
			List<AdminVO> list = adminSvc.getAll(map);
			model.addAttribute("adminListData", list);
			return "back_end/admin/listAllAdmin"; // 顯示所有管理員
		}
		

	// ========<< 查詢多資料用方法>>=========//
	@ModelAttribute("adminListData") // for select_admin_page.html 第97 109行用 // for listAllAdmin.html 第85行用
	protected List<AdminVO> referenceListData(Model model) {
		List<AdminVO> list = adminSvc.getAll();
		return list;
	}

	// ========<< 導向listAllMeb >>=========//
	// 查詢所有 meb 資料
	@GetMapping("/listAllMeb")
	public String listAllMeb(Model model, HttpSession session) {
		SaveSession(model, session);
		// 檢查是否已經登入
		if (!isAdminLoggedIn(session)) {
			return "redirect:/back_end/toAdminLogin"; // 如果未登入，跳轉到登入頁面
		}
		// 查詢 meb 表格的所有資料
		List<MebVO> mebList = mebSvc.getAll(); // 查詢所有 Ps 資料
		// 將資料加入模型
		model.addAttribute("mebListData", mebList);
		// 返回顯示 meb 資料的頁面
		return "back_end/admin/listAllMeb"; // 假設該視圖已經存在並準備好顯示資料
	}
	
	// 查詢會員圖片資料
		@GetMapping("/getImgByMebId")
		public ResponseEntity<byte[]> getImgByMebId(@RequestParam("mebId") Integer mebId) {
			return adminSvc.findMebImgByPsId(mebId);
		}

	@PostMapping("/updateMebStatus")
	public String updateMebStatus(@RequestParam("mebId") String mebId, 
			@RequestParam("mebStatus") String mebStatus,RedirectAttributes redirectAttributes) {
	    try {
	        Integer mebIdInt = Integer.valueOf(mebId); // 手動轉換
	        // 更新會員狀態
	        adminSvc.updateMebStatus(mebIdInt, mebStatus);

	        // 如果會員被停權 (假設 0 表示停權)
	        if ("2".equals(mebStatus)) {
	            MebVO meb = mebSvc.getByMebId(mebIdInt);
	            adminSvc.sendSuspensionEmailMeb(meb); // 發送停權通知郵件
	            redirectAttributes.addFlashAttribute("message", "已發送停權通知郵件");
	        }
	    } catch (NumberFormatException e) {
	        // 處理錯誤，顯示無效的 ID
	        return "errorPage";
	    }
	    return "redirect:/admin/listAllMeb"; // 更新後重定向
	}

	// ========<< 導向listAllPsAndPg >>=========//
	// 查詢所有 ps 和 pg 資料
	@GetMapping("/listAllPsAndPg")
	public String listAllPsAndPg(Model model, HttpSession session) {
		SaveSession(model, session);
		// 檢查是否已經登入
		if (!isAdminLoggedIn(session)) {
			return "redirect:/back_end/toAdminLogin"; // 如果未登入，跳轉到登入頁面
		}

		// 查詢 ps 和 pg 表格的所有資料
		List<PsVO> psList = psSvc.getAll(); // 查詢所有 Ps 資料
		List<PgVO> pgList = pgSvc.getAll(); // 查詢所有 Pg 資料

		// 將資料加入模型
		model.addAttribute("psListData", psList);
		model.addAttribute("pgListData", pgList);

		// 返回顯示 ps 和 pg 資料的頁面
		return "back_end/admin/listAllPsAndPg"; // 假設該視圖已經存在並準備好顯示資料
	}

	// =======<< 美容師狀態更新 >> ========//
	@PostMapping("/updatePgStatus")
	public String updatePgStatus(@RequestParam("pgId") String pgId, 
			@RequestParam("pgStatus") String pgStatus,RedirectAttributes redirectAttributes) {
		try {
			Integer pgIdInt = Integer.valueOf(pgId); // 手動轉換
			 // 如果會員被停權 (假設 0 表示停權)
	        if ("2".equals(pgStatus)) {
	        	PgVO pg = pgSvc.getOnePg(pgIdInt);
	            adminSvc.sendSuspensionEmailPg(pg); // 發送停權通知郵件
	            redirectAttributes.addFlashAttribute("message", "已發送停權通知郵件");
	        }

			adminSvc.updatePgStatus(pgIdInt, pgStatus);
		} catch (NumberFormatException e) {
			// 處理錯誤，顯示無效的 ID
			return "errorPage";
		}
		return "redirect:/admin/listAllPsAndPg"; // 更新後重定向
	}

	// =======<< 保母狀態更新 >> ========//
	@PostMapping("/updatePsStatus")
	public String updatePsStatus(@RequestParam("psId") String psId, 
			@RequestParam("psStatus") String psStatus,RedirectAttributes redirectAttributes) {
		try {
			Integer psIdInt = Integer.valueOf(psId); // 手動轉換
			 if ("2".equals(psStatus)) {
		        	PsVO ps = psSvc.getOnePs(psIdInt);
		            adminSvc.sendSuspensionEmailPs(ps); // 發送停權通知郵件
		            redirectAttributes.addFlashAttribute("message", "已發送停權通知郵件");
		        }
			adminSvc.updatePsStatus(psIdInt, psStatus);
		} catch (NumberFormatException e) {
			// 處理錯誤，顯示無效的 ID
			return "errorPage";
		}
		return "redirect:/admin/listAllPsAndPg"; // 更新後重定向
	}

	// 查詢美容師圖片資料
	@CrossOrigin
	@GetMapping("/getPgLicenses")
	public ResponseEntity<byte[]> getLicenseByPgId(@RequestParam("pgId") Integer pgId) {
		return adminSvc.getLicenseByPgId(pgId);
	}

	// 查詢保母圖片資料
	@CrossOrigin
	@GetMapping("/getPsLicenses")
	public ResponseEntity<byte[]> getLicenseByPsId(@RequestParam("psId") Integer psId) {
		return adminSvc.getLicenseByPsId(psId);
	}

	// ========<< 導向美容師訂單撥款(listAllPgOrder) >>=========//

	// 查詢所有 pgOrder
	@GetMapping("/listAllPgOrder")
	public String listAllPgOrder(Model model, HttpSession session) {
		SaveSession(model, session);
		if (!isAdminLoggedIn(session)) {
			return "redirect:/back_end/toAdminLogin";
		}
		List<PgOrderVO> pgOrderList = pgOrderSvc.getAll();
		model.addAttribute("pgOrderListData", pgOrderList);
		return "back_end/admin/listAllPgOrder";
	}

	// =======<< 美容師訂單狀態更新 >> ========//
	@PostMapping("/updatePgOrderStatus")
	public String updatePgOrderStatus(@RequestParam("pgOrderId") String pgOrderId,
			@RequestParam("pgOrderStatus") String pgOrderStatus) {
		try {
			Integer pgOrderIdInt = Integer.valueOf(pgOrderId); // 手動轉換
			adminSvc.updatePgOrderStatus(pgOrderIdInt, pgOrderStatus);
		} catch (NumberFormatException e) {
			// 處理錯誤，顯示無效的 ID
			return "errorPage";
		}
		return "redirect:/admin/listAllPgOrder"; // 更新後重定向
	}

	// ========<< 導向保母訂單撥款(listAllPsOrder)>>=========//
	// 查詢所有 psOrder
	@GetMapping("/listAllPsOrder")
	public String listAllPsOrder(Model model, HttpSession session) {
		SaveSession(model, session);
		if (!isAdminLoggedIn(session)) {
			return "redirect:/back_end/toAdminLogin";
		}
		List<PsOrderVO> psOrderList = psOrderSvc.getAll();
		model.addAttribute("psOrderListData", psOrderList);
		return "back_end/admin/listAllPsOrder";
	}

	// =======<< 保母訂單撥款更新 >> ========//
	@PostMapping("/updatePsOrderStatus")
	public String updatePsOrderStatus(@RequestParam("psOrderId") String psOrderId,
			@RequestParam("psOrderStatus") String psOrderStatus) {
		try {
			Integer psOrderIdInt = Integer.valueOf(psOrderId); // 手動轉換
			adminSvc.updatePsOrderStatus(psOrderIdInt, psOrderStatus);
		} catch (NumberFormatException e) {
			// 處理錯誤，顯示無效的 ID
			return "errorPage";
		}
		return "redirect:/admin/listAllPsOrder"; // 更新後重定向
	}
     
}
