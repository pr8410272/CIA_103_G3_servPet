package com.servPet.ntfy.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servPet.ntfy.model.NtfyVO;

@Repository
public interface NtfyRepository extends JpaRepository<NtfyVO, Integer> {
    
    // 查找某個會員的所有通知（按日期降序排序）
    List<NtfyVO> findByMebVO_MebIdOrderByNtfyMgmtIdDesc(Integer mebId);
    
    // 查找某個會員的特定狀態的通知（用於查詢未讀通知）
    List<NtfyVO> findByMebVO_MebIdAndStatusOrderByNtfyMgmtIdDesc(Integer mebId, Integer status);
    
    // 查找所有通知（按日期降序排序）
    List<NtfyVO> findAllByOrderByNtfyMgmtIdDesc();
    
    //刪除指定通知ID
    @Modifying
    @Query("DELETE FROM NtfyVO n WHERE n.ntfyMgmtId = :ntfyMgmtId")
    void deleteByNtfyId(Integer ntfyMgmtId);
   
    
    // 一鍵已讀
    @Modifying
    @Query("UPDATE NtfyVO n SET n.status = 1 WHERE n.mebVO.mebId = :mebId AND n.status = 0")
    void updateAllUnreadToRead(@Param("mebId") Integer mebId);

    
}

