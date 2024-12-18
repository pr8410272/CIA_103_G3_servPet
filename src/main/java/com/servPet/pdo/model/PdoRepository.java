package com.servPet.pdo.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PdoRepository extends JpaRepository<PdoVO, Integer> {
}