package com.servPet.fnc.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FncRepository extends JpaRepository<FncVO, Integer> {

    // 根據 FNC_ID 刪除功能
    @Transactional
    @Modifying
    @Query(value = "delete from fnc where fnc_id = ?1", nativeQuery = true)
    void deleteByFncId(int fncId);

    // 根據功能名稱查詢功能
    List<FncVO> findByFncNameContaining(String fncName);

    // 根據功能描述查詢功能
    List<FncVO> findByFncDesContaining(String fncDes);

    // 根據功能名稱和狀態查詢功能
    List<FncVO> findByFncNameAndFncDes(String fncName, String fncDes);

    // 查詢指定日期範圍內的功能
    @Query(value = "from FncVO where fncCreAt >= ?1 and fncUpdAt <= ?2 order by fncCreAt")
    List<FncVO> findByDateRange(String startDate, String endDate);

    // 根據功能名稱模糊查詢
    @Query(value = "from FncVO where fncName like %?1% order by fncId")
    List<FncVO> findByFncNameLike(String fncName);
}
