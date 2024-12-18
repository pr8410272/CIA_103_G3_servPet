package com.servPet.vtr.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.servPet.vtr.model.VtrManagerService;
import com.servPet.vtr.model.VtrVO;

@RestController
@RequestMapping("/vtr")
public class VtrManagerController {

    @Autowired
    private VtrManagerService service;

    @GetMapping("/filter")
    public Page<VtrVO> filterRecords(
            @RequestParam(required = false) Integer mebId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) Integer minMoney,
            @RequestParam(required = false) Integer maxMoney,
            @RequestParam(required = false) String transactionType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        System.out.println("Transaction Type: " + transactionType); // 添加調試輸出
        LocalDateTime startDateTime = (startTime != null && !startTime.isEmpty()) ? LocalDateTime.parse(startTime) : null;
        LocalDateTime endDateTime = (endTime != null && !endTime.isEmpty()) ? LocalDateTime.parse(endTime) : null;

        return service.filterRecords(mebId, startDateTime, endDateTime, minMoney, maxMoney, transactionType, PageRequest.of(page, size));
    }
}
