package com.servPet.psSvc.controller;

import com.servPet.psSvc.model.PsSvcService;
import com.servPet.psSvc.model.PsSvcVO;
import com.servPet.ps.model.PsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/psSvc")
public class PsSvcController {

    @Autowired
    private PsSvcService psSvcService;

//    // 顯示保母的服務項目列表頁面
//    @GetMapping("/psSvcList/{psId}")
//    public String listAllPsSvcItems(@PathVariable("psId") Integer psId, Model model) {
//        // 查詢所有服務項目來顯示於下拉式選單
//        List<PsSvcVO> list = psSvcService.getAll();
//        model.addAttribute("list", list);
//        model.addAttribute("psId", psId); // 保存保母的ID
//        return "back_end/psSvcEdit";
//    }


}
