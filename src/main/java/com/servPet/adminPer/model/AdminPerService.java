package com.servPet.adminPer.model;

import com.servPet.admin.model.AdminService;
import com.servPet.admin.model.AdminVO;
import com.servPet.fnc.model.FncVO;
import com.servPet.adminPer.hibernateUtilCompositeQuery.HibernateUtil_CompositeQuery_adminPer;
import com.servPet.fnc.model.FncService;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("adminPerService")
public class AdminPerService {

    @Autowired
    private AdminPerRepository adminPerRepository;

    @Autowired
    private AdminService adminService;  // 假設有一個 AdminService 來處理管理員相關邏輯

    @Autowired
    private FncService fncService;  // 假設有一個 FncService 來處理功能相關邏輯
    
    @Autowired
    private SessionFactory sessionFactory;

    // 新增管理員權限
    public void addAdminPer(AdminPerVO adminPerVO) {
        adminPerRepository.save(adminPerVO);  // 使用 JPA adminPerRepository 進行儲存
    }

    // 更新管理員權限
    public void updateAdminPer(AdminPerVO adminPerVO) {
        adminPerRepository.save(adminPerVO);  // 使用 JPA adminPerRepository 進行儲存
    }

    // 刪除管理員的權限
    public void deleteAdminPer(Integer adminPerId) {
        if (adminPerRepository.existsById(adminPerId)) {
            adminPerRepository.deleteById(adminPerId);  // 根據 adminPerId 刪除
        }
    }

    // 根據 adminPerId 查詢單一管理員權限
    public AdminPerVO getOneAdminPer(Integer adminPerId) {
        Optional<AdminPerVO> optional = adminPerRepository.findById(adminPerId);
        return optional.orElse(null);  // 如果存在，返回該權限，否則返回 null
    }

    // 查詢所有管理員權限資料
    public List<AdminPerVO> getAll() {
        return adminPerRepository.findAll();  // 返回所有管理員權限資料
    }

    // 查詢特定管理員的所有權限
    public List<AdminPerVO> getPerByAdminId(int adminId) {
        return adminPerRepository.findByAdminId(adminId);  // 查詢指定管理員的權限
    }

    // 查詢特定權限功能的所有管理員
    public List<AdminPerVO> getPerByFncId(int fncId) {
        return adminPerRepository.findByFncId(fncId);  // 查詢指定功能的管理員權限
    }

    // 查詢特定管理員對特定權限功能的資料
    public AdminPerVO getPerByAdminAndFnc(int adminId, int fncId) {
        AdminVO admin = adminService.getOneAdmin(adminId);  // 假設有一個方法來根據 adminId 查找管理員
        FncVO fnc = fncService.getOneFnc(fncId);  // 假設有一個方法來根據 fncId 查找功能
        return adminPerRepository.findByAdminAndFnc(admin, fnc);  // 查詢指定管理員對特定功能的權限資料
    }

//    // 刪除某個管理員的所有權限
//    public void deletePerByAdminId(int adminId) {
//        adminPerRepository.deleteByAdminId(adminId);  // 根據 adminId 刪除所有相關權限
//    }

    // 複合查詢所有管理員權限資料
    public List<AdminPerVO> getAll(Map<String, String[]> map) {
        return HibernateUtil_CompositeQuery_adminPer.getAllC(map, sessionFactory.openSession());  // 使用 Hibernate 進行複合查詢
    }
    
}
