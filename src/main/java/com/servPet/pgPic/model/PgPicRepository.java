package com.servPet.pgPic.model;
//美容師作品集

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PgPicRepository extends JpaRepository<PgPicVO, Integer> {

	// 一鍵刪除所有該美容師的作品集 //使用原生 SQL 查詢
//	@Transactional
//	@Modifying
//	@Query(value = "DELETE FROM PET_GROOMER_PICTURE WHERE PG_ID=?1", nativeQuery = true)
//	void deleteAllByPgId(int pgId);

	// 根據美容師 ID 查詢作品集ID
	@Query(value = "select picId FROM PgPicVO p WHERE p.pgId = :pgId")
	List<Integer> getPictureIdsByPgId(Integer pgId);

	// 根據作品集 ID 查詢作品集圖片
	@Query("SELECT p.pgSvcPic FROM PgPicVO p WHERE p.picId = :picId")
	byte[] findPictureById(@Param("picId") Integer picId);

}
