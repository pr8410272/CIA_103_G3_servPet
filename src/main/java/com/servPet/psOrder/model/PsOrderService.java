package com.servPet.psOrder.model;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.servPet.pet.model.PetVO;
import com.servPet.ps.model.PsVO;
import com.servPet.psSvc.model.PsSvcVO;
import org.apache.logging.log4j.core.config.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("PsOrderService")
public class PsOrderService {

    @Autowired
    private PsOrderRepository psOrderRepository;


    // 新增
    public void addPs(PsOrderVO psOrderVO) {
        psOrderRepository.save(psOrderVO);
    }

    // 修改
    public void update(PsOrderVO psOrderVO) {
        psOrderRepository.save(psOrderVO);
    }

    // 刪除
    public void deletePsOrder(Integer psoId) {
        if (psOrderRepository.existsById(psoId))
            psOrderRepository.deleteById(psoId);
    }

    // 查詢所有訂單
    public List<PsOrderVO> getAll() {
        return psOrderRepository.findAll();
    }

    // 根據保母編號查詢訂單
//    public PsOrderVO getOnePsOrder(Integer psId) { // 可更安全清晰的處理返回值為空的情況
//        Optional<PsOrderVO> optional = psOrderRepository.findById(psId);
////   return optional.get();
//        return optional.orElse(null); // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
//    }

// PsOrderService.java
//public List<PsOrderVO> getOrdersByPsId(PsVO psVO) {
//    return psOrderRepository.findByPsId(psVO);
//}

    public List<PsOrderVO> getOrdersByPsId(Integer psId) {
        return psOrderRepository.findByPsVO_PsId(psId);
    }


    // 根據會員編號查詢訂單
    public PsOrderVO getOneMebOrder(Integer mebId) { // 可更安全清晰的處理返回值為空的情況
        Optional<PsOrderVO> optional = psOrderRepository.findById(mebId);
//     return optional.get();
        return optional.orElse(null); // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
    }

    // 根據訂單狀態查詢訂單
    public List<PsOrderVO> getStatusOrder(String bookingStatus) {
        return psOrderRepository.findByBookingStatus(bookingStatus);

    }

public void saveOrder(PsOrderVO psOrderVO) {
    PsOrderVO order = new PsOrderVO();
    order.setPsVO(psOrderVO.getPsVO());
    order.setMebVO(psOrderVO.getMebVO());
    order.setPetVO(psOrderVO.getPetVO());
    order.setBookingDate(psOrderVO.getBookingDate());
    order.setBookingTime(psOrderVO.getBookingTime());
    order.setSvcId(psOrderVO.getSvcId());
    order.setSvcPrice(psOrderVO.getSvcPrice());
    order.setSvcPrice(psOrderVO.getSvcPrice());

    psOrderRepository.save(order);
}


//    @Transactional
//    public void createOrder(PsOrderVO orderVO, List<Integer> svcIds) {
//        // 1. 儲存訂單基本信息
//        PsOrderVO savedOrder = psOrderRepository.save(orderVO);
//
//        // 2. 遍歷服務項目，關聯到該訂單
//        for (Integer svcId : svcIds) {
//            savedOrder.getServiceItems().add(new PsSvcVO(savedOrder, svcId));
//        }
//
//        // 3. 更新訂單
//        psOrderRepository.save(savedOrder);
//    }

    public void createOrder(PsOrderVO orderVO) {
        // 資料驗證（根據需求）
        if (orderVO.getMebVO() == null || orderVO.getPsVO() == null || orderVO.getPetVO() == null || orderVO.getSvcId() == null) {
            throw new IllegalArgumentException("訂單資訊不完整，請檢查資料");
        }

        // 儲存到資料庫
        psOrderRepository.save(orderVO);
    }


}