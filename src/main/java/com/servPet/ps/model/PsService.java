package com.servPet.ps.model;


import com.servPet.ps.model.PsRepository;
import com.servPet.psPor.model.PsPorVO;
import com.servPet.psSvcItem.model.PsSvcItemRepository;
import com.servPet.psSvcItem.model.PsSvcItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service("PsService")
public class PsService {

    @Autowired
    private PsRepository psRepository;
    @Autowired
    private PsSvcItemRepository psSvcItemRepository;

    // 新增
    public void addPs(PsVO psVO) {
        psRepository.save(psVO);
    }

    // 修改
//    public void update(PsVO psVO) {
//        repository.save(psVO);
//    }

    public PsVO findById(Integer psId) {
        return psRepository.findById(psId).orElse(null);
    }
    @Transactional
    public void update(PsVO psVO) {
        psRepository.save(psVO);
    }




    // 刪除
    public void deletePs(Integer psId) {
        if (psRepository.existsById(psId)) {
            psRepository.deleteById(psId);
        }
    }

    // 查詢所有
    public List<PsVO> getAll() {
        return psRepository.findAll();
    }

    // 根據 ID 查詢
    public PsVO getOnePs(Integer psId) {
        Optional<PsVO> optional = psRepository.findById(psId);
        return optional.orElse(null);
    }


    public PsSvcItemVO getSvcItemById(Integer svcId) {
        // 假設你有一個 Repository 層處理數據庫查詢
        return psSvcItemRepository.findById(svcId).orElse(null);
    }



    // 根據條件查詢
//    public List<PsVO> searchPs(Integer psId, String psName, String psArea, Integer totalStars, Integer ratingTimes, Integer reportTimes, byte[] psPic) {
//        return psRepository.findByPsIdOrPsNameContainingOrPsAreaContainingOrTotalStarsOrRatingTimesOrReportTimesOrPsPic(
//                psId, psName, psArea, totalStars, ratingTimes, reportTimes, psPic
//        );
//    }
    
    
 // 管理員登錄(學雍)
    public PsVO login(Integer integer, String psPw) {
    	PsVO ps = psRepository.findByPsId(integer);
        if (ps != null && psPw.equals(ps.getPsPw())) {  // 直接比對密碼
            return ps; // 驗證成功
        } else {
            return null; // 登入失敗
        }

    }
}
