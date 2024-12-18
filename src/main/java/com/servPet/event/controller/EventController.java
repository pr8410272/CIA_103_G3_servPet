package com.servPet.event.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.servPet.admin.model.AdminVO;
import com.servPet.adminPer.model.AdminPerService;
import com.servPet.adminPer.model.AdminPerVO;
import com.servPet.event.model.EventService;
import com.servPet.event.model.EventVO;

@Controller
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;
    
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

    @GetMapping("/select")
    public String showSelectPage(Model model,HttpSession session) {
		SaveSession(model,session);
        model.addAttribute("events", eventService.getAllEvents());
        return "back_end/event/select_page";
    }

    @GetMapping("/display")
    public String displayEvent(@RequestParam("infid") Integer infid, Model model, 
    							RedirectAttributes redirectAttributes, HttpSession session) {
		SaveSession(model,session);
        return getEventOrRedirect(infid, model, redirectAttributes, "back_end/event/listOneEvent",session);
    }

    @GetMapping("/addPage")
    public String showAddEventPage(Model model,HttpSession session) {
		SaveSession(model,session);
        model.addAttribute("eventVO", new EventVO());
        return "back_end/event/addEvent";
    }

    @PostMapping("/add")
    public String addEvent(@Valid @ModelAttribute EventVO eventVO, BindingResult result, RedirectAttributes redirectAttributes,
    						Model model, HttpSession session) {
		SaveSession(model,session);
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMsg", "新增失敗：資料不完整");
            return "redirect:/event/addPage";
        }
        try {
            // 新增活動並返回完整的活動物件
            EventVO savedEvent = eventService.addEvent(eventVO);
            redirectAttributes.addFlashAttribute("successMsg", "活動新增成功");
            // 跳轉到顯示該活動詳情的頁面
            return "redirect:/event/display?infid=" + savedEvent.getInfId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "新增活動失敗：" + e.getMessage());
            return "redirect:/event/addPage";
        }
    }


    @GetMapping("/edit")
    public String editEvent(@RequestParam("infid") Integer infId, Model model, 
    						RedirectAttributes redirectAttributes, HttpSession session) {
		SaveSession(model,session);
        return getEventOrRedirect(infId, model, redirectAttributes, "back_end/event/updateEventInput",session);
    }

    @PostMapping("/update")
    public String updateEvent(@ModelAttribute EventVO eventVO, RedirectAttributes redirectAttributes,
							Model model, HttpSession session) {
    	SaveSession(model,session);
        return handleEventOperation(() -> eventService.updateEvent(
            eventVO.getInfId(), eventVO.getTitle(), eventVO.getContent(), eventVO.getInfType()), 
            "活動更新成功", "更新失敗", "redirect:/event/display?infid=" + eventVO.getInfId(), redirectAttributes);
    }

    @PostMapping("/unpublish")
    public String unpublishEvent(@RequestParam("infid") Integer infid, RedirectAttributes redirectAttributes,
					Model model, HttpSession session) {
		SaveSession(model,session);
        EventVO eventVO = eventService.getEventById(infid);
        if (eventVO == null || eventVO.getInfType() == 99) {
            redirectAttributes.addFlashAttribute("errorMsg", "活動已下架或不存在");
            return "redirect:/event/list";
        }
        return handleEventOperation(() -> eventService.unpublishEvent(infid), 
            "活動下架成功", "下架失敗", "redirect:/event/list", redirectAttributes);
    }

    @GetMapping("/list")
    public String listAllEvents(Model model, HttpSession session) {
    	SaveSession(model,session);
        model.addAttribute("events", eventService.getAllEvents());
        return "back_end/event/listAllEvent";
    }

    private String getEventOrRedirect(Integer infid, Model model, RedirectAttributes redirectAttributes, 
    									String successView, HttpSession session) {
		SaveSession(model,session);
        try {
            model.addAttribute("eventVO", eventService.getEventById(infid));
            return successView;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "操作失敗：" + e.getMessage());
            return "redirect:/event/select";
        }
    }

    private String handleEventOperation(Runnable operation, String successMsg, String errorMsg, 
                                        String redirectPath, RedirectAttributes redirectAttributes) {
        try {
            operation.run();
            redirectAttributes.addFlashAttribute("successMsg", successMsg);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", errorMsg + "：" + e.getMessage());
        }
        return redirectPath;
    }
}

