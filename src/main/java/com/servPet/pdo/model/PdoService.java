package com.servPet.pdo.model;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servPet.meb.model.MebRepository;
import com.servPet.meb.model.MebService;
import com.servPet.meb.model.MebVO;
import com.servPet.pdDetails.model.PdDetailsRepository;
import com.servPet.pdDetails.model.PdDetailsVO;
import com.servPet.pdoItem.model.PdoItemRepository;
import com.servPet.pdoItem.model.PdoItemVO;

@Service("pdoService")
public class PdoService {

	@Autowired
	PdoRepository repository;

	@Autowired
	PdoItemRepository pdoItemRepository;

	@Autowired
	PdDetailsRepository pdDetailsRepository;

	@Autowired
	MebRepository mebRepository;

	@Autowired
	MebService mebSvc;

	@Transactional
	public Integer createOrderWithItems(String shippingAddr, String shippingMethod, Integer totalAmount,
			List<PdDetailsVO> productList, List<Integer> quantities,Principal principal) {
		try {
	        String email = principal.getName();
	        Optional<MebVO> memberOptional = mebSvc.findMemberByEmail(email);
	        MebVO mebVO = memberOptional.get();
	        Integer mebId = mebVO.getMebId();
			// 模擬會員信息，實際應從登入用戶獲取

			// 1. 建立訂單
			PdoVO order = new PdoVO();
			order.setMebVO(mebVO);
			order.setShippingAddr(shippingAddr);
			order.setShippingMethod(shippingMethod);
			order.setPdTotalPrice(0); // 初始值
			order.setPdoDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
			order.setPdoUpdateTime(java.sql.Timestamp.valueOf(LocalDateTime.now()));
			order.setPdoStatus("0");
			order.setPaymentStatus("0");
			order.setShippingStatus("0");

			// 儲存訂單
			PdoVO savedOrder = this.repository.save(order);

			// 2. 建立訂單細項並計算整體總金額
			int totalPrice = 0; // 訂單總金額
			for (int i = 0; i < productList.size(); i++) {
				PdDetailsVO product = productList.get(i);

				// 商品價格與數量
				int price = (product.getPdPrice() != null) ? product.getPdPrice() : 0;
				int quantity = (quantities.get(i) != null) ? quantities.get(i) : 0;
				int itemTotal = price * quantity;

				// 累加整張訂單的總金額
				totalPrice += itemTotal;
			}

			// 3. 儲存訂單細項並設置 PD_TOTAL_PRICE
			for (int i = 0; i < productList.size(); i++) {
				PdDetailsVO product = productList.get(i);
				int price = (product.getPdPrice() != null) ? product.getPdPrice() : 0;
				int quantity = (quantities.get(i) != null) ? quantities.get(i) : 0;

				// 檢查商品庫存是否足夠
				if (product.getPdQty() < quantity) {
					throw new RuntimeException("商品庫存不足，商品ID: " + product.getPdId());
				}

				// 扣除商品庫存
				product.setPdQty(product.getPdQty() - quantity);

				// 如果庫存低於5，自動設置 pdStatus 為停售 (2)
				if (product.getPdQty() < 5) {
					product.setPdStatus("2");
				}

				// 創建訂單細項
				PdoItemVO item = new PdoItemVO();
				item.setPdoVO(savedOrder);
				item.setPdDetailsVO(product);
				item.setPdQty(quantity);
				item.setPdPrice(price);

				// **將整張訂單的總金額設置到每個細項的 PD_TOTAL_PRICE**
				item.setPdTotalPrice(totalPrice);

				// 保存細項
				pdoItemRepository.save(item);

				// 更新商品庫存
				pdDetailsRepository.save(product);
			}

			// 4. 更新訂單的總金額
			savedOrder.setPdTotalPrice(totalPrice);
			this.repository.save(savedOrder);

			return savedOrder.getPdoId();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error creating order with items: " + e.getMessage());
			return -1; // 返回一個預設錯誤值
		}
	}

	public List<PdoVO> getAll() {
		return this.repository.findAll();
//        return repository.findAllWithCartDetails();

	}

	// 查詢單一訂單
	public PdoVO getOnePdo(Integer pdoId) {
		return this.repository.findById(pdoId).orElse(null);
	}

	// 更新訂單
	public void updatePdo(PdoVO pdoVO) {
		this.repository.save(pdoVO);
	}

	public PdoVO getPdoForUpdate(Integer pdoId) {
		// 查詢訂單
		Optional<PdoVO> optionalPdo = this.repository.findById(pdoId);

		// 如果訂單不存在，拋出異常
		if (!optionalPdo.isPresent()) {
			throw new IllegalArgumentException("未找到對應的訂單，訂單編號：" + pdoId);
		}

		// 返回訂單物件
		return optionalPdo.get();
	}

	@Transactional
	public void updateOrder(Integer pdoId, String pdoStatus, String shippingAddr, String shippingMethod,
			String shippingStatus) {
		// 查詢訂單
		Optional<PdoVO> optionalPdo = this.repository.findById(pdoId);

		if (!optionalPdo.isPresent()) {
			throw new IllegalArgumentException("未找到對應的訂單，訂單編號：" + pdoId);
		}

		// 更新訂單屬性
		PdoVO pdoVO = optionalPdo.get();
		pdoVO.setPdoStatus(pdoStatus);
		pdoVO.setShippingAddr(shippingAddr);
		pdoVO.setShippingMethod(shippingMethod);
		pdoVO.setShippingStatus(shippingStatus);

		// 自動更新修改時間
		pdoVO.setPdoUpdateTime(java.sql.Timestamp.valueOf(LocalDateTime.now()));

		// 保存到資料庫
		this.repository.save(pdoVO);
	}

	public MebVO getMemberInfoByOrderId(Integer pdoId) {
		// 查詢 PdoVO
		PdoVO pdo = this.repository.findById(pdoId)
				.orElseThrow(() -> new IllegalArgumentException("無效的訂單編號: " + pdoId));
		// 從 PdoVO 中獲取關聯的 MebVO
		return pdo.getMebVO();
	}

	@Transactional
	public PdoVO createOrderWithItems(PdoVO order, List<PdoItemVO> items) {
		// 保存訂單主表
		PdoVO savedOrder = this.repository.save(order);

		// 保存訂單細項
		for (PdoItemVO item : items) {
			item.setPdoVO(savedOrder); // 設置訂單細項與訂單的關聯
			pdoItemRepository.save(item);
		}

		return savedOrder; // 返回已保存的訂單
	}
	
    // 根據訂單 ID 更新 paymentStatus
    @Transactional
    public void updatePaymentStatus(Integer pdoId, String paymentStatus) {
        PdoVO pdo = this.repository.findById(pdoId)
                .orElseThrow(() -> new IllegalArgumentException("找不到該筆訂單: " + pdoId));
        pdo.setPaymentStatus(paymentStatus); // 更新狀態
        this.repository.save(pdo);             // 儲存更新後的訂單
    }


}