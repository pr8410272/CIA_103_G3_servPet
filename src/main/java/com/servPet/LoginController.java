package com.servPet;

import com.servPet.admin.model.AdminService;
import com.servPet.admin.model.AdminVO;
import com.servPet.pg.model.PgService;
import com.servPet.pg.model.PgVO;
import com.servPet.ps.model.PsService;
import com.servPet.ps.model.PsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/back_end") // 確保路徑匹配
public class LoginController {

    @Autowired
    private AdminService adminSvc;
    
    @Autowired
    private PsService psSvc;
    
    @Autowired
    private PgService pgSvc;

 // 顯示登入頁面
    @GetMapping("/toAdminLogin")
    public String loginAdminPage(Model model) {
        model.addAttribute("adminVO", new AdminVO());
        return "redirect:/back_end/login";  // 返回登入頁面
    }

    @GetMapping("/toPgLogin")
    public String loginPgPage(Model model) {
        model.addAttribute("pgVO", new PgVO());
        return "redirect:/back_end/login";  // 返回登入頁面
    }

	    @GetMapping("/toPsLogin")
	    public String loginPsPage(Model model) {
	        model.addAttribute("psVO", new PsVO());
	        return "redirect:/back_end/login";  // 返回登入頁面
	    }

    @PostMapping("/adminLogin")
    public String login(@ModelAttribute AdminVO adminVO, HttpSession session, Model model) {
        AdminVO admin = adminSvc.login(adminVO.getAdminAcc(), adminVO.getAdminPwd());
        
        if(admin == null) {
        	 model.addAttribute("errorMessage", "輸入錯誤，請確認您輸入的帳號密碼");
        	 return "back_end/login";
        } else {
            if (admin.getAdminAccStatus() == null) {
                model.addAttribute("errorMessage", "您的帳號狀態未註明，請聯絡平台管理員。");
                return "back_end/login";
            }else if(admin.getAdminAccStatus().equals("0")) {
            	model.addAttribute("errorMessage", "您的帳號被停用，請聯絡平台管理員。");
            	return "back_end/login";
            }else {
            	session.setAttribute("adminVO", admin); // 登入成功，保存用戶信息到 session
                return "redirect:/back_end/index";
            }
            
        }
    }
    
    @PostMapping("/pgLogin")
    public String login(@ModelAttribute PgVO pgVO, HttpSession session, Model model) {
    	
    	if (pgVO.getPgPw() == null || pgVO.getPgPw().isEmpty()) {
            model.addAttribute("errorMessage", "密碼不可為空");
            return "back_end/toPgLogin";  // 返回登入頁面
        }
    	
    	PgVO pg = pgSvc.login(pgVO.getPgId(), pgVO.getPgPw());

        if (pg != null) {
        	
            if (pg.getPgStatus() == null) {
                model.addAttribute("errorMessage", "您的帳號狀態未知，請聯絡管理員。");
                return "back_end/login"; // 返回登入頁面，顯示錯誤訊息
            }
            
            if (pg.getPgStatus().equals("2")) {  // 假設 0 表示停權狀態
                model.addAttribute("errorMessage", "您的帳號已停用，請聯絡管理員。");
                return "back_end/login"; // 返回登入頁面，顯示停用訊息
            }
            session.setAttribute("pgVO", pg); // 登入成功，保存用戶信息到 session
            System.out.println("pgVO: " + session.getAttribute("pgVO"));
            return "redirect:/pg/listOnePg_back?pgId=" +pg.getPgId(); // 重定向到首頁
        } else {
            model.addAttribute("errorMessage", "信箱或密碼錯誤，請重新嘗試。"); // 登入失敗
            return "back_end/login"; // 返回登入頁面
        }		
    }
    
    
    @PostMapping("/psLogin")
    public String login(@ModelAttribute PsVO psVO, HttpSession session, Model model) {
    	
    	if (psVO.getPsPw() == null || psVO.getPsPw().isEmpty()) {
            model.addAttribute("errorMessage", "密碼不可為空");
            return "back_end/toPsLogin";  // 返回登入頁面
        }
    	
    	PsVO ps = psSvc.login(psVO.getPsId(), psVO.getPsPw());
    	
        if (ps.getPsStatus() == null) {
            model.addAttribute("errorMessage", "您的帳號狀態未知，請聯絡管理員。");
            return "back_end/login"; // 返回登入頁面，顯示錯誤訊息
        }
        
        if (ps != null) {
            if (ps.getPsStatus().equals("2")) {  // 假設 0 表示停用狀態
                model.addAttribute("errorMessage", "您的帳號已停用，請聯絡管理員。");
                return "back_end/login"; // 返回登入頁面，顯示停用訊息
            }
            session.setAttribute("psVO", ps); // 登入成功，保存用戶信息到 session
            return "redirect:/ps/profile/"+ps.getPsId(); // 重定向到首頁
        } else {
            model.addAttribute("errorMessage", "帳號或密碼錯誤，請重新嘗試。"); // 登入失敗
            return "back_end/login"; // 返回登入頁面
        }
    }
    	@GetMapping("/pg/listOnePg")
    public String pgIndexBackEnd(HttpSession session, Model model) {
        // 從 session 中取得 PgVO 物件
        PgVO pg = (PgVO) session.getAttribute("pgVO");

        if (pg == null) {
            return "redirect:/back_end/login"; // 如果未登入，重定向到登入頁面
        }

        // 如果 PgVO 存在，將其放入 model 以便 Thymeleaf 使用
        model.addAttribute("pgVO", pg); // 確保將 pgVO 加入 model
        return "back_end/pg/listOnePg"; // 返回模板
    }

    // 處理登出請求
    @GetMapping("/logout")
    public String logout(HttpSession session) {	
        session.invalidate(); // 注銷用戶
        return "redirect:/back_end/login"; // 重定向到登入頁面
    }
    
    
}
