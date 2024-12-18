package com.servPet.admin.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminVO, Integer> {

    // 自定義條件查詢方法，根據 adminId, adminName, 和 adminAccStatus 查詢
    @Query(value = "from AdminVO where adminId = ?1 and adminName like ?2 and adminAccStatus = ?3 order by adminId")
    List<AdminVO> findByOthers(int adminId, String adminName, String adminAccStatus);
	
    // 自定義查詢所有 adminAcc 狀態的管理員
    List<AdminVO> findByadminAccStatus(String adminAccStatus);

    // 查詢指定帳號的管理員
    AdminVO findByadminAcc(String adminAcc);
    
 // 查詢指定密碼的管理員
    AdminVO findByadminPwd(String adminPwd);

}
