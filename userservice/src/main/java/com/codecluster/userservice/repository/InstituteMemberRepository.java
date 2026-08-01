package com.codecluster.userservice.repository;

import com.codecluster.userservice.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstituteMemberRepository extends JpaRepository<InstituteMember, UUID> {

    boolean existsByInstituteAndUser(Institute institute, User user);

    Page<InstituteMember> findByInstituteInstituteId(
            UUID instituteId,
            Pageable pageable);

    Page<InstituteMember> findByInstituteInstituteIdAndMemberRole(
            UUID instituteId,
            MemberRole memberRole,
            Pageable pageable);

    Page<InstituteMember> findByInstituteInstituteIdAndStatus(
            UUID instituteId,
            UserStatus status,
            Pageable pageable);

    Page<InstituteMember> findByInstituteInstituteIdAndMemberRoleAndStatus(
            UUID instituteId,
            MemberRole memberRole,
            UserStatus status,
            Pageable pageable);

    java.util.Optional<InstituteMember> findByMemberIdAndInstituteInstituteId(
            UUID memberId,
            UUID instituteId);
}
