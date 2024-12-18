package com.servPet.psSvcItem.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("PsSvcItemService")
//所有服務項目(提供幾項基本服務可以勾選，並且提供保母自行額外增加刪除服務項目)
public class PsSvcItemService {
    @Autowired
    private PsSvcItemRepository psSvcItemRepository;

    //查詢全部
    public List<PsSvcItemVO> getAll(){
        return psSvcItemRepository.findAll();
    }


    //新增一筆資料
    public PsSvcItemVO addPsSvcItem(PsSvcItemVO psSvcItemVO) {  //用void?

        return psSvcItemRepository.save(psSvcItemVO);
    }

    //刪除一筆資料(根據SVC_ID)
    public void deletePsSvcItemById(Integer svcId) {
        if (psSvcItemRepository.existsById(svcId)) {
            psSvcItemRepository.deleteById(svcId);
        } else {
            throw new IllegalArgumentException("資料不存在，無法刪除");
        }
    }

    public List<PsSvcItemVO> getAllPsSvcItems() {
        return List.of();
    }

    public PsSvcItemVO getPsSvcItemById(Integer svcId) {
        return psSvcItemRepository.findById(svcId)
                .orElseThrow(() -> new IllegalArgumentException("資料不存在"));
    }

    public void deletePsSvcItem(Integer svcId) {
        if (psSvcItemRepository.existsById(svcId)) {
            psSvcItemRepository.deleteById(svcId);
        } else {
            throw new IllegalArgumentException("資料不存在，無法刪除");
        }
    }


    public PsSvcItemVO getSvcItemById(Integer svcId) {
        // 假設你有一個 Repository 層處理數據庫查詢
        return psSvcItemRepository.findById(svcId).orElse(null);
    }

}
