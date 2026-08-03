package com.codecluster.userservice.repository;

import com.codecluster.userservice.entity.Institute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/*
 * Repository interface for performing CRUD
 * operations on the Institute entity.
 *
 * By extending JpaRepository, Spring Data JPA
 * automatically provides methods such as
 * save(), findById(), findAll(), delete(), etc.
 */
public interface InstituteRepository extends JpaRepository<Institute, UUID> {

    /*
     * Checks whether an institute with the
     * given email already exists.
     *
     * Spring Data JPA automatically generates
     * the query from the method name.
     */
    boolean existsByEmail(String email);
}