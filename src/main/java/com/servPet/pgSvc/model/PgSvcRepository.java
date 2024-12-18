package com.servPet.pgSvc.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PgSvcRepository extends JpaRepository<PgSvcVO, Integer> {



	// 根據美容師編號查詢其所有服務包含服務項目的詳細資訊
	@Query("SELECT p, s.svcName, s.svcDescr " + "FROM PgSvcVO p " + "JOIN PgSvcItemVO s ON p.svcId = s.svcId "
			+ "WHERE p.pgId = :pgId AND s.svcIsDeleted = '1'")
	List<Object[]> findServicesByPgIdWithItemDetails(@Param("pgId") Integer pgId);

	// 根據項目清單編號查詢服務詳情以供編輯
	@Query(value = "SELECT p.PGSVC_ID, p.PG_ID, p.SVC_ID, p.SVC_TYPE, p.SVC_PRICE, s.SVC_NAME, s.SVC_DESCR "
			+ "FROM PET_GROOMER_SERVICE p " + "JOIN PET_GROOMER_SERVICE_ITEM s ON p.SVC_ID = s.SVC_ID "
			+ "WHERE p.PGSVC_ID = ?1", nativeQuery = true)
	List<Object[]> findServicesByPgSvcId(Integer pgSvcId);

}
