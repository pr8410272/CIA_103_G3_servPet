package com.servPet.sitterFav.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servPet.sitterFav.model.SitterFavVO;

@Repository
public interface SitterFavRepository extends JpaRepository<SitterFavVO, Integer> {

	
//	List<SitterFavVO> findByMebId(Integer mebId);

	/**  新增收藏  **/
	
	 boolean existsByMebIdAndPsId(Integer mebId, Integer psId);
	
	/**  刪除收藏 **/
	void deleteBySitterFavoId(Integer sitterFavoId);
	
	
	/**  檢查是否存在指定的收藏ID **/
	boolean existsBySitterFavoId(Integer sitterFavoId);

	
	@Query("SELECT f.psId FROM SitterFavVO f WHERE f.sitterFavoId = :sitterFavoId")
	Integer findPsIdBySitterFavoId(@Param("sitterFavoId") Integer sitterFavoId);

	
	@Query("SELECT new map(p.psId as psId, p.psName as psName, p.psLicenses as psLicenses, p.totalStars as totalStars) " +
		       "FROM PsVO p WHERE p.psId IN :psIds")
		List<Map<String, Object>> findDetailsByPsIds(@Param("psIds") List<Integer> psIds);

	
	
	/**  顯示前台 **/
	@Query("SELECT new map(f.sitterFavoId as sitterFavoId, f.mebId as mebId, p.psId as psId, "
			+ "p.psName as psName, p.psArea as psArea, p.psLicenses as psLicenses, p.totalStars as totalStars) "
			+ "FROM SitterFavVO f JOIN PsVO p ON f.psId = p.psId " + "WHERE f.mebId = :mebId")
	List<Map<String, Object>> findFavoritesWithDetailsByMebId(@Param("mebId") Integer mebId);
}
