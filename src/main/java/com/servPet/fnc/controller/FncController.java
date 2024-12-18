package com.servPet.fnc.controller;

import com.servPet.fnc.model.FncVO;
import com.servPet.admin.model.AdminVO;
import com.servPet.adminPer.model.AdminPerService;
import com.servPet.adminPer.model.AdminPerVO;
import com.servPet.fnc.model.FncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/fnc")
public class FncController {

    @Autowired
    private FncService fncSvc;
    
    @Autowired
    private AdminPerService adminPerSvc;

	// 公共方法來檢查 admin 是否存在於 session 中
	private boolean isAdminLoggedIn(HttpSession session) {
	    return session.getAttribute("adminVO") != null;
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
			
		  // 顯示新增功能頁面
	    @GetMapping("/select_fnc_page")
	    public String select_fnc_page(ModelMap model,HttpSession session) {
	    	SaveSession(model,session);
	        model.addAttribute("adminVO", new AdminVO());
	        return "back_end/fnc/select_fnc_page"; 
	    }
		    
    // 顯示新增功能頁面
    @GetMapping("addFnc")
    public String addFnc(ModelMap model,HttpSession session) {
    	SaveSession(model,session);
        model.addAttribute("fncVO", new FncVO());
        return "back_end/fnc/addFnc"; // 頁面名稱為 addFnc.html	
    }

 @PostMapping("insert")
 	public String insert(@Valid FncVO fncVO, BindingResult result, ModelMap model,HttpSession session) {
	 SaveSession(model,session);
     // 如果驗證錯誤，返回新增頁面
     if (result.hasErrors()) {
         return "back_end/fnc/addFnc";
     }
     
     // 設定創建時間與更新時間為當前時間
     LocalDateTime currentTime = LocalDateTime.now();
     fncVO.setFncCreAt(currentTime);  // 設定創建時間
     fncVO.setFncUpdAt(currentTime);  // 設定更新時間
     
     // 新增功能資料
     fncSvc.addFnc(fncVO);
     
     model.addAttribute("success", "- (新增成功)");
     return "redirect:/fnc/listAllFnc";  // 新增後重導至功能列表頁面
 }


 @PostMapping("getOne_For_Display")
 public String getOneForDisplay(@RequestParam("fncId") String fncIdStr, ModelMap model, HttpSession session) {
     // 保存session資訊
     SaveSession(model, session);

     // 檢查 fncId 是否為空
     if (fncIdStr == null || fncIdStr.trim().isEmpty()) {
         model.addAttribute("errorMessage", "請提供功能編號");
         return "back_end/fnc/select_fnc_page";  // 返回功能列表頁面
     }

     // 檢查 fncId 是否為有效的數字
     if (!fncIdStr.matches("\\d+")) {
         model.addAttribute("errorMessage", "功能編號必須是數字");
         return "back_end/fnc/select_fnc_page";  // 返回功能列表頁面
     }

     // 將 fncIdStr 轉換為 Integer
     Integer fncId = Integer.parseInt(fncIdStr);

     // 查詢功能資料
     FncVO fncVO = fncSvc.getOneFnc(fncId);

     // 檢查是否找到對應的功能資料
     if (fncVO == null) {
         model.addAttribute("errorMessage", "無此功能資料");
         return "back_end/fnc/select_fnc_page";  // 無此功能則返回功能列表頁面
     }

     // 將查詢到的功能資料放入模型中，供頁面顯示
     model.addAttribute("fncVO", fncVO);
     
     // 返回顯示功能資料頁面
     return "back_end/fnc/listOneFnc";  
 }

    // 查詢所有功能資料
    @PostMapping("listAllFnc_ByCompositeQuery")
    public String listAllFnc(HttpServletRequest req,Model model,HttpSession session) {
    	SaveSession(model,session);
    	Map<String, String[]> map = req.getParameterMap();
        List<FncVO> list = fncSvc.getAll(map);  // 查詢所有功能資料
        model.addAttribute("fncList", list);
        return "back_end/fnc/listAllFnc";  // 顯示所有功能
    }

    // 顯示更新功能頁面
    @PostMapping("getOne_For_Update")
    public String getOneForUpdate(@RequestParam("fncId") String fncId, ModelMap model,HttpSession session) {
    	SaveSession(model,session);
        FncVO fncVO = fncSvc.getOneFnc(Integer.valueOf(fncId));
        model.addAttribute("fncVO", fncVO);
        return "back_end/fnc/update_fnc_input"; // 返回更新功能頁面
    }

    // 更新功能資料
    @PostMapping("update")
    public String update(@Validated FncVO fncVO, BindingResult result, ModelMap model,HttpSession session) {
    	SaveSession(model,session);
        if (result.hasErrors()) {
            return "back_end/fnc/updateFnc";
        }

        // 更新時，設定更新時間為當前時間
        fncVO.setFncUpdAt(LocalDateTime.now());  // 設定更新時間
        
        // 更新功能資料
        fncSvc.updateFnc(fncVO);
        
        model.addAttribute("success", "- (修改成功)");
        model.addAttribute("fncVO", fncVO);
        return "back_end/fnc/listOneFnc";  // 更新後轉到顯示功能資料頁面
    }


    // 複合查詢功能
    @PostMapping("listFncs_ByCompositeQuery")
    public String listFncByCompositeQuery(HttpServletRequest req, Model model,HttpSession session) {
    	SaveSession(model,session);
        Map<String, String[]> map = req.getParameterMap();
        List<FncVO> fncList = fncSvc.getAll(map);  // 使用複合查詢過濾功能資料
        model.addAttribute("fncList", fncList);
        return "back_end/fnc/listAllFnc";  // 顯示過濾後的功能列表
    }
    
    @GetMapping("/listAllFnc")
  	public String listAllFnc(Model model,HttpSession session) {
    	SaveSession(model,session);
  		return "back_end/fnc/listAllFnc";
  	}
      
      @ModelAttribute("fncListData")  // for select_fnc_page.html 第97 109行用 // for listAllFnc.html 第85行用
  	protected List<FncVO> referenceListDataFnc(Model model) {
  		
      	List<FncVO> list = fncSvc.getAll();
  		return list;
  	}
}