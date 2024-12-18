package com.servPet.pgCompl.model;

import com.servPet.pgCompl.model.PgComplVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PgComplRepository extends JpaRepository<PgComplVO, Integer>, JpaSpecificationExecutor<PgComplVO> {

	// 根據美容師編號查詢檢舉資料
	List<PgComplVO> findByPgId(Integer pgId);

	// 根據會員編號查詢檢舉資料
	List<PgComplVO> findByMebId(Integer mebId);

	// 根據檢舉狀態查詢檢舉資料
	List<PgComplVO> findByPgComplStatus(String pgComplStatus);

	// 根據日期範圍查詢檢舉資料
	@Query("from PgComplVO where pgComplDate >= ?1 and pgComplDate <= ?2 order by pgComplDate")
	List<PgComplVO> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

	// 根據檢舉描述模糊查詢
	@Query("from PgComplVO where pgComplDes like %?1% order by pgComplId")
	List<PgComplVO> findByPgComplDesLike(String pgComplDes);
}
