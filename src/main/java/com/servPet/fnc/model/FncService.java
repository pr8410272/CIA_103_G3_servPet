package com.servPet.fnc.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servPet.fnc.hibernateUtilCompositeQuery.HibernateUtil_CompositeQuery_fnc;

@Service("fncService")
public class FncService {

    @Autowired
    private FncRepository fncRepository;

    @Autowired
    private SessionFactory sessionFactory;

    // 新增功能 (Add a new function)
    public void addFnc(FncVO fncVO) {
        fncRepository.save(fncVO);  // 使用 JPA fncRepository 進行儲存
    }

    // 更新功能 (Update a function)
    public void updateFnc(FncVO fncVO) {
        fncRepository.save(fncVO);  // 使用 JPA fncRepository 進行儲存
    }

    // 刪除功能 (Delete a function by FNC_ID)
    public void deleteFnc(Integer fncId) {
        if (fncRepository.existsById(fncId)) {
            fncRepository.deleteById(fncId);  // 根據 fncId 刪除
        }
    }

    // 根據 FNC_ID 查詢單一功能 (Get one function by FNC_ID)
    public FncVO getOneFnc(Integer fncId) {
        Optional<FncVO> optional = fncRepository.findById(fncId);
        return optional.orElse(null);  // 如果存在，返回該功能，否則返回 null
    }

    // 查詢所有功能 (Get all functions)
    public List<FncVO> getAll() {
        return fncRepository.findAll();  // 返回所有功能資料
    }

    // 複合查詢所有功能資料 (Composite query for all functions)
    public List<FncVO> getAll(Map<String, String[]> map) {
        return HibernateUtil_CompositeQuery_fnc.getAllC(map, sessionFactory.openSession());
    }

}