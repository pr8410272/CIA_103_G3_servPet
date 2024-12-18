package com.servPet.mebPdo.model;

import com.servPet.pdo.model.PdoVO;
import com.servPet.pdoItem.model.PdoItemRepository;
import com.servPet.pdoItem.model.PdoItemVO;
import com.servPet.vtr.model.VtrService;
import com.servPet.pdo.model.PdoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.servPet.meb.model.MebVO;

@Service
public class MebPdoService {

    @Autowired
    private PdoRepository pdoRepository;
    
    @Autowired
    private PdoItemRepository pdoItemRepository;
    
    @Autowired
    private VtrService vtrService;

    // 取得會員的所有訂單
    public List<PdoVO> getAllOrdersByMebId(Integer mebId) {
        return pdoRepository.findAll().stream()
                .filter(pdo -> pdo.getMebVO() != null && pdo.getMebVO().getMebId().equals(mebId))
                .toList();
    }

    // 取得單一訂單資訊
    public PdoVO getOrderById(Integer pdoId) {
        return pdoRepository.findById(pdoId).orElse(null);
    }
    
    // 取得指定訂單的詳細資訊
    public List<PdoItemVO> getPdoItemsByPdoId(Integer pdoId) {
        return pdoItemRepository.findByPdoVO(pdoId);
    }

    // 取消訂單
//    public void cancelOrder(Integer pdoId) {
//        Optional<PdoVO> optionalOrder = pdoRepository.findById(pdoId); // 查詢訂單
//        if (!optionalOrder.isPresent()) { // 檢查是否存在
//            throw new RuntimeException("無此訂單");
//        }
//        PdoVO pdo = optionalOrder.get(); // 取得訂單物件
//        pdo.setPdoStatus("已取消"); // 設定狀態為"已取消"
//        pdoRepository.save(pdo); // 更新訂單狀態到資料庫
//    }
    
//    // 取消訂單
//    public void cancelOrder(Integer pdoId) {
//        // 查詢訂單
//        PdoVO pdo = pdoRepository.findById(pdoId).orElse(null);
//
//        // 檢查訂單是否存在
//        if (pdo == null) {
//            throw new RuntimeException("訂單不存在");
//        }
//
//        // 檢查訂單狀態，只有 '進行中' (假設狀態為 '0') 才能取消
//        if (!"0".equals(pdo.getPdoStatus())) {
//            throw new IllegalStateException("該訂單無法取消，因為訂單狀態不是進行中");
//        }
//
//        // 修改訂單狀態為已取消
//        pdo.setPdoStatus("2");
//        pdo.setPaymentStatus("2");
//        pdo.setPdoUpdateTime(new java.util.Date());
//
//        // 儲存更新後的訂單
//        PdoVO updatedPdo = pdoRepository.save(pdo);
//        if (updatedPdo != null) {
//            System.out.println("訂單取消成功: 訂單狀態已更新為 '已取消'");
//        } else {
//            System.out.println("訂單取消失敗: 無法更新訂單");
//            throw new RuntimeException("更新訂單失敗");
//        }
//    }

    // 取消訂單並金儲值金退回錢包
    public void cancelOrder(Integer pdoId) {
        // 查詢訂單
        PdoVO pdo = pdoRepository.findById(pdoId).orElse(null);

        // 檢查訂單是否存在
        if (pdo == null) {
            throw new RuntimeException("訂單不存在");
        }

        // 檢查訂單狀態，只有 '進行中' (假設狀態為 '0') 才能取消
        if (!"0".equals(pdo.getPdoStatus())) {
            throw new IllegalStateException("該訂單無法取消，因為訂單狀態不是進行中");
        }

        // 修改訂單狀態為已取消
        pdo.setPdoStatus("2"); // 設定訂單狀態為已取消
        pdo.setPaymentStatus("2"); // 設定付款狀態為已退款
        pdo.setPdoUpdateTime(new java.util.Date());
        pdoRepository.save(pdo);

        // 將金額退回到會員錢包
        Integer refundAmount = pdo.getPdTotalPrice(); // 訂單金額
        Integer mebId = pdo.getMebVO().getMebId();    // 取得會員 ID
        vtrService.createTransaction(mebId, refundAmount, "退款"); // 建立退款交易紀錄
    }

    
//    public void updateShippingAddress(Integer pdoId, String newAddress) {
//        PdoVO pdo = pdoRepository.findById(pdoId).orElse(null); // 查詢訂單
//        if (pdo != null) {
//            // 條件檢查
//            if ("0".equals(pdo.getPdoStatus()) && "1".equals(pdo.getPaymentStatus()) &&
//                "0".equals(pdo.getShippingMethod()) && "0".equals(pdo.getShippingStatus())) {
//                // 更新配送地址
//                pdo.setShippingAddr(newAddress);
//                // 更新訂單最後修改時間
//                pdo.setPdoUpdateTime(new java.util.Date());
//                // 儲存更新後的訂單
//                pdoRepository.save(pdo);
//            } else {
//                throw new IllegalStateException("當前訂單狀態不允許修改配送地址");
//            }
//        } else {
//            throw new IllegalArgumentException("訂單不存在");
//        }
//    }

    
}
