package com.servPet.meb.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface MebRepository extends JpaRepository<MebVO, Integer> {
    Optional<MebVO> findByMebMail(String mebMail);
    
    @Query("SELECT m.id FROM MebVO m WHERE m.mebMail = :mebMail")
    Integer findMebIdByMebMail(@Param("mebMail") String mebMail);
    
    @Query("SELECT m FROM MebVO m WHERE m.mebId = :mebId")
    MebVO findMemberById(@Param("mebId") Integer mebId);
    
    @Modifying
    @Transactional
    @Query("UPDATE MebVO m SET m.bal = m.bal - :amount WHERE m.mebId = :mebId")
    void deductBalance(@Param("mebId") Integer mebId, @Param("amount") Double amount);

  //用會員編號查找會員圖片(學雍.會員停權)
    @Query(value = "select mebImg from MebVO where mebId = :mebId")
	byte[] findMebImgByPsId(@Param("mebId") Integer mebId);

}
