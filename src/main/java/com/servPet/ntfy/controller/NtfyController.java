package com.servPet.ntfy.controller;

import com.servPet.meb.model.MebService;
import com.servPet.ntfy.model.NtfyRepository;
import com.servPet.ntfy.model.NtfyService;
import com.servPet.ntfy.model.NtfyVO;
import com.servPet.admin.model.AdminService;
import com.servPet.admin.model.AdminVO;
import com.servPet.adminPer.model.AdminPerService;
import com.servPet.adminPer.model.AdminPerVO;
import com.servPet.event.model.EventVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/ntfy")
public class NtfyController {

    @Autowired
    private NtfyService ntfyService;
    
    @Autowired
    private MebService mebService;
    
    @Autowired
    private NtfyRepository ntfyRepository;
    
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
 	
 	
    // 會員查看所有通知和公告通知
    @GetMapping("/member")
    public String getMemberNotificationsAndEvents(
            @RequestParam(value = "type", defaultValue = "notification") String type, 
            Model model, 
            Principal principal) {
        String mebMail = principal.getName(); // 從 Principal 中取得 mebMail
        Integer mebId = mebService.getMebIdByEmail(mebMail); // 根據 mebMail 查詢 mebId

        if ("event".equals(type)) {
            // 获取公告通知
            List<EventVO> events = ntfyService.getEventAll(mebId);
            model.addAttribute("events", events);
            return "front_end/member-events"; // 这个页面展示公告
        } else {
            // 获取普通通知
            List<NtfyVO> notifications = ntfyService.getAllNotificationsForMember(mebId);
            model.addAttribute("notifications", notifications);
            return "front_end/member-notifications"; // 这个页面展示普通通知
        }
    }

    // 會員查看未讀通知
    @GetMapping("/member/unread")
    public String getMemberUnreadNotifications(Model model, Principal principal,HttpSession session) {
    	SaveSession(model, session);
        String mebMail = principal.getName(); // 從 Principal 中取得 mebMail
        Integer mebId = mebService.getMebIdByEmail(mebMail); // 根據 mebMail 查詢 mebId

        List<NtfyVO> notifications = ntfyService.getUnreadNotificationsForMember(mebId);
        model.addAttribute("notifications", notifications);
        model.addAttribute("mebId", mebId);
        return "front_end/member-notifications";
    }

    // 標記通知為已讀
    @PostMapping("/member/read/{ntfyMgmtId}")
    @ResponseBody
    public ResponseEntity<?> markAsRead(@PathVariable Integer ntfyMgmtId, Principal principal,Model model,HttpSession session) {
    	SaveSession(model, session);
        try {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 驗證用戶是否登入
            }

            String mebMail = principal.getName(); // 從 Principal 中取得用戶 email
            Integer mebId = mebService.getMebIdByEmail(mebMail); // 根據 email 查詢用戶 ID

            if (mebId == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 用戶未授權
            }

            ntfyService.markNotificationAsRead(ntfyMgmtId); // 更新資料庫狀態
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    // 一鍵已讀
    @PostMapping("/member/markAllAsRead")
    public String markAllAsRead(RedirectAttributes redirectAttributes, Principal principal,Model model,HttpSession session) {
    	SaveSession(model, session);
        String mebMail = principal.getName(); // 從 Principal 中取得 mebMail
        Integer mebId = mebService.getMebIdByEmail(mebMail); // 根據 mebMail 查詢 mebId

        ntfyService.updateAllUnreadToRead(mebId);
        
        redirectAttributes.addFlashAttribute("message", "所有通知已標記為已讀");
        return "redirect:/ntfy/member";
    }

    // 後台相關操作保留不變
    @GetMapping("/admin")
    public String getAllNotifications(Model model,HttpSession session) {
    	SaveSession(model, session);
        List<NtfyVO> allNotifications = ntfyService.getAllNotifications();
        model.addAttribute("notifications", allNotifications);
        model.addAttribute("newNotification", new NtfyVO());
        return "back_end/admin-notifications";
    }

    @PostMapping("/admin/create")
    public String createNotification(@Valid @ModelAttribute("newNotification") NtfyVO ntfy,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes,Model model,HttpSession session) {
    	SaveSession(model, session);
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println("驗證錯誤：" + error.getDefaultMessage()));
            redirectAttributes.addFlashAttribute("error", "新增通知失敗，請檢查輸入資料。");
            return "redirect:/ntfy/admin";
        }

        try {
            if (ntfy.getDate() == null) {
                ntfy.setDate(Timestamp.valueOf(LocalDateTime.now()));
            }

            ntfyService.createNotification(ntfy);
            redirectAttributes.addFlashAttribute("success", "通知新增成功！");
        } catch (Exception e) {
            System.err.println("新增通知失敗：" + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "請新增現有的會員編號 (例如:2000)");
        }
        return "redirect:/ntfy/admin";
    }

    @GetMapping("/admin/edit/{ntfyMgmtId}")
    public String editNotificationForm(@PathVariable Integer ntfyMgmtId, Model model,HttpSession session) {
    	SaveSession(model, session);
        NtfyVO ntfy = ntfyService.getNotificationById(ntfyMgmtId);
        if (ntfy == null) {
            return "redirect:/ntfy/admin";
        }
        model.addAttribute("notification", ntfy);
        return "back_end/edit-notification";
    }

    @PostMapping("/admin/update")
    public String updateNotification(@Valid @ModelAttribute NtfyVO ntfy, BindingResult result, Model model,HttpSession session) {
    	SaveSession(model, session);
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println("驗證錯誤：" + error.getDefaultMessage()));
            return "back_end/edit-notification";
        }
        ntfyService.updateNotification(ntfy);
        return "redirect:/ntfy/admin";
    }

    @PostMapping("/admin/delete/{ntfyMgmtId}")
    public String deleteNotification(@PathVariable Integer ntfyMgmtId,
                                     RedirectAttributes redirectAttributes, Model model,HttpSession session) {
    	SaveSession(model, session);
        try {
            ntfyService.deleteNotification(ntfyMgmtId);
            redirectAttributes.addFlashAttribute("success", "通知刪除成功！");
        } catch (Exception e) {
            System.err.println("刪除通知失敗：" + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "刪除通知時發生錯誤：" + e.getMessage());
        }
        return "redirect:/ntfy/admin";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                try {
                    LocalDateTime dateTime = LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    setValue(Timestamp.valueOf(dateTime));
                } catch (DateTimeParseException e) {
                    setValue(null);
                }
            }
        });
    }
}
