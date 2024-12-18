package com.servPet.psSvcItem.controller;

import com.servPet.psSvc.model.PsSvcVO;
import com.servPet.psSvcItem.model.PsSvcItemService;
import com.servPet.psSvcItem.model.PsSvcItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/psSvcItem")
public class PsSvcItemController {

    @Autowired
    private PsSvcItemService psSvcItemService;


//    @PostMapping("/update")
//    public String getOneForUpdate(@RequestParam("svcId") Integer svcId, Model model) {
//        PsSvcItemVO psSvcItemVO = psSvcItemService.getPsSvcItemById(svcId);
//        model.addAttribute("psSvcItemVO", psSvcItemVO);
//        return "back_end/update_psSvcItem_input";
//    }

    @PostMapping("/update")
    @ResponseBody // 使用 @ResponseBody 讓方法返回 JSON
    public PsSvcItemVO getOneForUpdate(@RequestParam("svcId") Integer svcId) {
        return psSvcItemService.getPsSvcItemById(svcId);
    }


    @PostMapping("/delete")
    public String deletePsSvcItem(@RequestParam("svcId") Integer svcId) {
        psSvcItemService.deletePsSvcItem(svcId);
        return "redirect:/psSvcItem/psSvcItemlist";
    }

    @PostMapping("/insert/{psId}")
    public String insertPsSvcItem(@RequestParam("psId") Integer psId,@ModelAttribute("PsSvcVO") List<PsSvcVO> psSvcVOList, Model model){
        System.out.println("psSvcVOList = " + psSvcVOList);

        return "redirect:/ps/profile/" + psId;
    }



}

