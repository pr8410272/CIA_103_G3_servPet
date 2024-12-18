package com.servPet.psSvc.model;

import com.servPet.psSvcItem.model.PsSvcItemVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PsSvcRepository extends JpaRepository<PsSvcVO, Integer> {

//    //根據服務項目查詢保母
//    @Query(value = "from PsSvcVO where psVO.psId=?1 order by psSvcItemVO.svcId")
//    List<PsSvcVO> findBySvcId(int svcId);

    //根據保母查詢服務項目?
//    @Query(value = "from PsSvcVO where psSvcVO.psId=?1 order by psSvcItemVO.svcId")
//    List<PsSvcVO> findBySvcId(int svcId);

    // 根據保母 ID 和 服務項目 ID 查找特定服務項目

    PsSvcVO findByPsSvcId_PsVO_PsIdAndPsSvcId_PsSvcItemVO_SvcId(Integer psId, Integer svcId);

    @Query("SELECT p FROM PsSvcVO p WHERE p.psSvcId.psVO.psId = :psId")
    List<PsSvcVO> getPsSvcItemVOByPsSvcVO(@Param("psId")  Integer psId);

//    //更新
//    @Query(value = "UPDATE pet_sitter_service SET ps_id = :price WHERE svc_id = :psId AND svc_price = :svc_price", nativeQuery = true)
//    int updateServicePrice(@Param("ps_id") Integer ps_id, @Param("svc_id") Integer svc_id, @Param("svc_price") Integer svc_price);
//
//    //新增

//    List<PsSvcItemVO> findByPsId(Integer psSvcId);
//    List<PsSvcVO> findByPsSvcId_PsId(Integer psId);
      List<PsSvcVO> findByPsSvcId_PsVO_PsId(Integer psId);

    Optional<PsSvcVO> findByPsSvcId_PsSvcItemVO_SvcId(Integer svcId);



}
