package com.servPet.apply.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.servPet.meb.model.MebVO;


public interface ApplyRepository extends JpaRepository <ApplyVO, Integer> {
	
	// 根據會員 ID 查詢申請
    List<ApplyVO> findByMebId(MebVO mebId);
   
    @Query("from ApplyVO c where c.mebId = :member")
    // 新增根據會員 ID 查詢的方法
    List<ApplyVO> getByMember(@Param("member") MebVO member);



    // 根據申請狀態查詢
    Page<ApplyVO> findByApplyStatus(String applyStatus, Pageable pageable);
	
    
    // 根據申請時間查詢
    List<ApplyVO> findByMebIdOrderByApplyTimeDesc(Integer mebId);

    
    // 根據多個狀態篩選申請
    Page<ApplyVO> findByApplyStatusIn(List<String> statuses, Pageable pageable);

    
    // 根據駁回原因 查詢模糊匹配並按降冪排序
    @Query("SELECT a FROM ApplyVO a WHERE a.applyRejectReason LIKE %:keyword% ORDER BY a.applyId DESC")
    Page<ApplyVO> searchByRejectReasonWithSorting(@Param("keyword") String keyword, Pageable pageable);


    // 根據會員姓名進行模糊查詢
    @Query("SELECT a FROM ApplyVO a WHERE a.mebId.mebName LIKE %:keyword%")
    List<ApplyVO> findByMemberNameContaining(@Param("keyword") String keyword);


}
