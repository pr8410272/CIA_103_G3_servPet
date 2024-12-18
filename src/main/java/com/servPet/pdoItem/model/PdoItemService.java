package com.servPet.pdoItem.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servPet.cartDetails.model.CartDetailsService;
import com.servPet.pdDetails.model.PdDetailsRepository;
import com.servPet.pdDetails.model.PdDetailsVO;
import com.servPet.pdo.model.PdoService;
import com.servPet.pdo.model.PdoVO;

@Service("pdoItemService")
public class PdoItemService {

	@Autowired
    private PdoItemRepository repository;
	
	@Autowired
	CartDetailsService cartDetailsSvc;

	@Autowired
	PdoService pdoSvc;

	@Autowired
	PdDetailsRepository pdDetailsRepository;
	
    /**
     * 批量保存訂單商品細項
     * @param pdoItems 訂單商品細項列表
     */
    public void saveAllOrderItems(List<PdoItemVO> pdoItems) {
        this.repository.saveAll(pdoItems);
    }
    
    // 保存訂單細項
    @Transactional
    public void saveOrderItems(List<PdoItemVO> orderItems) {
    	this.repository.saveAll(orderItems);
    }

    // 查詢某訂單的所有細項
    public List<PdoItemVO> getOrderItemsByPdoId(Integer pdoId) {
        return this.repository.findByPdoVO_PdoId(pdoId);
    }
    
    
//    /**
//     * 儲存單筆訂單細項
//     */
//    public PdoItemVO saveOrderItem(PdoVO pdoVO, PdDetailsVO pdDetailsVO, Integer pdQty, Integer pdPrice) {
//        PdoItemVO pdoItem = new PdoItemVO();
//        pdoItem.setPdoVO(pdoVO);                 // 設定對應的訂單
//        pdoItem.setPdDetailsVO(pdDetailsVO);     // 設定商品編號
//        pdoItem.setPdQty(pdQty);                 // 商品數量
//        pdoItem.setPdPrice(pdPrice);             // 商品單價
//        pdoItem.setPdTotalPrice(pdQty * pdPrice);// 小計金額
//
//        return this.repository.save(pdoItem);  // 儲存到資料庫
//    }

    @Transactional
    public void createOrderItemsAndUpdateStock(PdoVO pdoVO, List<PdoItemVO> pdoItems) {
        for (PdoItemVO item : pdoItems) {
            // 1. 保存訂單細項
            item.setPdoVO(pdoVO); // 使用 PdoVO 來設置訂單關聯
            this.repository.save(item);

            // 2. 更新商品庫存
            PdDetailsVO product = pdDetailsRepository.findById(item.getPdDetailsVO().getPdId())
                    .orElseThrow(() -> new RuntimeException("商品不存在, ID: " + item.getPdDetailsVO().getPdId()));

            int currentStock = product.getPdQty();
            int newStock = currentStock - item.getPdQty();

            if (newStock < 0) {
                throw new IllegalStateException("商品庫存不足，商品ID: " + product.getPdId());
            }

            product.setPdQty(newStock); // 扣除庫存
            pdDetailsRepository.save(product); // 更新商品管理表格
        }
    }


}