package com.codecluster.userservice.repository;

import com.codecluster.userservice.entity.Enrollment;
import com.codecluster.userservice.entity.Institute;
import com.codecluster.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/*
 * Repository interface for performing CRUD
 * operations on the Enrollment entity.
 *
 * By extending JpaRepository, Spring Data JPA
 * automatically provides common database operations
 * such as save(), findById(), findAll(), delete(), etc.
 */
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    /*
     * Checks whether a user is already enrolled
     * in the specified institute.
     *
     * Spring Data JPA automatically generates the
     * query based on the method name.
     */
    boolean existsByInstituteAndUser(Institute institute, User user);

}