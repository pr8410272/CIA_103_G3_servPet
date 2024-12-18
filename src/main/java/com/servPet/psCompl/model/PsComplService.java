package com.servPet.psCompl.model;


import com.servPet.psCompl.hibernateUtilCompositeQuery.HibernateUtil_CompositeQuery_psCompl;
import com.servPet.psCompl.model.PsComplRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("psComplService")
public class PsComplService {

    @Autowired
    private PsComplRepository psComplRepository;

    @Autowired
    private SessionFactory sessionFactory;

    // 新增檢舉 (Add a new complaint)
    public void addPsCompl(PsComplVO<?> psComplVO) {
        psComplRepository.save(psComplVO);  // 使用 JPA psComplRepository 進行儲存
    }

    // 處理文件上傳 (Handle file upload)
    public void handleFileUpload(PsComplVO<?> psComplVO, MultipartFile[] files) throws IOException {
        // 檢查每個檔案並將它們存儲到對應的屬性中
        for (int i = 0; i < files.length; i++) {
            if (files[i] != null && !files[i].isEmpty()) {
                byte[] fileBytes = files[i].getBytes();  // 將檔案轉換為 byte[]
                switch (i) {
                    case 0:
                        psComplVO.setPsComplUpfiles1(fileBytes);
                        break;
                    case 1:
                        psComplVO.setPsComplUpfiles2(fileBytes);
                        break;
                    case 2:
                        psComplVO.setPsComplUpfiles3(fileBytes);
                        break;
                    case 3:
                        psComplVO.setPsComplUpfiles4(fileBytes);
                        break;
                    default:
                        break;  // 可處理更多檔案
                }
            }
        }
    }

    // 更新檢舉 (Update a complaint)
    @Transactional
    public void updatePsCompl(PsComplVO<?> psComplVO) {
        // 确保数据库中相关的 `psComplDate` 字段在更新时不丢失
        psComplRepository.save(psComplVO);  // 使用 JPA psComplRepository 進行儲存
    }

    // 根據 PS_COMPL_ID 查詢單一檢舉 (Get one complaint by PS_COMPL_ID)
    public PsComplVO<?> getOnePsCompl(Integer psComplId) {
        Optional<PsComplVO> optional = psComplRepository.findById(psComplId);
        return optional.orElse(null);  // 如果存在，返回該檢舉，否則返回 null
    }

 // 查詢所有美容師資料
    public List<PsComplVO> getAll() {
        return psComplRepository.findAll();  // 返回所有管理員資料
    }

    // 複合查詢所有美容師資料
    public List<PsComplVO> getAll(Map<String, String[]> map) {
        return HibernateUtil_CompositeQuery_psCompl.getAllC(map, sessionFactory.openSession());
    }
}

