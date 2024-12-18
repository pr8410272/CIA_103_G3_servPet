package com.servPet.vtr.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class VtrManagerRepositoryCustomImpl implements VtrManagerRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<VtrVO> findByCriteria(Integer mebId, LocalDateTime startTime, LocalDateTime endTime, Integer minMoney, Integer maxMoney, String transactionType, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<VtrVO> cq = cb.createQuery(VtrVO.class);
        Root<VtrVO> root = cq.from(VtrVO.class);

        List<Predicate> predicates = new ArrayList<>();

        if (mebId != null) {
            predicates.add(cb.equal(root.get("mebId"), mebId));
        }

        if (startTime != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createTime"), startTime));
        }

        if (endTime != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createTime"), endTime));
        }

        if (minMoney != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("money"), minMoney));
        }

        if (maxMoney != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("money"), maxMoney));
        }

        if (transactionType != null && !transactionType.isEmpty() && !transactionType.equals("全部")) {
            // 映射前端的交易類型到資料庫的值
            Map<String, String> transactionTypeMapping = Map.of(
                "儲值", "儲值",
                "扣款", "扣款",
                "退款", "退款"

            );
            String mappedTransactionType = transactionTypeMapping.get(transactionType);
            if (mappedTransactionType != null) {
                predicates.add(cb.equal(root.get("transactionType"), mappedTransactionType));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<VtrVO> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // 獲取總數
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<VtrVO> countRoot = countQuery.from(VtrVO.class);
        countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(query.getResultList(), pageable, total);
    }

}
