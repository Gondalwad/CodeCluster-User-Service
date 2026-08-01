package com.codecluster.userservice.repository;

import com.codecluster.userservice.entity.Institute;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstituteRepository extends JpaRepository<Institute, UUID> {

    boolean existsByEmail(String email);
}


