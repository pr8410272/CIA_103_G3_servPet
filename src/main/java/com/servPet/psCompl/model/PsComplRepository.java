package com.servPet.psCompl.model;

import com.servPet.psCompl.model.PsComplVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PsComplRepository extends JpaRepository<PsComplVO, Integer> {

    // 根據 PS_ID 查詢檢舉
    List<PsComplVO> findByPsId(Integer psVO);

    // 根據 MEB_ID 查詢檢舉
    List<PsComplVO> findByMebId(Integer mebVO);

    // 根據 PS_COMPL_STATUS 查詢檢舉
    List<PsComplVO> findByPsComplStatus(String psComplStatus);

    // 根據檢舉日期範圍查詢檢舉
    @Query(value = "from PsComplVO where psComplDate >= ?1 and psComplDate <= ?2 order by psComplDate")
    List<PsComplVO> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    // 根據檢舉描述模糊查詢
    @Query(value = "from PsComplVO where psComplDes like %?1% order by psComplId")
    List<PsComplVO> findByPsComplDesLike(String psComplDes);
}
