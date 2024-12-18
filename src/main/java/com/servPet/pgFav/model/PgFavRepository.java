package com.servPet.pgFav.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PgFavRepository extends JpaRepository<PgFavVO, Integer> {

    // 查詢是否已收藏特定美容師
    Optional<PgFavVO> findByMebVO_MebIdAndPgVO_PgId(Integer mebId, Integer pgId);

    // 刪除收藏
    void deleteByMebVO_MebIdAndPgVO_PgId(Integer mebId, Integer pgId);
}
