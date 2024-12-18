package com.servPet.pgBooking.model;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;

@Service
public class PgBookingService {

    @Autowired
    private PgBookingRepository pgBookingRepository;
    
    @Autowired
    private MebService mebService;
    // 暫時使用固定會員ID，之後改為從session獲取
//    private static final Integer TEMP_MEMBER_ID = 2010;

    public List<PgBookingDTO> getMemberBookings(Principal principal) {
        String email = principal.getName();
        Optional<MebVO> memberOptional = mebService.findMemberByEmail(email);
        	MebVO mebVO = memberOptional.get();
        	Integer mebId = mebVO.getMebId();
        	return pgBookingRepository.findByMebVO_MebId(mebId).stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

  
    @Transactional
    public void updateBooking(Integer pgoId, String bookingDate, String bookingTime) {
        PgBookingVO order = pgBookingRepository.findById(pgoId)
                .orElseThrow(() -> new RuntimeException("預約未找到"));

        LocalDate localDate = LocalDate.parse(bookingDate);

        // 驗證新預約時間
        validateBooking(order.getPgVO().getPgId(), localDate, bookingTime);

        // 更新預約資料
        order.setBookingDate(localDate);
        order.setBookingTime(bookingTime);
        pgBookingRepository.save(order);
    }

    //取消預約 
    
    public void cancelBooking(Integer pgoId,Principal principal) {
        PgBookingVO order = pgBookingRepository.findById(pgoId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        // 驗證是否為當前會員的預約
        String email = principal.getName();
        Optional<MebVO> memberOptional = mebService.findMemberByEmail(email);
        	MebVO mebVO = memberOptional.get();
        	Integer mebId = mebVO.getMebId();
        	
        if (!order.getMebVO().getMebId().equals(mebId)) {
            throw new RuntimeException("Unauthorized access to booking");
        }
        order.setBookingStatus("3"); // 已取消
        pgBookingRepository.save(order);
    }

    //評價
    public void addRating(Integer pgoId, Integer star, String comment,Principal principal) {
        PgBookingVO order = pgBookingRepository.findById(pgoId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        // 驗證是否為當前會員的預約
        String email = principal.getName();
        Optional<MebVO> memberOptional = mebService.findMemberByEmail(email);
        	MebVO mebVO = memberOptional.get();
        	Integer mebId = mebVO.getMebId();
        if (!order.getMebVO().getMebId().equals(mebId)) {
            throw new RuntimeException("Unauthorized access to booking");
        }
        // 只有已完成的預約才能評價
        if (!"1".equals(order.getBookingStatus())) {
            throw new RuntimeException("Cannot rate incomplete booking");
        }
        order.setStar(star);
        order.setRatingComment(comment);
        pgBookingRepository.save(order);
    }

    private PgBookingDTO convertToDTO(PgBookingVO order) {
        PgBookingDTO dto = new PgBookingDTO();
        dto.setPgoId(order.getPgoId());
        dto.setPgId(order.getPgVO().getPgId());
        dto.setPgName(order.getPgVO().getPgName());
        dto.setPetName(order.getPetVO().getPetName());
        dto.setSvcId(order.getPgSvcId());
        dto.setSvcName(getSvcNameByPgSvcId(order.getPgSvcId()));
        dto.setBookingDate(order.getBookingDate());
        dto.setBookingTime(order.getBookingTime());
        dto.setBookingStatus(order.getBookingStatus());
        dto.setStar(order.getStar());
        dto.setRatingComment(order.getRatingComment());
        return dto;
    }

    private boolean isPgAvailable(Integer pgId, LocalDate date, String time) {
        PgBookingDTO schedule = pgBookingRepository.findScheduleByPgId(pgId)
                .orElseThrow(() -> new RuntimeException("美容師不存在"));

        String schDate = schedule.getSchDate(); // 美容師排班日期
        String schTime = schedule.getSchTime(); // 美容師排班時間

        // 計算當前日期是星期幾 (0: 周日, 1: 周一, ..., 6: 周六)
        int dayOfWeek = date.getDayOfWeek().getValue() % 7;

        // 檢查日期和時間是否可用
        return schDate.length() > dayOfWeek && schDate.charAt(dayOfWeek) == '1'
                && schTime.length() > Integer.parseInt(time) && schTime.charAt(Integer.parseInt(time)) == '1';
    }

    @Transactional
    public void validateBooking(Integer pgId, LocalDate date, String time) {
        // 檢查時間段是否被占用
        if (!isTimeSlotAvailable(pgId, date, time)) {
            throw new RuntimeException("所選時間段已被預約");
        }

        // 檢查美容師是否工作
        if (!isPgAvailable(pgId, date, time)) {
            throw new RuntimeException("美容師在所選時間段不工作");
        }
    }


 // 檢查時間段是否可用
    private boolean isTimeSlotAvailable(Integer pgId, LocalDate date, String time) {
        return pgBookingRepository.findConflictingBookings(pgId, date, time).isEmpty();
    }

    private String getSvcNameByPgSvcId(Integer pgSvcId) {
        return pgBookingRepository.findSvcNameByPgSvcIdNative(pgSvcId);
    }

}
