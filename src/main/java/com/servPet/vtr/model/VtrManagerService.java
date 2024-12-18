package com.servPet.vtr.model;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VtrManagerService {

    @Autowired
    private VtrManagerRepository repository;

    public Page<VtrVO> filterRecords(Integer mebId, LocalDateTime startTime, LocalDateTime endTime, Integer minMoney, Integer maxMoney, String transactionType, Pageable pageable) {
        return repository.findByCriteria(mebId, startTime, endTime, minMoney, maxMoney, transactionType, pageable);
    }
}
