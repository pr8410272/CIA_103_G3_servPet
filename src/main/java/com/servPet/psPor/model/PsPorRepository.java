package com.servPet.psPor.model;
//美容師作品集

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PsPorRepository extends JpaRepository<PsPorVO, Integer>{

    //刪除該美容師的單一作品 //使用原生 SQL 查詢
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM PET_SITTER_PORTFOLIO WHERE PS_ID = ?1 AND PIC_ID = ?2", nativeQuery = true)
    void deletePictureByPsIdAndPicId(int psId, int picId);


    //一鍵刪除所有該美容師的作品集 //使用原生 SQL 查詢
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM PET_SITTER_PORTFOLIO WHERE PS_ID=?1", nativeQuery = true)
    void deleteAllByPsId(int psId);


    // 根據美容師 ID 查詢作品集 //使用自訂 JPQL 查詢
    @Query(value = "from PsPorVO where psId=?1 order by picId")
    List<PsPorVO> findByPsId(int psId);

    @Query("SELECT p.picId FROM PsPorVO p WHERE p.psId = :psId")
    List<String> findPictureIdsByPsId(@Param("psId") Integer psId);


}