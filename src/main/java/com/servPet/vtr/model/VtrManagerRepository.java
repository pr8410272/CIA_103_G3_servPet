package com.servPet.vtr.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VtrManagerRepository extends JpaRepository<VtrVO, Integer>, VtrManagerRepositoryCustom {
}
