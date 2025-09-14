package com.codewithmosh.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithmosh.store.entities.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {

	public Seller findByEmail(String email);
}
