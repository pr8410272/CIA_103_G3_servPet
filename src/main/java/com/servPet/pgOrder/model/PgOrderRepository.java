package com.servPet.pgOrder.model;
//美容師服務訂單

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PgOrderRepository extends JpaRepository<PgOrderVO, Integer> {
	
	// 查找寵物圖片
	@Query(value = "SELECT PET_IMG FROM PET where PET_ID = ?1" , nativeQuery = true)
	byte[] findPetImgById(@Param("petId") Integer petId);
	
	// 修改訂單狀態為"2"進行中
	@Modifying
	@Transactional
	@Query(value = "UPDATE PET_GROOMER_ORDER o SET o.BOOKING_STATUS = '2' WHERE o.BOOKING_DATE = :today AND o.BOOKING_STATUS = '0' AND o.BOOKING_STATUS != '3'", nativeQuery = true)
	void updateBookingStatusToInProgress(@Param("today") Date today);

	// 修改訂單狀態為"1"已完成
	@Modifying
	@Transactional
	@Query(value = "UPDATE PET_GROOMER_ORDER o SET o.BOOKING_STATUS = '1' WHERE o.BOOKING_DATE < :yesterday AND o.BOOKING_STATUS IN ('0', '2') AND o.BOOKING_STATUS != '3'", nativeQuery = true)
	void updateBookingStatusToCompleted(@Param("yesterday") Date yesterday);


	// 篩選出非隱藏的服務項目相關資料
	@Query(value = "SELECT  ps.PGSVC_ID, s.SVC_ID, s.SVC_NAME, ps.SVC_TYPE , ps.SVC_PRICE "
			+ "FROM PET_GROOMER_SERVICE ps " + "JOIN PET_GROOMER_SERVICE_ITEM s ON ps.SVC_ID = s.SVC_ID "
			+ "WHERE ps.PG_ID = ?1 AND s.SVC_IS_DELETED = '1'", nativeQuery = true)
	List<Object[]> findAvailableServicesByPgId(@Param("pgId") Integer pgId);

	@Query(value = "SELECT COUNT(*) " + "FROM PET_GROOMER_ORDER o "
			+ "WHERE o.PG_ID = ?1 AND o.BOOKING_DATE = ?2 AND o.BOOKING_TIME = ?3", nativeQuery = true)
	BigInteger countSlotBookingsNative(@Param("pgId") Integer pgId, @Param("bookingDate") Date bookingDate,
			@Param("bookingTime") String bookingTime);

	// 查詢美容師的已預約時段
	@Query("SELECT o.bookingDate, o.bookingTime FROM PgOrderVO o WHERE o.pgId = :pgId AND o.bookingStatus != '3'")
	List<Object[]> findBookedSlots(@Param("pgId") Integer pgId);

	// 根據訂單 ID 查詢訂單詳情，包含會員和寵物的其他信息
	@Query(value = "SELECT m.MEB_NAME, p.PET_NAME, p.PET_TYPE, p.PET_IMG, i.SVC_NAME,s.SVC_TYPE " + "FROM PET_GROOMER_ORDER o "
			+ "JOIN MEMBER m ON o.MEB_ID = m.MEB_ID " + "JOIN PET p ON o.PET_ID = p.PET_ID AND o.MEB_ID = p.MEB_ID "
			+ "JOIN PET_GROOMER_SERVICE s ON o.PGSVC_ID = s.PGSVC_ID "
			+ "JOIN PET_GROOMER_SERVICE_ITEM i ON s.SVC_ID = i.SVC_ID " + "WHERE o.PGO_ID = ?1", nativeQuery = true)
	List<Object[]> findOrderDetailsWithMemberAndPet(Integer pgoId);

	// 根據美容師編號查詢訂單
	@Query(value = "from PgOrderVO where pgId=?1 ")
	List<PgOrderVO> findOrderByPgId(int pgId);
	
	
	// 根據訂單編號查詢(學雍
	@Query(value = "from PgOrderVO where pgoId=?1 order by pgoId")
    PgOrderVO findByPgOrderId(int pgoId);

	


}
