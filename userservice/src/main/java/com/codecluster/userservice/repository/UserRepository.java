package com.codecluster.userservice.repository;

import com.codecluster.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/*
 * Repository interface for performing CRUD
 * operations on the User entity.
 *
 * By extending JpaRepository, Spring Data JPA
 * automatically provides methods such as
 * save(), findById(), findAll(), delete(), etc.
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    /*
     * Checks whether a user with the given
     * email already exists.
     *
     * Spring Data JPA automatically generates
     * the query from the method name.
     */
    boolean existsByEmail(String email);

    /*
     * Checks whether a user with the given
     * username already exists.
     *
     * Spring Data JPA automatically generates
     * the query from the method name.
     */
    boolean existsByUsername(String username);

    /*
     * Finds a user by email address.
     *
     * Returns an Optional because a user with
     * the given email may or may not exist.
     */
    Optional<User> findByEmail(String email);

    /*
     * Finds a user by username.
     *
     * Returns an Optional because a user with
     * the given username may or may not exist.
     */
    Optional<User> findByUsername(String username);

}