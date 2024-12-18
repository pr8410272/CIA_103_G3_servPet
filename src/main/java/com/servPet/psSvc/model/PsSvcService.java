package com.servPet.psSvc.model;

import com.servPet.ps.model.PsRepository;
import com.servPet.ps.model.PsVO;
import com.servPet.psSvcItem.model.PsSvcItemRepository;
import com.servPet.psSvcItem.model.PsSvcItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("PsSvcService")
//保母個人的服務清單(讓會員可以依照保母編號、服務項目、價格來單筆或複合查詢)
public class PsSvcService {
    @Autowired
    private PsSvcRepository psSvcRepository;

    @Autowired
    PsRepository psRepository;

    @Autowired
    PsSvcItemRepository psSvcItemRepository;

    public List<PsSvcVO> getAll() {
        return psSvcRepository.findAll();
    }

    // 根據保母 ID 更新其服務項目
    public void updateServicesForSitter(Integer psId, List<Integer> svcIds, List<Integer> svcPrices) {
        // 驗證輸入
        if (svcIds == null || svcPrices == null || svcIds.size() != svcPrices.size()) {
            throw new IllegalArgumentException("Invalid input: svcIds and svcPrices must be non-null and of the same size");
        }

        // 加載保母和已有的服務項目
        PsVO psVO = psRepository.findById(psId).orElse(null);
        if (psVO == null) return;
        List<PsSvcVO> psSvcList = psSvcRepository.getPsSvcItemVOByPsSvcVO(psId);

        // 計算已有的服務項目
        Set<Integer> hasSvc = psSvcList.stream()
                .map(a -> a.getPsSvcId().getPsSvcItemVO().getSvcId())
                .collect(Collectors.toSet());

        // 計算需要刪除和新增的項目
        Set<Integer> removeIds = new HashSet<>(hasSvc);
        removeIds.removeAll(svcIds);
        Set<Integer> newIds = new HashSet<>(svcIds);
        newIds.removeAll(hasSvc);

        // 加載所有需要的 PsSvcItemVO
        List<PsSvcItemVO> psSvcItems = psSvcItemRepository.findAllById(svcIds);
        Map<Integer, PsSvcItemVO> psSvcItemMap = psSvcItems.stream()
                .collect(Collectors.toMap(PsSvcItemVO::getSvcId, item -> item));

        PsSvcVO psSvcVO = new PsSvcVO();
        // 新增或更新服務項目
        for (int i = 0; i < svcIds.size(); i++) {
            Integer svcId = svcIds.get(i);
            if (svcId != null) {
                PsSvcItemVO psSvcItemVO = psSvcItemMap.get(svcId);
                if (psSvcItemVO != null) {
                    PsSvcId psSvcId = new PsSvcId(psVO, psSvcItemVO);
                    psSvcVO.setPsSvcId(psSvcId);
                    psSvcVO.setSvcPrice(svcPrices.get(i));
                    psSvcRepository.save(psSvcVO);
                }
            }
        }

        // 刪除多餘的服務項目
        for (Integer svcId : removeIds) {
            PsSvcItemVO psSvcItemVO = new PsSvcItemVO();
            psSvcItemVO.setSvcId(svcId);
            System.out.println("psSvcItemVO " + psSvcItemVO);
            if (psSvcItemVO != null) {
                PsSvcId psSvcId = new PsSvcId(psVO, psSvcItemVO);
                psSvcVO.setPsSvcId(psSvcId);
                System.out.println(psSvcId);
                psSvcRepository.delete(psSvcVO);
            }
        }



//        // 指定服務清單編號的詳細資訊
//        public PsServiceDetailsDTO findByPsSvcId_PsSvcItemVO_SvcId(PsSvcId) {
//            List<Object[]> result = psSvcRepository.findByPsSvcId_PsSvcItemVO_SvcId(svcId);
//            PsServiceDetailsDTO psServiceDetailsDTO = new PsServiceDetailsDTO();
//
//            for (Object[] o : result) {
//                psServiceDetailsDTO.setPsSvcId((Integer) o[0]);
//                psServiceDetailsDTO.setPsId((Integer) o[1]);
//                psServiceDetailsDTO.setSvcId((Integer) o[2]);
//                psServiceDetailsDTO.setSvcType((String) o[3]);
//                psServiceDetailsDTO.setSvcPrice((Integer) o[4]);
//                psServiceDetailsDTO.setSvcName((String) o[5]);
//                psServiceDetailsDTO.setSvcDescr((String) o[6]);
//            }
//
//            return psServiceDetailsDTO;
//        }


    }

    public List<PsSvcVO> getServicesByPsId(Integer psId) {
        return psSvcRepository.findByPsSvcId_PsVO_PsId(psId);
    }

}
