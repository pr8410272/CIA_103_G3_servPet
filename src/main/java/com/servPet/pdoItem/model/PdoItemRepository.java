package com.servPet.pdoItem.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PdoItemRepository extends JpaRepository<PdoItemVO, Integer> {

	// 查詢某訂單的所有細項
	List<PdoItemVO> findByPdoVO_PdoId(Integer pdoId);

	List<PdoItemVO> findByPdoVO(Integer pdoVO);

}
