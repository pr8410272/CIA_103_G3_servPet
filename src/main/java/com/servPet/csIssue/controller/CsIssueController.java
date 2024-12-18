package com.servPet.csIssue.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.servPet.admin.model.AdminVO;
import com.servPet.adminPer.model.AdminPerService;
import com.servPet.adminPer.model.AdminPerVO;
import com.servPet.csIssue.model.CsIssueService;
import com.servPet.csIssue.model.CsIssueVO;

@RequestMapping("/cs-issues")
@Controller
public class CsIssueController {

	@Autowired
	private CsIssueService csIssueService;
	
	@Autowired
	private TemplateEngine templateEngine; // 注入 Thymeleaf 的 TemplateEngine

	@Autowired
	private JavaMailSender mailSender; // 注入 Spring 的 JavaMailSender
	
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
	
	
	
	
	
	

//    @GetMapping("/")
//    public String show() {
//        return "index";
//    }
	


	

	@GetMapping("/list")
	public String listAllCsIssues(Model model,HttpSession session) {
		SaveSession(model,session);
	    // 獲取所有的客服表單數據，並按狀態和會員ID排序
	    List<CsIssueVO> issues = csIssueService.getAllOrderedByStatusAndMemberId();
	    
	    // 添加數據到模型
	    model.addAttribute("issues", issues);
	    
	    // 返回Thymeleaf模板名稱
	    return "back_end/cs_issues/listCsIssues";
	}



	@GetMapping("/view/{id}")
	public String viewCsIssue(@PathVariable Integer id, Model model,HttpSession session) {
		SaveSession(model,session);
		
//		 System.out.println("接收到的問題 ID：" + id);
		
		CsIssueVO csIssueVO = csIssueService.getOneCsIssue(id);
		model.addAttribute("csIssueVO", csIssueVO);
		return "back_end/cs_issues/viewCsIssue"; // Thymeleaf 模板名稱
	}

	@GetMapping("/edit/{id}")
	public String showEditCsIssuePage(@PathVariable Integer id, Model model,HttpSession session) {
		SaveSession(model,session);
		CsIssueVO csIssueVO = csIssueService.getOneCsIssue(id);
		model.addAttribute("csIssueVO", csIssueVO);
		return "back_end/cs_issues/editCsIssue"; // Thymeleaf 模板名稱
	}



	 @PostMapping("/reply")
	    public String replyCsIssue(
	            @RequestParam Integer csQaId,
	            @RequestParam String reContent,
	            Model model,HttpSession session) {
			SaveSession(model,session);

	        try {
	            // 獲取問題
	            CsIssueVO csIssue = csIssueService.getOneCsIssue(csQaId);

	            // 獲取當前管理員 ID
	            Integer adminId = 1001; // 替換為從 session 獲取的 ID
	            AdminVO admin = csIssueService.getAdminById(adminId);

	            // 更新問題信息
	            csIssue.setAdminId(admin);
	            csIssue.setReContent(reContent);
	            csIssue.setReDate(LocalDateTime.now());
	            csIssue.setQaSta("1"); // 設置狀態為已回覆

	            // 保存更新
	            csIssueService.addCsIssue(csIssue);

	            // 發送郵件通知
	            sendReplyEmail(csIssue);

	            model.addAttribute("successMessage", "問題已成功回覆並通知用戶");

	            // 重導向至詳細資料頁面
	            return "redirect:back_end/cs-issues/view/" + csQaId;
	        } catch (RuntimeException e) {
	            model.addAttribute("errorMessage", "回覆失敗：" + e.getMessage());
	            return "redirect:back_end/cs-issues/list";
	        }
	    }

	    
	 private void sendReplyEmail(CsIssueVO csIssue) {
		    try {
		        // 格式化回覆日期
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		        String formattedDate = csIssue.getReDate() != null ? csIssue.getReDate().format(formatter) : "尚未回覆";

		        // Thymeleaf 上下文
		        Context context = new Context();
		        context.setVariable("memberId", csIssue.getMebId().getMebId());
		        context.setVariable("questionContent", csIssue.getQaContent());
		        context.setVariable("replyContent", csIssue.getReContent());
		        context.setVariable("replyDate", formattedDate); // 使用格式化后的时间

		        // 判斷使用的模板
		        String templatePath = "back_end/cs_issues/emailReply"; // 你的 Thymeleaf 模板路徑
		        String mailContent = templateEngine.process(templatePath, context);

		        // 組裝郵件
		        MimeMessage message = mailSender.createMimeMessage();
		        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		        // 收件人 - 從會員信息中獲取郵箱
		        String recipientEmail = csIssue.getMebId().getMebMail(); // 使用 getMebMail() 方法
		        helper.setTo(recipientEmail);

		        // 標題
		        helper.setSubject("您的客服問題已回覆");

		        // 內容
		        helper.setText(mailContent, true);

		        // 發送郵件
		        mailSender.send(message);

		    } catch (MessagingException e) {
		        e.printStackTrace();
		        throw new RuntimeException("郵件發送失敗：" + e.getMessage());
		    }
		}

	 
	

	@GetMapping("/addReply/{id}")
	public String showAddReplyPage(@PathVariable Integer id, Model model,HttpSession session) {
		SaveSession(model,session);
		CsIssueVO csIssueVO = csIssueService.getOneCsIssue(id);
		model.addAttribute("csIssueVO", csIssueVO);

//		System.out.println("獲取的問題內容：" + csIssueVO);

		return "back_end/cs_issues/addReply"; // 回覆表單頁面模板
	}

}
