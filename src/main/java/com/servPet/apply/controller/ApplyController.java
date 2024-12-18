package com.servPet.apply.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.servPet.admin.model.AdminVO;
import com.servPet.adminPer.model.AdminPerService;
import com.servPet.adminPer.model.AdminPerVO;
import com.servPet.apply.model.ApplyResultDTO;
import com.servPet.apply.model.ApplyService;
import com.servPet.apply.model.ApplyVO;
import com.servPet.meb.model.MebRepository;
import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;

@Controller
@RequestMapping("/apply")
public class ApplyController {

    @Autowired
    private ApplyService applyService;
    @Autowired
    private MebService memberService;

    @Autowired
    private MebRepository memberRepository;
    
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

    // 測試用會員 ID
//    private static final Integer TEST_MEMBER_ID = 2002;
    
    /**
     * 顯示申請列表
     */
    @GetMapping("/listApply")
    public String listApplications(Model model,HttpSession session) {
    	SaveSession(model,session);
        List<ApplyVO> applications = applyService.getAllApplicationsSortedByIdDesc();
        model.addAttribute("applications", applications);
        return "back_end/apply/listApply";
    }

    /**
     * 顯示申請表單頁面
     */
    @GetMapping("/create")
    public String createApplyForm(Model model, Principal principal,HttpSession session) {
    	SaveSession(model,session);
        // 從 Principal 獲取用戶 email
        String email = principal.getName();
        Optional<MebVO> memberOptional = memberService.findMemberByEmail(email);

        if (memberOptional.isPresent()) {
            model.addAttribute("member", memberOptional.get()); // 添加會員信息
        } else {
            model.addAttribute("errorMessage", "會員信息未找到");
            return "error-page"; // 返回錯誤頁面
        }

        // 初始化 applyVO 並添加到模型
        ApplyVO applyVO = new ApplyVO();
        model.addAttribute("applyVO", applyVO);

        return "front_end/apply/create";
    }
    
    
    /**
     * 處理申請表單提交
     */
    @PostMapping("/create")
    public String createApply(@Valid @ModelAttribute("applyVO") ApplyVO applyVO,
                              BindingResult result,
                              @RequestParam("applyPicFile") MultipartFile applyPicFile,
                              RedirectAttributes redirectAttributes,
                              Model model ,HttpSession session) {
    	SaveSession(model,session);
        try {
            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("errorMessage", "表單驗證失敗！");
                return "redirect:/apply/create";
            }

            // 設置證件照
            if (!applyPicFile.isEmpty()) {
                applyVO.setApplyPic(applyPicFile.getBytes());
            } else {
                throw new IllegalArgumentException("必須上傳證件照！");
            }

            // 保存申請
            applyService.saveApply(applyVO);

            redirectAttributes.addFlashAttribute("message", "申請成功！");
            return "redirect:/apply/list";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "文件上傳失敗！");
            return "redirect:/apply/create";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/apply/create";
        }
    }
    
   
    @GetMapping("/list")
    public String listMemberApply(Model model, Principal principal,HttpSession session) {
    	SaveSession(model,session);
        // 獲取當前用戶的 email
        String email = principal.getName();

        // 查找會員
        MebVO member = memberRepository.findByMebMail(email)
                .orElseThrow(() -> new RuntimeException("找不到會員，Email: " + email));

        List<ApplyVO> apply = applyService.getByMember(member);
        model.addAttribute("apply", apply);
        model.addAttribute("member", member); // 傳遞會員信息供前端顯示

        return "front_end/apply/listMemberApply"; // 前端頁面路徑
    }

   

    /**
     * 顯示申請詳細資訊
     */
    @GetMapping("view/{id}")
    public String viewApplyById(@PathVariable("id") Integer applyId, Model model,HttpSession session) {
    	SaveSession(model,session);
        ApplyVO apply = applyService.getApplyById(applyId);
        if (apply == null) {
            throw new RuntimeException("找不到申請，ID: " + applyId);
        }
        model.addAttribute("apply", apply);
        return "front_end/apply/view";
    }
    /**
     * 顯示申請詳細資訊
     */
    @GetMapping("/{id}")
    public String getApplyById(@PathVariable("id") Integer applyId, Model model,HttpSession session) {
    	SaveSession(model,session);
    	ApplyVO apply = applyService.getApplyById(applyId);
    	if (apply == null) {
    		throw new RuntimeException("找不到申請，ID: " + applyId);
    	}
    	model.addAttribute("apply", apply);
    	return "back_end/apply/detail";
    }
   

    /**
     * 提供證件照圖片
     */
    @GetMapping("/image/{id}/license")
    @ResponseBody
    public ResponseEntity<byte[]> getLicenseImage(@PathVariable("id") Integer applyId,
    											  Model model,HttpSession session) {
    	SaveSession(model,session);
        ApplyVO apply = applyService.getApplyById(applyId);
        if (apply == null || apply.getApplyPic() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // 或 IMAGE_PNG，根據實際圖片格式
        return new ResponseEntity<>(apply.getApplyPic(), headers, HttpStatus.OK);
    }
    
    
   

    // 通過申請
    @PostMapping("/approve/{id}")
    public String approveApplication(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes,
    												Model model,HttpSession session) {
    	SaveSession(model,session);
        try {
            applyService.updateApplyStatus(id, "1", null);
            redirectAttributes.addFlashAttribute("successMessage", "申請已通過！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "操作失敗：" + e.getMessage());
        }
        return "redirect:/apply/listApply";
    }
    
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .authorizeRequests()
//                .antMatchers("/apply/reject/**", "/apply/approve/**").permitAll() // 测试环境放行
//                .anyRequest().authenticated()
//            .and()
//            .csrf().disable(); // 禁用 CSRF
//    }


    
    

 // 駁回申請
    @PostMapping("/reject/{id}")
    public String rejectApplication(@PathVariable("id") Integer id,
                                     @RequestParam("rejectReason") String rejectReason,
                                     RedirectAttributes redirectAttributes,
										Model model,HttpSession session) {
    	SaveSession(model,session);
    	System.out.println("進入駁回方法，ID: " + id);

        try {
            if (rejectReason == null || rejectReason.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "駁回原因不能為空！");
                return "redirect:/apply/listApply" ;

            }System.out.println(rejectReason);
            
          
            applyService.updateApplyStatus(id, "2", rejectReason);
            redirectAttributes.addFlashAttribute("successMessage", "申請已駁回！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "操作失敗：" + e.getMessage());
        }
        return "redirect:/apply/listApply";
    }






    /**
     * 處理申請
     */
    @PostMapping("/process")
    public ResponseEntity<ApplyResultDTO<Void>> processApplication(@RequestBody ApplyVO applyVO) {
        try {
            applyService.processApplication(applyVO);
            return ResponseEntity.ok(ApplyResultDTO.success(null, "申請已處理成功"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApplyResultDTO.failure("處理申請時發生錯誤：" + e.getMessage()));
        }
    }

    
}
