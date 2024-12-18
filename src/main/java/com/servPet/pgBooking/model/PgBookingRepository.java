package com.servPet.pgBooking.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.servPet.pgBooking.model.PgBookingDTO;
import com.servPet.pgBooking.model.PgBookingVO;

public interface PgBookingRepository extends JpaRepository<PgBookingVO, Integer> {
	
	// 根據會員 ID 查詢預約
    List<PgBookingVO> findByMebVO_MebId(Integer mebId);

    // 查詢美容師的排班資料，返回 PgBookingDTO
    @Query("SELECT new com.servPet.pgBooking.model.PgBookingDTO(pg.pgId, pg.pgName, pg.schDate, pg.schTime) " +
           "FROM PgVO pg WHERE pg.pgId = :pgId")
    Optional<PgBookingDTO> findScheduleByPgId(@Param("pgId") Integer pgId);

    // 查詢時間衝突的預約
    @Query("SELECT o FROM PgBookingVO o WHERE o.pgVO.pgId = :pgId " +
           "AND o.bookingDate = :date AND o.bookingTime = :time " +
           "AND o.bookingStatus != '3'")
    List<PgBookingVO> findConflictingBookings(@Param("pgId") Integer pgId, 
                                            @Param("date") LocalDate date, 
                                            @Param("time") String time);

    // 檢查美容師的可用性
    @Query(value = "SELECT COUNT(*) FROM PET_GROOMER " +
                   "WHERE PG_ID = :pgId AND SUBSTRING(SCH_DATE, :weekDay, 1) = '1' " +
                   "AND SUBSTRING(SCH_TIME, :timeSlot, 1) = '1' AND PG_STATUS = '1'", nativeQuery = true)
    int checkPgAvailability(@Param("pgId") Integer pgId, 
                             @Param("weekDay") int weekDay, 
                             @Param("timeSlot") int timeSlot);

    // 更新預約日期與時間
    @Modifying
    @Query("UPDATE PgBookingVO p SET p.bookingDate = :bookingDate, p.bookingTime = :bookingTime WHERE p.pgoId = :pgoId")
    void updateBookingDateAndTime(@Param("bookingDate") LocalDate bookingDate, 
                                  @Param("bookingTime") String bookingTime, 
                                  @Param("pgoId") Integer pgoId);

    // 查詢服務名稱
    @Query(value = "SELECT si.svc_name FROM PET_GROOMER_SERVICE pgs " +
                   "JOIN PET_GROOMER_SERVICE_ITEM si ON pgs.SVC_ID = si.SVC_ID " +
                   "WHERE pgs.PGSVC_ID = :pgSvcId", nativeQuery = true)
    String findSvcNameByPgSvcIdNative(@Param("pgSvcId") Integer pgSvcId);
}
