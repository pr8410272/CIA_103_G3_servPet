package com.servPet.event.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.servPet.admin.model.AdminVO;
import com.servPet.event.model.EventService;
import com.servPet.event.model.EventVO;

@Controller
@RequestMapping("/member/event")
public class MemberEventController { @Autowired
    private EventService eventService;

//公共方法來檢查 admin 是否存在於 session 中
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
		   return "redirect:/back_end/login"; // 如果未登入，跳轉到登入頁面
		  }
		  AdminVO admin = (AdminVO) session.getAttribute("adminVO");
		  model.addAttribute("adminVO", new AdminVO());
		  model.addAttribute("admin", admin);
		  return null; // 若只處理 session 和 model，這裡可以返回 null 或其它
		 }
	
	
	// ===<< 將admin存於 session 中一起送出 >>===/
	 public String SaveSession(ModelMap model, HttpSession session) {
	  if (!isAdminLoggedIn(session)) {
	   return "redirect:/back_end/login"; // 如果未登入，跳轉到登入頁面
	  }
	  AdminVO admin = (AdminVO) session.getAttribute("adminVO");
	  model.addAttribute("adminVO", new AdminVO());
	  model.addAttribute("admin", admin);
	  return null; // 若只處理 session 和 model，這裡可以返回 null 或其它
	 }

	@GetMapping("/")
	public String show() {
	  return "front_end/event/index";
	}

    @GetMapping("/list")
    public String listAllEvents(Model model,HttpSession session) {
		SaveSession(model,session);
        model.addAttribute("events", eventService.getAllEvents());
        return "front_end/event/listAllEvent";
    }

    @GetMapping("/display")
    public String displayEvent(@RequestParam("infid") Integer infId, Model model,HttpSession session) {
		SaveSession(model,session);
        EventVO eventVO = eventService.getEventById(infId);
        model.addAttribute("eventVO", eventVO);
        return "front_end/event/displayEvent";
    }
}