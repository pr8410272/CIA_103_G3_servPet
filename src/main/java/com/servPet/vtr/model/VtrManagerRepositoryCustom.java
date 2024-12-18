package com.servPet.vtr.model;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VtrManagerRepositoryCustom {
    Page<VtrVO> findByCriteria(Integer mebId, LocalDateTime startTime, LocalDateTime endTime, Integer minMoney, Integer maxMoney, String transactionType, Pageable pageable);
}

