package com.codecluster.userservice.repository;

import com.codecluster.userservice.entity.UserRole;
import com.codecluster.userservice.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Repository interface for performing CRUD
 * operations on the UserRole entity.
 *
 * By extending JpaRepository, Spring Data JPA
 * automatically provides methods such as
 * save(), findById(), findAll(), delete(), etc.
 *
 * The entity uses UserRoleId as its composite
 * primary key.
 */
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

}