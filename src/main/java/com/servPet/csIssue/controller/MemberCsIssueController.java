package com.servPet.csIssue.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.servPet.csIssue.model.CsIssueService;
import com.servPet.csIssue.model.CsIssueVO;
import com.servPet.meb.model.MebRepository;
import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;

@RequestMapping("/member/cs-issues")
@Controller
public class MemberCsIssueController {

 @Autowired
 private CsIssueService csIssueService;

 @Autowired
 private MebRepository memberRepository; // 使用 MemberRepository 直接操作會員數據

 @Autowired
 private MebService memberService;

 // 假會員 ID（測試用）
// private static final Integer TEST_MEMBER_ID = 2002;

 /**
  * 新增問題頁面
  */
//    @GetMapping("/add")
//    public String showAddIssuePage(Model model) {
//        // 從資料庫查詢假會員
//        MebVO simulatedMember = memberRepository.findById(TEST_MEMBER_ID)
//                .orElseThrow(() -> new RuntimeException("找不到會員 ID: " + TEST_MEMBER_ID));
//
//        // 將會員信息傳遞到前端
//        model.addAttribute("member", simulatedMember);
//
//        return "front_end/cs_issues/addCsIssue"; // 返回新增頁面模板名稱
//    }
 @GetMapping("/add")
 public String showProfile(Model model, Principal principal) {
  String email = principal.getName();
  Optional<MebVO> memberOptional = memberService.findMemberByEmail(email);

  if (memberOptional.isPresent()) {
   model.addAttribute("member", memberOptional.get());
  } else {
   model.addAttribute("errorMessage", "會員信息未找到");
   return "error-page"; // 返回錯誤頁面
  }

  return "front_end/cs_issues/addCsIssue";
 }

 @PostMapping("/insert")
 public String insertIssue(Model model, Principal principal, CsIssueVO csIssueVO) {
  String memberEmail = principal.getName();
  Optional<MebVO> optionalMember = memberRepository.findByMebMail(memberEmail);

  if (optionalMember.isPresent()) {
   MebVO member = optionalMember.get();
   model.addAttribute("member", member);

   // 傳遞整個 MebVO 給 CsIssueVO 的 setMebId 方法
   csIssueVO.setMebId(member);
   csIssueService.addCsIssue(csIssueVO);

   return "redirect:/submission-complete"; // 成功後重定向
  } else {
   model.addAttribute("errorMessage", "會員信息未找到");
   return "error-page"; // 返回錯誤頁面
  }
 }

 @GetMapping("/submission-complete")
 public String showSubmissionCompletePage() {
  return "front_end/cs_issues/submissionComplete"; // 跳转页面模板路径
 }

 /**
  * 顯示當前會員的問題列表
  */
 @GetMapping("/list")
 public String listMemberIssues(Model model, Principal principal) {
  // 從資料庫查詢假會員
  String email = principal.getName();
  Optional<MebVO> memberOptional = memberService.findMemberByEmail(email);

  if (memberOptional.isPresent()) {
   model.addAttribute("member", memberOptional.get());
  } else {
   model.addAttribute("errorMessage", "會員信息未找到");
   return "error-page"; // 返回錯誤頁面
  }

  MebVO member = memberOptional.get(); // 從 Optional 提取會員物件
  List<CsIssueVO> issues = csIssueService.getByMember(member);
  model.addAttribute("issues", issues);
  model.addAttribute("member", member); // 傳遞會員信息供前端顯示

  return "front_end/cs_issues/listMemberIssues"; // 返回列表頁模板名稱
 }
}