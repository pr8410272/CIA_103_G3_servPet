package com.servPet.pgCompl.model;

import com.servPet.pgCompl.hibernateUtilCompositeQuery.HibernateUtil_CompositeQuery_pgCompl;
import com.servPet.pgCompl.model.PgComplRepository;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("pgComplService")
public class PgComplService {

    @Autowired
    private PgComplRepository pgComplRepository;

    @Autowired
    private SessionFactory sessionFactory;

    // 新增檢舉 (Add a new complaint)
    public void addPgCompl(PgComplVO pgComplVO) {
        pgComplRepository.save(pgComplVO);  // 使用 JPA pgComplRepository 進行儲存
    }

    // 處理文件上傳 (Handle file upload)
    public void handleFileUpload(PgComplVO pgComplVO, MultipartFile[] files) throws IOException {
        // 檢查每個檔案並將它們存儲到對應的屬性中
        for (int i = 0; i < files.length; i++) {
            if (files[i] != null && !files[i].isEmpty()) {
                byte[] fileBytes = files[i].getBytes();  // 將檔案轉換為 byte[]
                switch (i) {
                    case 0:
                        pgComplVO.setPgComplUpfiles1(fileBytes);
                        break;
                    case 1:
                        pgComplVO.setPgComplUpfiles2(fileBytes);
                        break;
                    case 2:
                        pgComplVO.setPgComplUpfiles3(fileBytes);
                        break;
                    case 3:
                        pgComplVO.setPgComplUpfiles4(fileBytes);
                        break;
                    default:
                        break;  // 可處理更多檔案
                }
            }
        }
    }

    // 更新檢舉 (Update a complaint)
    @Transactional
    public void updatePgCompl(PgComplVO pgComplVO) {
        // 确保数据库中相关的 `pgComplDate` 字段在更新时不丢失
        pgComplRepository.save(pgComplVO);  // 使用 JPA pgComplRepository 進行儲存
    }

    // 根據 PG_COMPL_ID 查詢單一檢舉 (Get one complaint by PG_COMPL_ID)
    public PgComplVO getOnePgCompl(Integer pgComplId) {
        Optional<PgComplVO> optional = pgComplRepository.findById(pgComplId);
        return optional.orElse(null);  // 如果存在，返回該檢舉，否則返回 null
    }
    
 // 查詢所有美容師資料
    public List<PgComplVO> getAll() {
        return pgComplRepository.findAll();  // 返回所有管理員資料
    }

    // 複合查詢所有美容師資料
    public List<PgComplVO> getAll(Map<String, String[]> map) {
        return HibernateUtil_CompositeQuery_pgCompl.getAllC(map, sessionFactory.openSession());
    }
}

