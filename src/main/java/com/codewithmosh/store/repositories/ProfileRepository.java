package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Profile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}