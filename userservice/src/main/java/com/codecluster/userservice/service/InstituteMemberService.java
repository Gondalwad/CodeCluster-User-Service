package com.codecluster.userservice.service;

import com.codecluster.userservice.dto.InstituteMemberDto;
import com.codecluster.userservice.entity.Institute;
import com.codecluster.userservice.entity.InstituteMember;
import com.codecluster.userservice.entity.User;
import com.codecluster.userservice.entity.UserStatus;
import com.codecluster.userservice.exception.ResourceAlreadyExistsException;
import com.codecluster.userservice.exception.ResourceNotFoundException;
import com.codecluster.userservice.repository.InstituteMemberRepository;
import com.codecluster.userservice.repository.InstituteRepository;
import com.codecluster.userservice.repository.UserRepository;
import com.codecluster.userservice.entity.MemberRole;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 * Service class responsible for managing
 * institute memberships.
 *
 * Contains business logic for adding members,
 * retrieving members, and updating member
 * information.
 */
@Service
public class InstituteMemberService {

    /*
     * Repository dependencies used to access
     * institute member, institute, and user data.
     */
    private final InstituteMemberRepository instituteMemberRepository;
    private final InstituteRepository instituteRepository;
    private final UserRepository userRepository;

    /*
     * Constructor-based dependency injection.
     */
    public InstituteMemberService(InstituteMemberRepository instituteMemberRepository,
                                  InstituteRepository instituteRepository,
                                  UserRepository userRepository) {
        this.instituteMemberRepository = instituteMemberRepository;
        this.instituteRepository = instituteRepository;
        this.userRepository = userRepository;
    }

    /*
     * Adds a user as a member of an institute.
     *
     * Steps:
     * 1. Verify the institute exists.
     * 2. Verify the user exists.
     * 3. Ensure the user is not already a member.
     * 4. Create a new InstituteMember entity.
     * 5. Save it to the database.
     * 6. Convert the entity into a DTO and return it.
     *
     * @Transactional ensures all database operations
     * are executed as a single transaction.
     */
    @Transactional
    public InstituteMemberDto addMemberToInstitute(UUID instituteId, InstituteMemberDto request) {
        // Find the institute or throw an exception.
        Institute institute = instituteRepository.findById(instituteId)
                .orElseThrow(() -> new ResourceNotFoundException("Institute not found with id: " + instituteId));

        // Find the user or throw an exception.
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        // Prevent duplicate institute memberships.
        if (instituteMemberRepository.existsByInstituteAndUser(institute, user)) {
            throw new ResourceAlreadyExistsException("User is already a member of this institute");
        }

        // Create and populate a new institute member.
        InstituteMember member = new InstituteMember();
        member.setMemberId(UUID.randomUUID());
        member.setInstitute(institute);
        member.setUser(user);
        member.setMemberRole(request.getMemberRole());
        member.setStatus(UserStatus.active);
        member.setJoinedAt(OffsetDateTime.now());

        // Persist the new member.
        InstituteMember savedMember = instituteMemberRepository.save(member);
        return toDto(savedMember);
    }

    /*
     * Converts an InstituteMember entity into
     * an InstituteMemberDto.
     *
     * This separates the persistence model
     * from the data returned by the API.
     */
    private InstituteMemberDto toDto(InstituteMember member) {
        InstituteMemberDto dto = new InstituteMemberDto();
        dto.setMemberId(member.getMemberId());
        dto.setInstituteId(member.getInstitute().getInstituteId());
        dto.setUserId(member.getUser().getUserId());
        dto.setMemberRole(member.getMemberRole());
        dto.setStatus(member.getStatus());
        dto.setJoinedAt(member.getJoinedAt());
        return dto;
    }

    /*
     * Retrieves institute members with optional
     * filtering and pagination.
     *
     * Supports filtering by:
     * - member role
     * - user status
     *
     * If no filters are provided, all members
     * of the institute are returned.
     *
     * @Transactional(readOnly = true) optimizes
     * the transaction for read-only operations.
     */
    @Transactional(readOnly = true)
    public List<InstituteMemberDto> getInstituteMembers(
            UUID instituteId,
            String role,
            String status,
            int page,
            int pageSize) {

        // Verify that the institute exists.
        if (!instituteRepository.existsById(instituteId)) {
            throw new ResourceNotFoundException("Institute not found with id: " + instituteId);
        }

        // Convert the requested page number into
        // a zero-based page index.
        int pageIndex = page > 0 ? page - 1 : 0;
        // Create pagination information.
        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        Page<InstituteMember> memberPage;


        // Convert query parameters into enum values.
        MemberRole memberRole = null;
        UserStatus userStatus = null;

        /*
         * Select the appropriate repository method
         * depending on which filters were provided.
         */
        if (role != null && !role.isBlank()) {
            memberRole = MemberRole.valueOf(role.toLowerCase());
        }

        if (status != null && !status.isBlank()) {
            userStatus = UserStatus.valueOf(status.toLowerCase());
        }

        /*
         * Choose the appropriate repository method
         * based on the filters provided by the client.
         */
        if (role != null && !role.isBlank() &&
                status != null && !status.isBlank()) {

            memberPage = instituteMemberRepository
                    .findByInstituteInstituteIdAndMemberRoleAndStatus(
                            instituteId,
                            memberRole,
                            userStatus,
                            pageable);

        } else if (role != null && !role.isBlank()) {

            memberPage = instituteMemberRepository
                    .findByInstituteInstituteIdAndMemberRole(
                            instituteId,
                            memberRole,
                            pageable);

        } else if (status != null && !status.isBlank()) {

            memberPage = instituteMemberRepository
                    .findByInstituteInstituteIdAndStatus(
                            instituteId,
                            userStatus,
                            pageable);

        } else {

            memberPage = instituteMemberRepository
                    .findByInstituteInstituteId(
                            instituteId,
                            pageable);
        }

        // Convert each entity into its DTO representation.
        List<InstituteMemberDto> dtos = new ArrayList<>();

        for (InstituteMember member : memberPage.getContent()) {
            dtos.add(toDto(member));
        }

        return dtos;
    }

    /*
     * Updates an institute member's role
     * and/or status.
     *
     * Steps:
     * 1. Retrieve the member.
     * 2. Update only the provided fields.
     * 3. Save the updated entity.
     * 4. Return the updated DTO.
     */
    @Transactional
    public InstituteMemberDto updateMember(
            UUID instituteId,
            UUID memberId,
            InstituteMemberDto request) {

        // Find the member or throw an exception.
        InstituteMember member = instituteMemberRepository
                .findByMemberIdAndInstituteInstituteId(memberId, instituteId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member not found with id: " + memberId));

        // Update the member's role if provided.
        if (request.getMemberRole() != null) {
            member.setMemberRole(request.getMemberRole());
        }

        // Update the member's status if provided.
        if (request.getStatus() != null) {
            member.setStatus(request.getStatus());
        }

        // Persist the updated member.
        InstituteMember updatedMember = instituteMemberRepository.save(member);

        return toDto(updatedMember);
    }

}
