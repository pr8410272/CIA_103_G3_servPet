package com.servPet.adminPer.model;

import com.servPet.admin.model.AdminVO;
import com.servPet.fnc.model.FncVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * AdminPerRepository 對應到資料庫的 ADMIN_PER 表格。
 * 提供對管理員權限資料的操作。
 */
public interface AdminPerRepository extends JpaRepository<AdminPerVO, Integer> {

    // ● (自訂)刪除指定管理員的權限記錄
    @Transactional
    @Modifying
    @Query("delete from AdminPerVO ap where ap.adminVO.adminId = ?1")
    void deleteByAdminId(int adminId);

    // ● (自訂)查詢特定管理員的權限資料
    @Query("from AdminPerVO ap where ap.adminVO.adminId = ?1 order by ap.adminPerId")
    List<AdminPerVO> findByAdminId(int adminId);

    // ● (自訂)查詢特定權限功能的管理員資料
    @Query("from AdminPerVO ap where ap.fncVO.fncId = ?1 order by ap.adminPerId")
    List<AdminPerVO> findByFncId(int fncId);

    // ● (自訂)查詢特定管理員對特定權限功能的資料
    @Query("from AdminPerVO ap where ap.adminVO = ?1 and ap.fncVO = ?2")
    AdminPerVO findByAdminAndFnc(AdminVO adminVO, FncVO fncVO);

}
