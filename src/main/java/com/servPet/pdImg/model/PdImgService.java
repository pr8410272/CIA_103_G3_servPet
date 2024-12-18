package com.servPet.pdImg.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servPet.pdDetails.model.PdDetailsVO;

@Service("pdImgService")
public class PdImgService {

	@Autowired
	PdImgRepository repository;

	@Autowired
	private SessionFactory sessionFactory;

	public void addPdImg(PdImgVO pdImgVO) {
		repository.save(pdImgVO);
	}

	public void updatePdImg(PdImgVO pdImgVO) {
		repository.save(pdImgVO);
	}

	public PdImgVO getOnePdImg(Integer pdImgId) {
		Optional<PdImgVO> optional = repository.findById(pdImgId);
//		return optional.get();
		return optional.orElse(null); // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<PdImgVO> getImagesByProductId(Integer pdId) {
		PdDetailsVO pdDetailsVO = new PdDetailsVO();
		pdDetailsVO.setPdId(pdId); // 設定 pdId
		return this.repository.findByPdDetailsVO(pdDetailsVO);
	}

	public List<PdImgVO> getAll() {
		return repository.findAll();
	}

	// 刪除圖片邏輯
	public void deleteImageById(Integer pdImgId) {
		if (!this.repository.existsById(pdImgId)) {
			throw new RuntimeException("圖片不存在");
		}
		this.repository.deleteById(pdImgId); // 刪除操作
	}

	public byte[] getImageData(Integer pdImgId) {
		return this.repository.findById(pdImgId).map(pdImgVO -> pdImgVO.getPdImg()) // 獲取圖片數據
				.orElse(null); // 如果找不到，返回 null
	}

//	public Integer getDefaultImgIdByProductId(Integer pdId) {
//		// 查詢 PD_IMG_ID 最小值
////		return this.repository.findMinPdImgIdByPdId(pdId).orElse(null);
//		 return repository.findMinPdImgIdByPdId(pdId).orElse(0);
//	}
	
	public Integer getDefaultImgIdByProductId(Integer pdId) {
	    // 使用 Optional 的 .orElse() 方法，若無值則回傳 0 (預設圖片 ID)
	    Integer imgId = repository.findMinPdImgIdByPdId(pdId).orElse(0);
	    
	    // Log 確認每個商品的處理結果
	    System.out.println("Product ID: " + pdId + ", Default Image ID: " + imgId);
	    
	    return imgId;
	}



}