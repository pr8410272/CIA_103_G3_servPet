package com.servPet.vtr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VtrPageController {

    @GetMapping("/vtr_filter")
    public String showFilterPage() {
        return "vtr_filter"; // 返回 Thymeleaf 模板名稱
    }
}
