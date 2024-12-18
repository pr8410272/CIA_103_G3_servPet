package com.servPet.pdFav.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PdFavRepository extends JpaRepository<PdFavVO, Integer> {

    // 查詢是否已收藏特定商品
    Optional<PdFavVO> findByMebVO_MebIdAndPdDetailsVO_PdId(Integer mebId, Integer pdId);

    // 刪除收藏
    void deleteByMebVO_MebIdAndPdDetailsVO_PdId(Integer mebId, Integer pdId);
}
