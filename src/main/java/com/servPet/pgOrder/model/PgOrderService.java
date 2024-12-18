package com.servPet.pgOrder.model;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.servPet.pg.model.PgVO;
import com.servPet.pgSvc.model.PgServiceDetailsDTO;

@Service
public class PgOrderService {

	@Autowired
	private PgOrderRepository pgOrderRepository;
	
	// 每天凌晨執行一次修改訂單狀態
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateBookingStatus() {
    	
        Date today = Date.valueOf(LocalDate.now());
        Date yesterday = Date.valueOf(LocalDate.now().minusDays(1));

        // 更新為"進行中"
        pgOrderRepository.updateBookingStatusToInProgress(today);

        // 更新為"已完成"
        pgOrderRepository.updateBookingStatusToCompleted(yesterday);
    }
	

	public boolean isSlotAvailable(Integer pgId, Date bookingDate, String bookingTime) {
		BigInteger count = pgOrderRepository.countSlotBookingsNative(pgId, bookingDate, bookingTime);
		return count != null && count.intValue() == 0; // 如果 count 為 0，表示時段可用
	}
	
	

	public List<PgServiceDetailsDTO> getAvailableServicesByPgId(Integer pgId) {
		List<Object[]> result = pgOrderRepository.findAvailableServicesByPgId(pgId);

		return result.stream().map(row -> new PgServiceDetailsDTO((Integer) row[0], // pgSvcId
				(Integer) row[1], // svcId
				(String) row[2], // svcName
				(String) row[3], // svcType
				(Integer) row[4] // svcPrice
		)).collect(Collectors.toList());
	}

	// 查出美容師目前可預約時段
	public List<AvailableSlot> getAvailableSlots(PgVO pg, LocalDate startDate, LocalDate endDate) {
		// 校驗 SCH_DATE 和 SCH_TIME 格式
		String schDate = pg.getSchDate();
		String schTime = pg.getSchTime();
		if (schDate == null || schDate.length() != 7) {
			throw new IllegalArgumentException("SCH_DATE 格式無效，應為 7 位字符串");
		}
		if (schTime == null || schTime.length() != 3) {
			throw new IllegalArgumentException("SCH_TIME 格式無效，應為 3 位字符串");
		}

		// 獲取已預約的時間段
		List<Object[]> bookedSlots = pgOrderRepository.findBookedSlots(pg.getPgId());

		// 計算所有可用時間段
		List<AvailableSlot> availableSlots = new ArrayList<>();

		// 使用 for 迴圈
		for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
			int dayOfWeek = currentDate.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday

			// 檢查當天是否營業
			if (dayOfWeek <= schDate.length() && schDate.charAt(dayOfWeek - 1) == '1') {
				for (int timeSlot = 0; timeSlot < schTime.length(); timeSlot++) {
					if (schTime.charAt(timeSlot) == '1') {
						int finalTimeSlot = timeSlot; // 創建一個 effectively final 的變數
						LocalDate finalDate = currentDate; // 確保 currentDate 為 effectively final

						boolean isBooked = bookedSlots.stream()
								.anyMatch(slot -> slot[0] != null && slot[1] != null
										&& LocalDate.parse(slot[0].toString()).equals(finalDate)
										&& slot[1].equals(String.valueOf(finalTimeSlot)));

						if (!isBooked) {
							availableSlots.add(new AvailableSlot(currentDate, timeSlot));
						}
					}
				}
			}
		}

		return availableSlots;
	}

	// 根據訂單 ID 查詢訂單詳情
	public PgOrderVO getOrderDetails(Integer pgoId) {
		return pgOrderRepository.findById(pgoId).orElse(null); // 如果找不到則返回 null
	}

	// 根據訂單 ID 查詢訂單詳情，包含會員和寵物的其他信息
	public PgOrderDTO getOrderDetailsWithMemberAndPet(Integer pgoId) {
		// 查詢會員和寵物的其他信息

		List<Object[]> otherDetails = pgOrderRepository.findOrderDetailsWithMemberAndPet(pgoId);
		PgOrderDTO pgOrderDTO = new PgOrderDTO();
		for (Object[] o : otherDetails) {
			pgOrderDTO.setMebName((String) o[0]);
			pgOrderDTO.setPetName((String) o[1]);
			pgOrderDTO.setPetType((String) o[2]);
			pgOrderDTO.setPetImg(o[3] != null ? (byte[]) o[3] : null);
			pgOrderDTO.setSvcName((String) o[4]);
			pgOrderDTO.setSvcType((String) o[5]);
		}

		return pgOrderDTO; // 如果找不到其他信息則返回 null
	}

	// 根據美容師 ID 查詢所有訂單
	public List<PgOrderVO> findOrderByPgId(Integer pgId) {
		return pgOrderRepository.findOrderByPgId(pgId);
	}

	// 新增訂單
	public void addPgOrder(PgOrderVO pgOrderVO) {
		pgOrderRepository.save(pgOrderVO);
	}
	
	  // 查詢所有訂單(學雍
    public List<PgOrderVO> getAll() {
        return pgOrderRepository.findAll();
    }

}

