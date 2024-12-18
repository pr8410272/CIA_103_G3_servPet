package com.servPet.pdDetails.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.servPet.pdCateg.model.PdCategVO;
import com.servPet.pdImg.model.PdImgRepository;
import com.servPet.pdImg.model.PdImgService;
import com.servPet.pdImg.model.PdImgVO;

@Service("pdDetailsService")
@Transactional
public class PdDetailsService {

	@Autowired
	PdDetailsRepository repository;

	@Autowired
	PdImgRepository pdImgRepository;

	@Autowired
	private PdImgService pdImgSvc;

	@Autowired
	private SessionFactory sessionFactory;

	public PdDetailsService() {
	}

	@Transactional
	public void addPdDetails(PdDetailsVO pdDetailsVO, MultipartFile[] images) throws IOException {
		// 1. 儲存商品資料
		pdDetailsVO.setCreatedAt(LocalDateTime.now());
		pdDetailsVO.setUpdatedAt(LocalDateTime.now());
		PdDetailsVO savedProduct = repository.save(pdDetailsVO);

		// 2. 處理圖片
		if (images != null && images.length > 0 && images.length < 5) {
			List<PdImgVO> imageList = new ArrayList<>();
			for (MultipartFile image : images) {
				if (!image.isEmpty()) {
					PdImgVO pdImgVO = new PdImgVO();
					pdImgVO.setPdDetailsVO(savedProduct); // 關聯商品
					pdImgVO.setPdImg(image.getBytes()); // 儲存圖片二進位數據
					pdImgVO.setImgDesc(image.getOriginalFilename()); // 圖片名稱
					pdImgVO.setUpdatedAt(new Date()); // 設置更新時間
					imageList.add(pdImgVO);
				}
			}
			// 儲存圖片到 `PRODUCT_IMAGES` 表
			pdImgRepository.saveAll(imageList);
		}
	}

	public void updateProductWithImages(PdDetailsVO pdDetailsVO, MultipartFile[] parts, List<Integer> imgIdsToDelete)
			throws IOException {
		// 1. 更新商品基本資料
		pdDetailsVO.setUpdatedAt(LocalDateTime.now());
		repository.save(pdDetailsVO); // 假設您使用的是 JpaRepository

		// 2. 刪除選擇的圖片
		if (imgIdsToDelete != null && !imgIdsToDelete.isEmpty()) {
			List<PdImgVO> imagesToDelete = pdImgRepository.findAllById(imgIdsToDelete);
			if (!imagesToDelete.isEmpty()) {
				pdImgRepository.deleteAll(imagesToDelete);
			}
		}

		// 3. 處理新圖片數據
		if (parts != null && parts.length > 0 && parts.length <= 4) {
			List<PdImgVO> newImages = new ArrayList<>();
			for (MultipartFile file : parts) {
				if (!file.isEmpty()) {
					PdImgVO imgVO = new PdImgVO();
					imgVO.setPdDetailsVO(pdDetailsVO); // 建立與商品的關聯
					imgVO.setPdImg(file.getBytes()); // 保存圖片二進制數據
					imgVO.setImgDesc(file.getOriginalFilename()); // 圖片名稱
					imgVO.setUpdatedAt(new Date()); // 設置更新時間
					newImages.add(imgVO);
				}
			}

			// 4. 保存新圖片數據
			if (!newImages.isEmpty()) {
				pdImgRepository.saveAll(newImages);
			}
		}
	}

//	for deleting single product
	public void deleteProduct(Integer pdId) {
		if (this.repository.existsById(pdId)) {
			this.repository.deleteById(pdId);
		}
	}

//	for deleting multiple products
	public void deleteBatchProducts(List<PdDetailsVO> pdDetailsList) {
		if (pdDetailsList != null && !pdDetailsList.isEmpty()) {
			this.repository.deleteAll(pdDetailsList);
		} else {
			throw new IllegalArgumentException("Product list is empty or null");
		}
	}

	public PdDetailsVO getOneProduct(Integer pdId) {
		return this.repository.findByIdWithDetailsAndImages(pdId).orElseThrow(() -> new RuntimeException("商品未找到"));
	}

	public List<PdDetailsVO> getAll() {
		return this.repository.findAll();
//        return repository.findAllWithCartDetails();

	}
	
	   // 獲取所有商品並補充預設分類
    public List<PdDetailsVO> getAllProductsWithDefaultImages() {
        List<PdDetailsVO> products = repository.findAll();
        for (PdDetailsVO product : products) {
            if (product.getPdCategVO() == null) {
                PdCategVO defaultCategory = new PdCategVO();
                defaultCategory.setPdCategory(0); // 默認分類 ID
                defaultCategory.setCategoryName("無分類");
                product.setPdCategVO(defaultCategory);
            }
        }
        return products;
    }

    // 獲取商品預設圖片映射
    public Map<Integer, Integer> getDefaultImagesMap(List<PdDetailsVO> products) {
        Map<Integer, Integer> defaultImages = new HashMap<>();
        for (PdDetailsVO product : products) {
            Integer defaultImgId = pdImgSvc.getDefaultImgIdByProductId(product.getPdId());
            defaultImages.put(product.getPdId(), defaultImgId != null ? defaultImgId : 0);
        }
        return defaultImages;
    }
    
    /**
     * 按商品狀態分組，返回分組數據
     */
    public Map<String, List<PdDetailsVO>> getProductsGroupedByStatus() {
        Map<String, List<PdDetailsVO>> groupedByStatus = new HashMap<>();

        groupedByStatus.put("下架", this.repository.findByPdStatus("0"));
        groupedByStatus.put("上架中", this.repository.findByPdStatus("1"));
        groupedByStatus.put("停售", this.repository.findByPdStatus("2"));
        groupedByStatus.put("測試", this.repository.findByPdStatus("3"));

        return groupedByStatus;
    }
    
    public List<PdDetailsVO> findProductsByIds(List<Integer> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("商品 ID 列表不可為空");
        }
        return repository.findAllById(productIds);
    }
    
//    public void updateProductStatusByQuantity() {
//        // 查詢所有商品
//        List<PdDetailsVO> products = this.repository.findAll();
//
//        // 遍歷商品，檢查庫存數量
//        for (PdDetailsVO product : products) {
//            if (product.getPdQty() < 5) {
//                product.setPdStatus("2"); // 將商品狀態設為 "2" (停售)
//            }
//        }
//        
//        // 保存更新後的商品狀態
//        this.repository.saveAll(products);
//    }
	
}