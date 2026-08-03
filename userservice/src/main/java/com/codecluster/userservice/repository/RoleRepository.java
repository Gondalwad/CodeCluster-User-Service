package com.codecluster.userservice.repository;

import com.codecluster.userservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/*
 * Repository interface for performing CRUD
 * operations on the Role entity.
 *
 * By extending JpaRepository, Spring Data JPA
 * automatically provides methods such as
 * save(), findById(), findAll(), delete(), etc.
 */
public interface RoleRepository extends JpaRepository<Role, UUID> {

    /*
     * Finds a role by its name.
     *
     * Returns an Optional because a role with
     * the given name may or may not exist.
     *
     * Spring Data JPA automatically generates
     * the query from the method name.
     */
    Optional<Role> findByRoleName(String roleName);

}