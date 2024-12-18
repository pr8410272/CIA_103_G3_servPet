package com.servPet.vtr.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VtrRepository extends JpaRepository<VtrVO, Integer> {
    List<VtrVO> findByMebId(Integer mebId);
    Page<VtrVO> findByMebId(Integer mebId, Pageable pageable);
    @Query("SELECT v FROM VtrVO v WHERE " +
            "(:mebId IS NULL OR v.mebId = :mebId) AND " +
            "(:transactionType IS NULL OR v.transactionType = :transactionType) AND " +
            "(:startDate IS NULL OR v.createTime >= :startDate) AND " +
            "(:endDate IS NULL OR v.createTime <= :endDate)")
     List<VtrVO> findRecordsByFilters(@Param("mebId") Integer mebId,
                                      @Param("transactionType") String transactionType,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);
}
