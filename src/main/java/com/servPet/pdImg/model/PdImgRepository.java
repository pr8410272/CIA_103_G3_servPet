package com.servPet.pdImg.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.servPet.pdDetails.model.PdDetailsVO;

public interface PdImgRepository extends JpaRepository<PdImgVO, Integer> {
	List<PdImgVO> findByPdDetailsVO(PdDetailsVO pdDetailsVO);
	List<PdImgVO> findAllById(Iterable<Integer> ids);
	
//    @Query("SELECT MIN(p.pdImgId) FROM PdImgVO p WHERE p.pdDetailsVO.pdId = :pdId")
//    Integer findMinPdImgIdByPdId(@Param("pdId") Integer pdId);
    
    @Query("SELECT MIN(p.pdImgId) FROM PdImgVO p WHERE p.pdDetailsVO.pdId = :pdId")
    Optional<Integer> findMinPdImgIdByPdId(@Param("pdId") Integer pdId);
}