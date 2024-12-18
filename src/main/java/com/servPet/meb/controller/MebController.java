package com.servPet.meb.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.servPet.meb.model.EmailService;
import com.servPet.meb.model.MebDTO;
import com.servPet.meb.model.MebRepository;
import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;
import com.servPet.meb.model.PasswordResetService;

@Controller
//@RequestMapping("/front_end")
public class MebController {
	
	
	@Autowired
	private final MebService memberService;
	
    @Autowired
    private EmailService emailService;
	
    @Autowired
    private PasswordResetService passwordResetService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private MebRepository mebRepository;
    
    private static final Logger log = LoggerFactory.getLogger(MebController.class);
	
    public MebController(MebService memberService) {
		this.memberService = memberService;
	}
//註冊
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("member", new MebVO());
		return "front_end/register";
	}

	@PostMapping("/register")
	public String registerMember(
	        @ModelAttribute MebDTO memberDTO,
	        Model model) {
	    try {
	        MebVO member = new MebVO(); // 將 DTO 轉換為 VO

	        // 設置其他字段
	        member.setMebName(memberDTO.getMebName());
	        member.setMebPwd(memberDTO.getMebPwd());
	        member.setMebMail(memberDTO.getMebMail());
	        member.setMebPhone(memberDTO.getMebPhone());
	        member.setMebAddress(memberDTO.getMebAddress());
	        member.setMebSex(memberDTO.getMebSex());

	        // 處理圖片
	        MultipartFile file = memberDTO.getMebImg();
	        if (file != null && !file.isEmpty()) {
	            log.info("Received file: {}", file.getOriginalFilename());
	            log.info("File size: {}", file.getSize());
	            log.info("File content type: {}", file.getContentType());

	            if (file.getSize() > 2 * 1024 * 1024) {
	                model.addAttribute("errorMessage", "圖片大小不能超過 2MB！");
	                return "front_end/register";
	            }
	            if (!file.getContentType().startsWith("image/")) {
	                model.addAttribute("errorMessage", "請上傳有效的圖片文件！");
	                return "front_end/register";
	            }

	            member.setMebImg(file.getBytes());
	        } else {
	            member.setMebImg(null); // 未上傳圖片
	        }

	        // 保存會員資料
	        memberService.registerMember(member);
	        log.info("Member registration successful");
	    } catch (IOException e) {
	        model.addAttribute("errorMessage", "圖片處理失敗，請稍後再試！");
	        log.error("Error processing image", e);
	        return "front_end/register";
	    }

	    return "redirect:/front_end/login";
	}

	@GetMapping("/")
	public String showIndex(Model model, Principal principal) {
	    if (principal == null) {
	        // 未登入，用於顯示未登入狀態
	        model.addAttribute("isLoggedIn", false);
	        return "index";
	    }

	    try {
	        String email = principal.getName();
	        Optional<MebVO> member = memberService.findMemberByEmail(email);

	        if (member.isPresent()) {
	            MebVO mebVO = member.get();
	            model.addAttribute("isLoggedIn", true);
	            model.addAttribute("member", mebVO);

	            // Base64 格式的圖片處理
	            if (mebVO.getMebImg() != null && mebVO.getMebImg().length > 0) {
	                String base64Image = Base64.getEncoder().encodeToString(mebVO.getMebImg());
	                model.addAttribute("mebImg", base64Image);
	                log.info("會員圖片已加載，圖片大小: {} bytes", mebVO.getMebImg().length);
	            } else {
	                model.addAttribute("mebImg", null);
	                log.info("會員無圖片，使用預設圖片");
	            }
	        } else {
	            model.addAttribute("isLoggedIn", false);
	            model.addAttribute("error", "未找到相關會員資料");
	            log.warn("未找到會員資料，電子郵件: {}", email);
	        }
	    } catch (Exception e) {
	        // 記錄錯誤日誌
	        log.error("會員資料加載失敗", e);
	        model.addAttribute("error", "系統錯誤，請稍後再試");
	    }

	    return "index";
	}


	
	@GetMapping("/profile")
	public String showProfile(Model model, Principal principal) {
	    if (principal == null) {
	        return "redirect:/front_end/login";
	    }

	    String email = principal.getName();
	    Optional<MebVO> memberOpt = memberService.findMemberByEmail(email);

	    if (memberOpt.isPresent()) {
	        MebVO member = memberOpt.get();
	        model.addAttribute("member", member);

	        if (member.getMebImg() != null && member.getMebImg().length > 0) {
	            try {
	                String base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(member.getMebImg());
	                model.addAttribute("mebImg", base64Image);
	            } catch (Exception e) {
	                System.err.println("圖片編碼錯誤：" + e.getMessage());
	                model.addAttribute("mebImg", "/images/defaultavatar.png");
	            }
	        } else {
	            model.addAttribute("mebImg", "/images/defaultavatar.png");
	        }
	    } else {
	        model.addAttribute("member", new MebVO());
	        model.addAttribute("mebImg", "/images/defaultavatar.png");
	        model.addAttribute("errorMessage", "未找到會員資料");
	    }

	    return "front_end/profile";
	}




	@PostMapping("/profile/update")
	public String updateProfile(
	    @ModelAttribute MebDTO memberDTO,
	    Model model
	) {
	    try {
	        // 查找現有會員資料
	        MebVO existingMember = memberService.findMemberByEmail(memberDTO.getMebMail())
	                .orElseThrow(() -> new IllegalArgumentException("會員不存在，無法更新"));

	        // 處理圖片文件
	        MultipartFile file = memberDTO.getMebImg();
	        if (file != null && !file.isEmpty()) {
	            if (file.getSize() > 2 * 1024 * 1024) {
	                model.addAttribute("errorMessage", "圖片大小不能超過 2MB！");
	                return "front_end/profile";
	            }
	            if (!file.getContentType().startsWith("image/")) {
	                model.addAttribute("errorMessage", "請上傳圖片文件！");
	                return "front_end/profile";
	            }
	            existingMember.setMebImg(file.getBytes());
	        }

	        // 更新其他字段
	        existingMember.setMebName(memberDTO.getMebName());
	        existingMember.setMebAddress(memberDTO.getMebAddress());
	        existingMember.setMebPhone(memberDTO.getMebPhone());

	        // 保存更新後的會員資料
	        memberService.updateMember(existingMember);
	    } catch (IOException e) {
	        model.addAttribute("errorMessage", "圖片處理失敗，請稍後再試！");
	        return "front_end/profile";
	    } catch (IllegalArgumentException e) {
	        model.addAttribute("errorMessage", e.getMessage());
	        return "front_end/profile";
	    }

	    return "redirect:/profile";
	}





	@GetMapping("/front_end/login")
	public String login(HttpServletRequest request, Model model) {
	    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
	    model.addAttribute("_csrf", csrfToken);
	    return "front_end/login";
	}
	
	@PostMapping("/test-register")
	public String testRegister() {
	    MebVO testUser = new MebVO();
	    testUser.setMebMail("test@gmail.com");
	    testUser.setMebPwd(passwordEncoder.encode("12345")); // 加密密碼
	    mebRepository.save(testUser);
	    return "Test user created";
	}
	
	
    // 顯示忘記密碼頁面
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "front_end/forgot-password";
    }

    // 處理忘記密碼請求
//    @PostMapping("front_end/forgot-password")
//    public String forgotPassword(@RequestParam String mebMail, Model model) {
//        Optional<MebVO> member = memberService.findMemberByEmail(mebMail);
//
//        if (member.isPresent()) {
//            String token = UUID.randomUUID().toString(); // 生成唯一的 Token
//            passwordResetService.saveResetToken(member.get().getId(), token); // 儲存 Token 到資料庫
//            
//            String resetLink = "http://yourwebsite.com/reset-password?token=" + token;
//            String emailContent = "<p>您好，請點擊以下連結重設密碼：</p><a href='" + resetLink + "'>" + resetLink + "</a>";
//            
//            emailService.sendPasswordResetEmail(mebMail, "重置密碼", emailContent);
//            model.addAttribute("message", "密碼提示已發送至您的電子郵件！");
//        } else {
//            model.addAttribute("error", "該電子郵件未註冊！");
//        }
//        return "front_end/forgot-password";
//    }
    
    
    // 處理忘記密碼請求
    @PostMapping("front_end/forgot-password")
    public String forgotPassword(@RequestParam String mebMail, Model model) {
        Optional<MebVO> member = memberService.findMemberByEmail(mebMail);

        if (member.isPresent()) {
            String token = UUID.randomUUID().toString();
            passwordResetService.saveResetToken(member.get().getMebId(), token);

            String resetLink = "http://localhost:8080/front_end/reset-password?token=" + token;
            String emailContent = "<p>您好，</p><p>請點擊以下連結重設密碼：</p>" +
                                  "<a href='" + resetLink + "'>" + resetLink + "</a>";
            emailService.sendPasswordResetEmail(mebMail, "重置密碼", emailContent);
            model.addAttribute("message", "密碼提示已發送至您的電子郵件！");
        } else {
            model.addAttribute("error", "該電子郵件未註冊！");
        }
        return "front_end/forgot-password";
    }
    
    @GetMapping("front_end/reset-password")
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token); // 將 token 傳遞到前端
        return "front_end/reset-password"; // 返回 Thymeleaf 的模板名稱
    }
    
    @PostMapping("front_end/reset-password")
    public String resetPassword(@RequestParam String token, 
                                @RequestParam String newPassword, 
                                Model model) {
        log.info("開始處理密碼重置請求，收到的 Token: {}", token);

        Integer userId = passwordResetService.getUserIdByToken(token);
        if (userId == null) {
            log.warn("Token 無效或已過期，Token: {}", token);
            model.addAttribute("error", "無效的重置連結或連結已過期！");
            return "front_end/reset-password";
        }

        Optional<MebVO> optionalMember = mebRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            log.error("會員資料不存在，會員 ID: {}", userId);
            model.addAttribute("error", "會員資料不存在，無法重置密碼！");
            return "front_end/reset-password";
        }

        MebVO member = optionalMember.get();

        try {
            String encodedPassword = passwordEncoder.encode(newPassword);
            member.setMebPwd(encodedPassword);
            mebRepository.save(member);
            passwordResetService.removeToken(token);

            log.info("密碼重置成功，會員 ID: {}", member.getMebId());
            model.addAttribute("success", "密碼重置成功！");
            return "front_end/reset-password"; // 返回當前頁面，讓前端處理跳轉
        } catch (Exception e) {
            log.error("密碼加密或保存失敗，原因: {}", e.getMessage(), e);
            model.addAttribute("error", "系統錯誤，請稍後再試！");
            return "front_end/reset-password";
        }
    }









    // 檢查電子郵件是否已被使用
    @PostMapping("front_end/checkEmail")
    @ResponseBody
    public Map<String, Boolean> checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        boolean exists = memberService.findMemberByEmail(email).isPresent();
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return response;
    }
	
}