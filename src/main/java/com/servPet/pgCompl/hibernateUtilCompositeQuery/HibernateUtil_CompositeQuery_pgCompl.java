package com.servPet.pgCompl.hibernateUtilCompositeQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.servPet.pgCompl.model.PgComplVO;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.Query;

import java.time.LocalDateTime;
import java.util.*;

public class HibernateUtil_CompositeQuery_pgCompl {

    // 這個方法根據欄位名稱和值生成對應的 Predicate
	public static List<PgComplVO> getAllC(Map<String, String[]> map, Session session) {
	    Transaction tx = session.beginTransaction();
	    List<PgComplVO> list = null;
	    try {
	        // 1. 創建 CriteriaBuilder
	        CriteriaBuilder builder = session.getCriteriaBuilder();
	        // 2. 創建 CriteriaQuery
	        CriteriaQuery<PgComplVO> criteriaQuery = builder.createQuery(PgComplVO.class);
	        // 3. 創建 Root
	        Root<PgComplVO> root = criteriaQuery.from(PgComplVO.class);

	        // 用來保存所有查詢條件的 Predicate 列表
	        List<Predicate> predicateList = new ArrayList<>();

	        // 4. 根據 map 中的條件來動態生成 Predicate
	        Set<String> keys = map.keySet();
	        for (String key : keys) {
	            String[] values = map.get(key);
	            if (values != null && values.length > 0) {
	                String value = values[0].trim();
	                
	                // 忽略 action 參數
	                if ("action".equals(key)) continue;
	                
	                if ("pgId".equals(key) && !value.isEmpty()) {
	                    // 依據美容師編號篩選
	                    predicateList.add(builder.equal(root.get("pgId"), Integer.valueOf(value)));
	                } else if ("mebId".equals(key) && !value.isEmpty()) {
	                    // 依據會員編號篩選
	                    predicateList.add(builder.equal(root.get("mebId"), Integer.valueOf(value)));
	                } else if ("startDate".equals(key) && !value.isEmpty()) {
	                    // 依據起始日期篩選
	                    LocalDateTime startDate = LocalDateTime.parse(value);
	                    predicateList.add(builder.greaterThanOrEqualTo(root.get("pgComplDate"), startDate));
	                } else if ("endDate".equals(key) && !value.isEmpty()) {
	                    // 依據結束日期篩選
	                    LocalDateTime endDate = LocalDateTime.parse(value);
	                    predicateList.add(builder.lessThanOrEqualTo(root.get("pgComplDate"), endDate));
	                } else if ("pgComplStatus".equals(key) && !value.isEmpty()) {
	                    // 依據檢舉狀態篩選
	                    predicateList.add(builder.equal(root.get("pgComplStatus"), value));
	                }
	            }
	        }

	        // 5. 應用所有查詢條件
	        if (!predicateList.isEmpty()) {
	            criteriaQuery.where(predicateList.toArray(new Predicate[0]));
	        }
	        
	        // 6. 默認按 pgComplId 升序排序
	        criteriaQuery.orderBy(builder.asc(root.get("pgComplId")));

	        // 7. 執行查詢並獲取結果
	        Query query = session.createQuery(criteriaQuery);
	        list = query.getResultList();

	        tx.commit();
	    } catch (RuntimeException ex) {
	        if (tx != null) tx.rollback();
	        throw ex;
	    } finally {
	        session.close();
	    }

	    return list;
	}
}
