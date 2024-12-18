package com.servPet.vtr.controller;

import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;
import com.servPet.vtr.model.VtrService;
import com.servPet.vtr.model.VtrVO;

@Controller
@RequestMapping("/vtr")
public class VtrController {

    @Autowired
    private VtrService vtrService;

    @Autowired
    private MebService mebService;
    
    @GetMapping("/create")
    public String showRechargePage(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                   Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/front_end/login";
        }

        String email = principal.getName();
        Optional<MebVO> memberOpt = mebService.findMemberByEmail(email);

        if (memberOpt.isPresent()) {
            MebVO member = memberOpt.get();
            model.addAttribute("member", member);
            model.addAttribute("mebId", member.getMebId());

            // 計算餘額
            Integer totalBalance = vtrService.calculateTotalBalance(member.getMebId());
            model.addAttribute("totalBalance", totalBalance);

            // 處理會員頭像
            if (member.getMebImg() != null && member.getMebImg().length > 0) {
                String base64Image = Base64.getEncoder().encodeToString(member.getMebImg());
                model.addAttribute("mebImg", base64Image);
            } else {
                model.addAttribute("mebImg", null);
            }

            // 使用分頁獲取交易紀錄
            Pageable pageable = PageRequest.of(page - 1, size); // PageRequest 頁碼從0開始
            Page<VtrVO> transactionPage = vtrService.getTransactionsByMemberIdWithPagination(member.getMebId(), pageable);

            model.addAttribute("transactions", transactionPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", transactionPage.getTotalPages());
        } else {
            return "redirect:/front_end/login";
        }

        return "front_end/vtrcreate";
    }

    @PostMapping("/create")
    public String createTransaction(
            @RequestParam Integer mebId,
            @RequestParam String money,
            @RequestParam String transactionType,
            RedirectAttributes redirectAttributes) {
        try {
            int parsedMoney = Integer.parseInt(money);

            if (parsedMoney < 100) {
                redirectAttributes.addFlashAttribute("error", "儲值金額必須至少為 100！");
                return "redirect:/vtr/create";
            }

            vtrService.createTransaction(mebId, parsedMoney, transactionType);
            redirectAttributes.addFlashAttribute("success", "儲值成功！");
            return "redirect:/vtr/create";

        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "儲值金額只能輸入數字！");
            return "redirect:/vtr/create";
        }
    }
    
    
    @GetMapping("/records/{mebId}")
    public String showTransactionRecords(
            @PathVariable Integer mebId,
            Model model,
            Principal principal) {
        if (principal == null) {
            return "redirect:/front_end/login";
        }

        // 驗證當前用戶的權限
        String email = principal.getName();
        Optional<MebVO> memberOpt = mebService.findMemberByEmail(email);
        if (memberOpt.isEmpty() || !memberOpt.get().getMebId().equals(mebId)) {
            return "redirect:/front_end/login";
        }

        // 獲取該會員的交易紀錄
        List<VtrVO> transactions = vtrService.getTransactionsByMemberId(mebId);

        // 將數據傳遞到前端
        model.addAttribute("transactions", transactions);
        return "front_end/vtrrecords"; // 渲染交易紀錄的 Thymeleaf 頁面
    }


    @GetMapping("/member/{mebId}")
    public List<VtrVO> getTransactionsByMemberId(@PathVariable Integer mebId) {
        return vtrService.getTransactionsByMemberId(mebId);
    }

    @PutMapping("/update/{vtrId}")
    public VtrVO updateTransaction(
            @PathVariable Integer vtrId,
            @RequestParam Integer money,
            @RequestParam String transactionType) {
        return vtrService.updateTransaction(vtrId, money, transactionType);
    }

    @DeleteMapping("/delete/{vtrId}")
    public String deleteTransaction(@PathVariable Integer vtrId) {
        vtrService.deleteTransaction(vtrId);
        return "Transaction deleted successfully!";
    }
}
