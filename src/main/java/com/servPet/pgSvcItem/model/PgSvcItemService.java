package com.servPet.pgSvcItem.model;
//美容服務總覽

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PgSvcItemService {

	@Autowired
	private PgSvcItemRepository pgSvcItemRepository;
	


	//真刪除
	public void deletePgSvcItemById(Integer svcId) {
		if (pgSvcItemRepository.existsById(svcId)) {
			pgSvcItemRepository.deleteById(svcId);
		} else {
			throw new IllegalArgumentException("資料不存在，無法刪除");
		}
	}

	// 查看"已顯示"的所有服務項目
	public List<PgSvcItemVO> getAllVisible() {
		return pgSvcItemRepository.findBySvcIsDeleted("1"); // 只查詢已顯示的項目
	}

	public PgSvcItemVO getServiceByName(String svcName) {
		return pgSvcItemRepository.findBySvcName(svcName).orElse(null);
	}

	public PgSvcItemVO getById(Integer svcId) { // 通過這個工具，可以更加安全和清晰地處理返回值為空的情況
		Optional<PgSvcItemVO> optional = pgSvcItemRepository.findById(svcId);
		return optional.orElse(null); // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	// 更新
	public void updatePgSvcItem(PgSvcItemVO pgSvcItemVO) {
		PgSvcItemVO updateItem = pgSvcItemRepository.findById(pgSvcItemVO.getSvcId())
				.orElseThrow(() -> new IllegalArgumentException("找不到該服務項目"));

		// 更新數據
		updateItem.setSvcName(pgSvcItemVO.getSvcName());
		updateItem.setSvcDescr(pgSvcItemVO.getSvcDescr());
		pgSvcItemRepository.save(updateItem); // 保存更新
	}

	// 新增服務項目
	public void createPgSvcItem(PgSvcItemVO pgSvcItemVO) {
		pgSvcItemRepository.save(pgSvcItemVO);
	}

	// 查看所有服務項目(含已隱藏)
	public List<PgSvcItemVO> getRealAll() {
		List<PgSvcItemVO> list = pgSvcItemRepository.findAll();
		return list;
	}

	// 一次更新服務項目(顯示/隱藏)
	@Transactional
	public void updateAllSvcItemStatus(String svcIsDeleted) {
	    pgSvcItemRepository.updateAllSvcItemStatus(svcIsDeleted);
	    }
	    
	// 更新服務項目狀態(顯示/隱藏)
	public void updateSvcItemStatus(Integer svcId, String svcIsDeleted) {
		PgSvcItemVO item = pgSvcItemRepository.findById(svcId)
				.orElseThrow(() -> new IllegalArgumentException("找不到對應的服務項目"));

		// 更新狀態
		item.setSvcIsDeleted(svcIsDeleted);
		pgSvcItemRepository.save(item); // 保存到資料庫
	}

}
