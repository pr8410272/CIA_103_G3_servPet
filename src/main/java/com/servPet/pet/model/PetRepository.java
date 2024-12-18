package com.servPet.pet.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetRepository extends JpaRepository<PetVO, Integer> {

	List<PetVO> findByMebId(Integer mebId);
	PetVO findOneByMebId(Integer mebID);
    @Query("SELECT p FROM PetVO p WHERE p.mebId = :mebId")
    List<PetVO> findPetsByMebId(@Param("mebId") Integer mebId);
}
