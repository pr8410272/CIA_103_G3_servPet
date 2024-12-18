package com.servPet.pgSvcItem.model;
//美容師服務總覽

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PgSvcItemRepository extends JpaRepository<PgSvcItemVO, Integer> {

	@Modifying
    @Transactional 
	@Query("UPDATE PgSvcItemVO p SET p.svcIsDeleted = :svcIsDeleted")
	void updateAllSvcItemStatus(@Param("svcIsDeleted") String svcIsDeleted);


	// 查詢未被隱藏的項目
	List<PgSvcItemVO> findBySvcIsDeleted(String svcIsDeleted);

	// 以服務項目名稱查詢
	Optional<PgSvcItemVO> findBySvcName(String svcName);

}
