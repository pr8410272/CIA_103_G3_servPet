package com.servPet.pdDetails.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PdDetailsRepository extends JpaRepository<PdDetailsVO, Integer> {

	@EntityGraph(attributePaths = { "pdImgVO" })
	@Query("SELECT p FROM PdDetailsVO p WHERE p.pdId = :pdId")
	PdDetailsVO getOneProductWithImages(@Param("pdId") Integer pdId);

	@Query("SELECT pd FROM PdDetailsVO pd " + "LEFT JOIN FETCH pd.pdImgVO " + // 加載圖片
			"LEFT JOIN FETCH pd.pdCategVO " + // 加載類別
			"WHERE pd.pdId = :pdId")
	Optional<PdDetailsVO> findByIdWithDetailsAndImages(@Param("pdId") Integer pdId);
	
    /**
     * 按商品狀態分組查詢
     * @param pdStatus 商品狀態
     * @return 該狀態的商品清單
     */
    @Query("SELECT p FROM PdDetailsVO p WHERE p.pdStatus = :pdStatus")
    List<PdDetailsVO> findByPdStatus(@Param("pdStatus") String pdStatus);

 // 立文的商品收藏
    // 根據商品編號查詢商品
    PdDetailsVO findByPdId(Integer pdId);
    
    // 根據會員及商品編號查詢商品收藏
//    Optional<PdFavVO> findByMebVO_MebIdAndPdDetailsVO_PdId(Integer mebId, Integer pdId);
}