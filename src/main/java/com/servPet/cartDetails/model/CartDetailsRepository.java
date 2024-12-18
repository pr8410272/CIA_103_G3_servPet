package com.servPet.cartDetails.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartDetailsRepository extends JpaRepository<CartDetailsVO, Integer> {
}