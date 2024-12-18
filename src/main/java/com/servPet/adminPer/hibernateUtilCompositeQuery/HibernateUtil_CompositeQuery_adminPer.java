package com.servPet.adminPer.hibernateUtilCompositeQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.Query;
import java.util.*;

import com.servPet.adminPer.model.AdminPerVO;

public class HibernateUtil_CompositeQuery_adminPer {

    // 根據欄位名稱和查詢值生成對應的 Predicate
    public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<AdminPerVO> root, String columnName, String value) {
        Predicate predicate = null;

        // 檢查查詢值是否為空或 null，若是，則不生成 Predicate
        if (value == null || value.trim().isEmpty()) {
            return null; // 查詢值為空，直接返回 null
        }

        // 根據不同的欄位生成對應的查詢條件
        if ("adminPerId".equals(columnName)) {  // 用於 Integer 類型
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        } else if ("adminId".equals(columnName)) {  // 假設 AdminPerVO 和 AdminVO 之間有 ManyToOne 關係
            predicate = builder.equal(root.get("adminVO").get("adminId"), Integer.valueOf(value));
        } else if ("fncId".equals(columnName)) {  // 假設 AdminPerVO 和 FncVO 之間有 ManyToOne 關係
            predicate = builder.equal(root.get("fncVO").get("fncId"), Integer.valueOf(value));
        }

        return predicate;
    }

    // 根據傳入的參數映射生成動態查詢
    @SuppressWarnings("unchecked")
    public static List<AdminPerVO> getAllC(Map<String, String[]> map, Session session) {
        Transaction tx = session.beginTransaction();
        List<AdminPerVO> list = null;
        try {
            // 1. 創建 CriteriaBuilder
            CriteriaBuilder builder = session.getCriteriaBuilder();
            // 2. 創建 CriteriaQuery
            CriteriaQuery<AdminPerVO> criteriaQuery = builder.createQuery(AdminPerVO.class);
            // 3. 創建 Root
            Root<AdminPerVO> root = criteriaQuery.from(AdminPerVO.class);

            // 用來存放所有查詢條件 (predicates)
            List<Predicate> predicateList = new ArrayList<Predicate>();

            // 取得 map 中的所有鍵，這些鍵對應到需要篩選的欄位
            Set<String> keys = map.keySet();
            int count = 0;

            // 遍歷 map，根據每個篩選條件生成 Predicate
            for (String key : keys) {
                String value = map.get(key)[0];
                if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
                    count++;
                    // 根據欄位和查詢值生成 Predicate 並加入到條件列表中
                    Predicate predicate = get_aPredicate_For_AnyDB(builder, root, key, value.trim());
                    if (predicate != null) {
                        predicateList.add(predicate);
                    }
                    System.out.println("發送的過濾條件數量: count = " + count);
                }
            }

            // 輸出條件列表的大小
            System.out.println("Predicate 列表大小: " + predicateList.size());

            // 如果 predicateList 不為空，則將條件應用到 CriteriaQuery
            if (!predicateList.isEmpty()) {
                criteriaQuery.where(predicateList.toArray(new Predicate[0]));
            }

            // 預設依據 adminPerId 進行升序排序
            criteriaQuery.orderBy(builder.asc(root.get("adminPerId")));

            // 4. 創建並執行查詢
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