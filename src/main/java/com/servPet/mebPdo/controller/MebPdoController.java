package com.servPet.mebPdo.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;
import com.servPet.mebPdo.model.MebPdoService;
import com.servPet.pdo.model.PdoVO;
import com.servPet.pdoItem.model.PdoItemVO;

@Controller
@RequestMapping("/mebPdo")
public class MebPdoController {

    @Autowired
    private MebPdoService mebPdoService;

    @Autowired
    private MebService mebService;
    
    // 取得會員所有訂單
    @GetMapping("/mebPdo")
    public String getAllOrders(Model model,Principal principal) {
        String email = principal.getName();
        Optional<MebVO> memberOptional = mebService.findMemberByEmail(email);
        MebVO mebVO = memberOptional.get();
        Integer mebId = mebVO.getMebId();
        System.out.println("收到的 mebId: " + mebId); // 輸出收到的 mebId
        if (mebId == null) {
            throw new RuntimeException("mebId 不能為空");
        }
        List<PdoVO> mebPdo = mebPdoService.getAllOrdersByMebId(mebId);
        model.addAttribute("mebPdo", mebPdo);
        return "front_end/mebpdo/mebPdo"; // 對應到 templates/front_end/mebPdo/mebpdo.html
    }
    
    // 會員編號 TEST
//    @GetMapping("/mebPdo")
//    public String getAllOrders(Model model) {
//        Integer mebId = 2011; // 將這裡替換為資料庫中已存在的會員編號
//        List<PdoVO> mebPdo = mebPdoService.getAllOrdersByMebId(mebId);
//        model.addAttribute("mebPdo", mebPdo);
//        return "front_end/mebpdo/mebPdo"; // 返回對應的 Thymeleaf 頁面
//    }

    // 查看單一訂單詳情
    @GetMapping("/mebPdo/{pdoId}")
    public String getOrderDetails(Model model, @PathVariable Integer pdoId) {
        PdoVO oneMebpdo = mebPdoService.getOrderById(pdoId);                 // 從 MebPdoService 取得訂單資料
        List<PdoItemVO> pdoItems = mebPdoService.getPdoItemsByPdoId(pdoId);  // 查詢商品訂單明細
        model.addAttribute("oneMebpdo", oneMebpdo);                          // 將訂單資料傳到 Thymeleaf
        model.addAttribute("pdoItems", pdoItems);
        return "front_end/mebpdo/listOneMebPdo";                            // 返回對應的頁面顯示訂單明細
    }

//    // 取消訂單
//    @PostMapping("/{pdoId}/cancel")
//    @ResponseBody
//    public ResponseEntity<String> cancelOrder(@PathVariable Integer pdoId) {
//        System.out.println("收到取消訂單請求，訂單ID: " + pdoId);
//        try {
//            mebPdoService.cancelOrder(pdoId);
//            return ResponseEntity.ok("訂單已取消，付款狀態已更新為已退款");
//        } catch (Exception e) {
//            System.out.println("取消訂單失敗: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("取消訂單失敗，請重新操作一次！");
//        }
//    }
    
    // 取消訂單並金儲值金退回錢包
    @PostMapping("/{pdoId}/cancel")
    @ResponseBody
    public ResponseEntity<String> cancelOrder(@PathVariable Integer pdoId) {
        System.out.println("收到取消訂單請求，訂單ID: " + pdoId);
        try {
            mebPdoService.cancelOrder(pdoId);
            return ResponseEntity.ok("訂單已取消，金額已退回錢包");
        } catch (Exception e) {
            System.out.println("取消訂單失敗: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("取消訂單失敗，請重新操作一次！");
        }
    }
    
//    // 更新配送地址
//    @PostMapping("/{pdoId}/updateAddress")
//    @ResponseBody
//    public ResponseEntity<String> updateShippingAddress(
//            @PathVariable Integer pdoId,
//            @RequestParam("newAddress") String newAddress) {
//        try {
//            mebPdoService.updateShippingAddress(pdoId, newAddress);
//            return ResponseEntity.ok("配送地址更新成功");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新配送地址失敗，請重新操作一次！");
//        }
//    }
    
//    @Controller
//    @RequestMapping("/login")
//    public class LoginController {
//
//        @Autowired
//        private MebService mebService;
//
//        @PostMapping
//        public String login(@RequestParam String email, 
//                            @RequestParam String password, 
//                            HttpSession session) {
//            try {
//                MebVO member = mebService.login(email, password); // 呼叫登入方法
//                session.setAttribute("mebId", member.getMebId()); // 設定 mebId 到 session
//                return "redirect:/mebPdo/mebPdo"; // 登入成功後導向商城訂單頁面
//            } catch (IllegalArgumentException e) {
//                System.out.println("登入失敗：" + e.getMessage());
//                return "redirect:/login?error=true"; // 返回登入頁面並提示錯誤
//            }
//        }
//    }

}
