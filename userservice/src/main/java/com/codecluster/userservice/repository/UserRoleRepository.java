package com.codecluster.userservice.repository;

import com.codecluster.userservice.entity.UserRole;
import com.codecluster.userservice.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

}
