package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.servPet.admin.model.AdminService;
import com.servPet.admin.model.AdminVO;
import com.servPet.adminPer.model.AdminPerService;
import com.servPet.adminPer.model.AdminPerVO;
import com.servPet.fnc.model.FncService;
import com.servPet.fnc.model.FncVO;
import com.servPet.pg.model.PgService;
import com.servPet.ps.model.PsService;
import com.servPet.ps.model.PsVO;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

//@PropertySource("classpath:application.properties") // 於https://start.spring.io建立Spring Boot專案時, application.properties文件預設已經放在我們的src/main/resources 目錄中，它會被自動檢測到
@Controller
public class IndexController_inSpringBoot {
	// @Autowired (●自動裝配)(Spring ORM 課程)
	// 目前自動裝配了AdminService --> 供第66使用
	@Autowired
	AdminService adminSvc;
	@Autowired
	FncService fncSvc;
	@Autowired
	AdminPerService adminPerSvc;
	@Autowired
	PsService psSvc;
	@Autowired
	PgService pgSvc;
    
    @GetMapping("/back_end")
    public String index(Model model) {	
        return "back_end/index"; //view
    }
    
    @GetMapping("/back_end/index")
    public String indexBackEnd(HttpSession session, Model model) {
    	 // 從 session 中取得 adminVO 物件
        AdminVO admin = (AdminVO) session.getAttribute("adminVO");

        if (admin == null) {
        return "redirect:/back_end/login"; // 如果未登入，重定向到登入頁面
        
//        	return "back_end/index"; //奇葩免登入設定
        }else {
        	List<AdminPerVO> adminPer = adminPerSvc.getPerByAdminId(admin.getAdminId());
            
            // 只取出該管理員有權限的功能 ID
            Set<Integer> allowedFunctionIds = adminPer.stream()
                .map(adminPerVO -> adminPerVO.getFncVO().getFncId())
                .collect(Collectors.toSet());

            // 將功能 ID 傳遞給前端
            model.addAttribute("allowedFunctionIds", allowedFunctionIds);
        }

        // 如果 adminVO 存在，將其放入 model 以便 Thymeleaf 使用
        model.addAttribute("admin", admin); // 確保 "admin" 是添加到 Model 中的正確名稱
        return "back_end/index"; // 返回首頁
    }	
    
    @GetMapping("/back_end/psindex")
    public String psIndexBackEnd(HttpSession session, Model model) {
    	 // 從 session 中取得 psVO 物件
        PsVO ps = (PsVO) session.getAttribute("psVO");

        if (ps == null) {
            return "redirect:/back_end/toPsLogin"; // 如果未登入，重定向到登入頁面
        }

        // 如果 adminVO 存在，將其放入 model 以便 Thymeleaf 使用
        model.addAttribute("ps", ps); // 確保 "admin" 是添加到 Model 中的正確名稱

        return "back_end/psindex"; // 返回首頁
    }	
    
    @GetMapping("/test_page")
    public String testPage() {
        return "test_page";  // 預期返回 test_page.html
    }
    
 // 映射到 back_end/login 頁面
    @GetMapping("/back_end/login")
    public String showLoginPage() {
        return "back_end/login"; // 返回 templates/back_end/login.html
    }
}