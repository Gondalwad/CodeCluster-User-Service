package com.codecluster.userservice.repository;

import com.codecluster.userservice.entity.Enrollment;
import com.codecluster.userservice.entity.Institute;
import com.codecluster.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    boolean existsByInstituteAndUser(Institute institute, User user);

}
