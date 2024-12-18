package com.servPet.psOrder.controller;

import com.servPet.meb.model.MebVO;
import com.servPet.pet.model.PetVO;
import com.servPet.ps.model.PsService;
import com.servPet.ps.model.PsVO;
import com.servPet.psOrder.model.PsOrderService;
import com.servPet.psOrder.model.PsOrderVO;
import com.servPet.psSvcItem.model.PsSvcItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/psOrder")
public class PsOrderController {


    @Autowired
    private PsOrderService psOrderService;
    @Autowired
    private PsService psService;

    @GetMapping("list")
    public String listAllPsOrder(Model model) {
        List<PsOrderVO> psOrderList = psOrderService.getAll();
        model.addAttribute("psOrderList", psOrderList);
        return "listPsOrder";
    }

    // 列出特定保母的訂單
    @GetMapping("/psOrderManagement/{psId}")
    public String listOrdersByPsId(@PathVariable("psId") Integer psId,
                                   @ModelAttribute PsSvcItemVO psSvcItemVO
                                   , Model model) {
        // 根據 psId 獲取保母對象
        PsVO psVO = psService.findById(psId);
//        if (psVO == null) {
//            model.addAttribute("errorMessage", "保母不存在");
//            return "errorPage"; // 返回錯誤頁面
//        }

        // 根據保母對象查詢訂單列表
        List<PsOrderVO> orders = psOrderService.getOrdersByPsId(psId);
        model.addAttribute("orders", orders);
        model.addAttribute("psVO", psVO);
        model.addAttribute("psSvcItemVO", psSvcItemVO);

        return "back_end/psOrderManagement"; // 返回前端頁面
    }






}
