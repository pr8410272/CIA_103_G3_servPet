package com.servPet.pg.model;
//美容師

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PgRepository extends JpaRepository<PgVO, Integer> {
	
	// 查詢指定美容師的證件照(學雍)
	@Query(value = "select pgLicenses from PgVO where pgId = :pgId")
	 byte[] findLicensesByPgId(@Param("pgId") Integer pgId);
	
	// 查詢所有營業狀態為"1"營業中的美容師
	List<PgVO> findByPgStatus(String pgStatus);


	// 依照姓名查詢
	@Query(value = "from PgVO where pgName like?1 order by pgId")// 可省略
	List<PgVO> findByNameLikeOrderByPgId(String pgName);
	
	@Query(value = "select pgPic from PgVO where pgId = :pgId")
	byte[] findPicById(@Param("pgId") Integer pgId);
	
	// 查詢指定美容師的編號(學雍)
	PgVO findByPgId(Integer pgId);
	
    // 查詢指定美容師的密碼(學雍)
	PgVO findByPgPw(String pgPw);
}


