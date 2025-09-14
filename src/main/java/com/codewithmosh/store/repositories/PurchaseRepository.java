package com.codewithmosh.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithmosh.store.entities.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}
