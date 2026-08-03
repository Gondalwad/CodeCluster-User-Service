package com.codecluster.userservice.repository;

import com.codecluster.userservice.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/*
 * Repository interface for performing CRUD
 * operations on the InstituteMember entity.
 *
 * By extending JpaRepository, Spring Data JPA
 * automatically provides methods such as
 * save(), findById(), findAll(), delete(), etc.
 */
public interface InstituteMemberRepository extends JpaRepository<InstituteMember, UUID> {

    /*
     * Checks whether a specific user is already
     * a member of the given institute.
     *
     * Spring Data JPA generates the query
     * automatically from the method name.
     */
    boolean existsByInstituteAndUser(Institute institute, User user);

    /*
     * Retrieves all members of a specific institute.
     *
     * Results are returned as a Page to support
     * pagination.
     */
    Page<InstituteMember> findByInstituteInstituteId(
            UUID instituteId,
            Pageable pageable);

    /*
     * Retrieves institute members filtered
     * by their assigned role.
     */
    Page<InstituteMember> findByInstituteInstituteIdAndMemberRole(
            UUID instituteId,
            MemberRole memberRole,
            Pageable pageable);

    /*
     * Retrieves institute members filtered
     * by their current status.
     */
    Page<InstituteMember> findByInstituteInstituteIdAndStatus(
            UUID instituteId,
            UserStatus status,
            Pageable pageable);

    /*
     * Retrieves institute members filtered
     * by both role and status.
     */
    Page<InstituteMember> findByInstituteInstituteIdAndMemberRoleAndStatus(
            UUID instituteId,
            MemberRole memberRole,
            UserStatus status,
            Pageable pageable);

    /*
     * Finds a specific institute member using
     * both the member ID and institute ID.
     *
     * Returns an Optional because the member
     * may not exist.
     */
    java.util.Optional<InstituteMember> findByMemberIdAndInstituteInstituteId(
            UUID memberId,
            UUID instituteId);
}