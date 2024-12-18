package com.servPet.admin.hibernateUtilCompositeQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.criteria.*;
import javax.persistence.Query;
import java.util.*;

import com.servPet.admin.model.AdminVO;

public class HibernateUtil_CompositeQuery_admin {

    // 動態生成 Predicate，根據欄位名稱和查詢值，生成對應的查詢條件
	public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<AdminVO> root, String columnName, String value) {
	    Predicate predicate = null;

	    if (value == null || value.trim().isEmpty()) {
	        // 如果查詢值為空，則返回 null，避免生成不必要的 Predicate
	        return null;
	    }

	    // 根據不同的欄位進行查詢條件的生成
	    if ("adminId".equals(columnName)) { // 用於 Integer 類型
	        predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
	    } else if ("adminName".equals(columnName) || "adminAccStatus".equals(columnName) || "adminAcc".equals(columnName) || "adminRole".equals(columnName)) { // 用於 String 類型
	        predicate = builder.like(root.get(columnName), "%" + value + "%");
	    } 

	    return predicate;
	}

	@SuppressWarnings("unchecked")
	public static List<AdminVO> getAllC(Map<String, String[]> map, Session session) {
	    Transaction tx = session.beginTransaction();
	    List<AdminVO> list = null;
	    try {
	        // 創建 CriteriaBuilder 和 CriteriaQuery
	        CriteriaBuilder builder = session.getCriteriaBuilder();
	        CriteriaQuery<AdminVO> criteriaQuery = builder.createQuery(AdminVO.class);
	        Root<AdminVO> root = criteriaQuery.from(AdminVO.class);

	        List<Predicate> predicateList = new ArrayList<>();

	        Set<String> keys = map.keySet();
	        for (String key : keys) {
	            String value = map.get(key)[0];
	            // 只有當 value 不為空且不是 "action" 時，才生成 Predicate
	            if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
	                Predicate predicate = get_aPredicate_For_AnyDB(builder, root, key, value.trim());
	                if (predicate != null) {
	                    predicateList.add(predicate);
	                }
	            }
	        }

	        // 如果 predicateList 不為空，則加入 where 條件
	        if (!predicateList.isEmpty()) {
	            criteriaQuery.where(predicateList.toArray(new Predicate[0]));
	        }

	        // 排序依照 adminId
	        criteriaQuery.orderBy(builder.asc(root.get("adminId")));
	        
	        // 執行查詢
	        Query query = session.createQuery(criteriaQuery);
	        list = query.getResultList();

	        tx.commit();
	    } catch (RuntimeException ex) {
	        if (tx != null) {
	            tx.rollback();
	        }
	        throw ex;
	    } finally {
	        session.close();
	    }

	    return list;
	}
}