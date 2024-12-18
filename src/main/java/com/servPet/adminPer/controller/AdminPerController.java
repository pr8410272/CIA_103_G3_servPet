package com.servPet.adminPer.controller;

import com.servPet.adminPer.model.AdminPerService;
import com.servPet.adminPer.model.AdminPerVO;
import com.servPet.admin.model.AdminService;
import com.servPet.admin.model.AdminVO;
import com.servPet.fnc.model.FncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/adminPer")
public class AdminPerController {

	@Autowired
	private AdminPerService adminPerSvc;

	@Autowired
	private AdminService adminSvc; // 假設你有一個服務來管理Admin

	@Autowired
	private FncService fncService; // 假設你有一個服務來管理Fnc

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
		Set<Integer> allowedFunctionIds = adminPer.stream().map(adminPerVO -> adminPerVO.getFncVO().getFncId())
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
		Set<Integer> allowedFunctionIds = adminPer.stream().map(adminPerVO -> adminPerVO.getFncVO().getFncId())
				.collect(Collectors.toSet());
		model.addAttribute("allowedFunctionIds", allowedFunctionIds);

		return null;
	}

	@GetMapping("select_adminPer_page")
	public String select_adminPer_page(Model model, HttpSession session) {
		// 確保Session中的管理員資料正確
		SaveSession(model, session);
		// 從服務層獲取所有管理員資料
		List<AdminVO> adminListData = adminSvc.getAll(); // 假設您有這樣一個方法
		// 將 adminListData 添加到模型中
		model.addAttribute("adminListData", adminListData);
		// 返回模板的相對路徑
		return "back_end/adminPer/select_adminPer_page";
	}

	@PostMapping("listOneAdminPer")
	public String listAdminPer(@RequestParam(value = "adminId", required = false) String adminId,
	                            @RequestParam(value = "adminName", required = false) String adminName,
	                            Model model, HttpSession session) {
	    SaveSession(model, session);
	    
	    // 創建查詢條件 Map
	    Map<String, String[]> queryParams = new HashMap<>();
	    
	    // 檢查 adminId 是否有效且為數字
	    if (adminId != null && !adminId.isEmpty()) {
	        // 如果 adminId 不是數字，則顯示錯誤並返回
	        if (!adminId.matches("\\d+")) {
	            model.addAttribute("errorMessage", "請輸入有效的管理員編號");
	            List<AdminVO> adminListData = adminSvc.getAll();
	    		model.addAttribute("adminListData", adminListData);
	            return "back_end/adminPer/select_adminPer_page"; // 返回權限類別列表頁面
	        } else {
	            // 查詢 adminId 是否存在於資料庫中
	            AdminVO admin = adminSvc.getOneAdmin(Integer.valueOf(adminId)); // 假設 adminSvc.getAdminById() 用來查詢 adminId 是否存在
	            if (admin == null) {
	                // 如果找不到該管理員，顯示查無資料的訊息
	                model.addAttribute("errorMessage", "未找到符合條件的管理員資料");
	                List<AdminVO> adminListData = adminSvc.getAll();
	        		model.addAttribute("adminListData", adminListData);
	                return "back_end/adminPer/select_adminPer_page"; // 返回權限類別列表頁面
	            }
	            // 如果 adminId 存在，加入查詢條件
	            queryParams.put("adminId", new String[] { adminId });
	        }
	    }

	    // 檢查 adminName 是否有效
	    if (adminName != null && !adminName.isEmpty()) {
	        queryParams.put("adminName", new String[] { adminName });
	    }

	    // 如果 adminId 和 adminName 都為空，顯示錯誤訊息並返回
	    if ((adminName == null || adminName.isEmpty()) && (adminId == null || adminId.isEmpty())) {
	        model.addAttribute("errorMessage", "請輸入管理員編號");
            List<AdminVO> adminListData = adminSvc.getAll();
    		model.addAttribute("adminListData", adminListData);
	        return "back_end/adminPer/select_adminPer_page"; // 返回權限類別列表頁面
	    }

	    // 根據查詢條件獲取指定管理員的權限資料
	    List<AdminPerVO> list = adminPerSvc.getAll(queryParams);
	    
	    // 如果查無資料，添加提示信息
	    if (list == null || list.isEmpty()) {
	        model.addAttribute("message", "未找到符合條件的管理員權限資料。");
            List<AdminVO> adminListData = adminSvc.getAll();
    		model.addAttribute("adminListData", adminListData);
	        return "back_end/adminPer/select_adminPer_page";
	    } else {
	        // 將查詢結果添加到模型中
	        model.addAttribute("adminPerListData", list);
	    }
	    
	    // 返回顯示指定管理員權限的頁面
	    return "redirect:/adminPer/listAllAdminPer";
	}




	// 透過複合查詢列出管理員權限
	// 這裡確保 listAdminPerByCompositeQuery 正確獲取資料
	@PostMapping("listAdminPer_ByCompositeQuery")
	public String listAdminPerByCompositeQuery(HttpServletRequest req, Model model, HttpSession session) {
		SaveSession(model, session);
		Map<String, String[]> map = req.getParameterMap();
		List<AdminPerVO> adminPerList = adminPerSvc.getAll(map); // 獲取所有符合條件的資料
		model.addAttribute("adminPerListData", adminPerList); // 將結果添加到模型中
		return "back_end/adminPer/listAllAdminPer"; // 返回對應的頁面
	}

	// 透過列出所有管理員權限
	@GetMapping("/listAllAdminPer")
	public String listAllAdminPer(Model model, HttpSession session) {
		SaveSession(model, session);
		return "back_end/adminPer/listAllAdminPer";
	}

	// 顯示給管理員指派權限的頁面
	@GetMapping("addAdminPer")
	public String addAdminPer(ModelMap model, HttpSession session) {
		SaveSession(model, session);
		model.addAttribute("adminPerVO", new AdminPerVO());
		model.addAttribute("adminListData", adminSvc.getAll()); // Assuming you have a method to get all admins
		model.addAttribute("fncList", fncService.getAll()); // Assuming you have a method to get all functions
		return "back_end/adminPer/addAdminPer";
	}

	// 新增某個管理員的功能權限
	@PostMapping("insert")
	public String insert(@Validated AdminPerVO adminPerVO, BindingResult result, ModelMap model, HttpSession session) {
		SaveSession(model, session);
		// 如果表單驗證有錯誤，返回錯誤訊息
		if (result.hasErrors()) {
			model.addAttribute("errorMessage", "Please correct the errors in the form.");
			return "back_end/adminPer/addAdminPer";
		}

		// 新增 AdminPer
		adminPerSvc.addAdminPer(adminPerVO);

		// 顯示成功訊息並返回列表
		model.addAttribute("success", "- (Permission Added Successfully)");
		return "redirect:/adminPer/listAllAdminPer"; // 重定向到管理員權限列表頁面
	}

	// 顯示特定管理員權限的更新頁面
	@PostMapping("getOne_For_Update")
	public String getOneForUpdate(@RequestParam("adminPerId") Integer adminPerId, ModelMap model, HttpSession session) {
		SaveSession(model, session);
		AdminPerVO adminPerVO = adminPerSvc.getOneAdminPer(adminPerId);
		if (adminPerVO == null) {
			model.addAttribute("errorMessage", "No such permission record found.");
			return "back_end/adminPer/listAllAdminPer";
		}
		model.addAttribute("adminPerVO", adminPerVO);
		model.addAttribute("adminListData", adminSvc.getAll());
		model.addAttribute("fncList", fncService.getAll());
		return "back_end/adminPer/updateAdminPer";
	}

	// 更新管理員權限
	@PostMapping("update")
	public String update(@Validated AdminPerVO adminPerVO, BindingResult result, ModelMap model, HttpSession session) {
		SaveSession(model, session);
		if (result.hasErrors()) {
			model.addAttribute("errorMessage", "Please correct the errors in the form.");
			return "back_end/adminPer/updateAdminPer";
		}

		// 更新管理員權限
		adminPerSvc.updateAdminPer(adminPerVO);

		// 顯示成功訊息並返回列表
		model.addAttribute("success", "- (Permission Updated Successfully)");
		return "redirect:/adminPer/listAllAdminPer"; // Redirect to the list of admin permissions
	}

	// 刪除特定的admin權限
//    @PostMapping("delete")
//    public String delete(@RequestParam("adminPerId") Integer adminPerId, ModelMap model, HttpSession session) {
//    	SaveSession(model,session);
//        adminPerSvc.deleteAdminPer(adminPerId);
//        model.addAttribute("success", "- (Permission Deleted Successfully)");
//        return "redirect:/adminPer/listAllAdminPer";  // Redirect to the list of admin permissions
//    }

	@ModelAttribute("adminPerListData") // for select_adminPer_page.html 第97 109行用 // for listAllAdminPer.html 第85行用
	protected List<AdminPerVO> referenceListDataAdminPer(Model model, HttpSession session) {
		SaveSession(model, session);
		List<AdminPerVO> list = adminPerSvc.getAll();
		return list;
	}
}
