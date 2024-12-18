package com.servPet.csIssue.model;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.servPet.meb.model.MebVO;

public interface CsIssueRepository extends JpaRepository<CsIssueVO, Integer> {

    // 分頁查詢指定狀態的記錄
    Page<CsIssueVO> findByQaSta(String qaSta, Pageable pageable);

    // 查詢指定狀態的所有記錄
    List<CsIssueVO> findByQaSta(String qaSta);

    // 查詢指定狀態和管理員 ID 的記錄
    @Query("SELECT c FROM CsIssueVO c WHERE c.qaSta = :status AND c.adminId.adminId = :adminId")
    List<CsIssueVO> findByStatusAndAdmin(@Param("status") String status, @Param("adminId") Integer adminId);

    // 多條件查詢
    @Query("""
            FROM CsIssueVO c
            WHERE (:status IS NULL OR c.qaSta = :status)
            AND (:adminId IS NULL OR c.adminId.adminId = :adminId)
            AND (:startDate IS NULL OR c.qaDate >= :startDate)
            AND (:endDate IS NULL OR c.qaDate <= :endDate)
            """)
     List<CsIssueVO> findByConditions(
             @Param("status") String status,
             @Param("adminId") Integer adminId,
             @Param("startDate") LocalDateTime startDate,
             @Param("endDate") LocalDateTime endDate);
    
    @Query("FROM CsIssueVO c WHERE c.mebId.mebId = :mebId")
    List<CsIssueVO> findByMember(@Param("mebId") Integer mebId);
    
    List<CsIssueVO> findByQaStaAndAdminId(String qaSta, Integer adminId);

    @Query("FROM CsIssueVO c ORDER BY c.qaSta ASC, c.mebId.mebId ASC")
    List<CsIssueVO> findAllOrderByStatusAndMemberId();

    @Query("FROM CsIssueVO c WHERE c.mebId.mebId = :mebId ORDER BY c.qaSta ASC")
    List<CsIssueVO> findByMemberAndOrderByStatus(@Param("mebId") Integer mebId);


    @Query("from CsIssueVO c where c.mebId = :member")
    List<CsIssueVO> getByMember(@Param("member") MebVO member);


}

