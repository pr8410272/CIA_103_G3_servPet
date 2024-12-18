package com.servPet.ps.model;

import com.servPet.ps.model.PsVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PsRepository extends JpaRepository<PsVO, Integer> {
//    List<PsVO> findByPsIdOrPsNameContainingOrPsAreaContainingOrTotalStarsOrRatingTimesOrReportTimesOrPsPic(
//            Integer psId, String psName, String psArea, Integer totalStars, Integer ratingTimes, Integer reportTimes, byte[] psPic
//    );

    @Query(value = "select psPic from PsVO where psId = :psId")
    byte[] findPicById(@Param("psId") Integer psId);
    
    // 查詢指定保母的證件照(學雍.保母停權)
   	@Query(value = "select psLicenses from PsVO where psId = :psId")
   	byte[] findLicensesByPsId(@Param("psId") Integer psId);

    // 查詢指定保母的編號(學雍.登入)
    	PsVO findByPsId(Integer psId);
    	
    // 查詢指定保母的密碼(學雍.登入)
    	PsVO findByPsPw(String psPw);

}

