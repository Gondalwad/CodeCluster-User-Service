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

@Service
public class InstituteMemberService {

    private final InstituteMemberRepository instituteMemberRepository;
    private final InstituteRepository instituteRepository;
    private final UserRepository userRepository;

    public InstituteMemberService(InstituteMemberRepository instituteMemberRepository,
                                  InstituteRepository instituteRepository,
                                  UserRepository userRepository) {
        this.instituteMemberRepository = instituteMemberRepository;
        this.instituteRepository = instituteRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public InstituteMemberDto addMemberToInstitute(UUID instituteId, InstituteMemberDto request) {
        Institute institute = instituteRepository.findById(instituteId)
                .orElseThrow(() -> new ResourceNotFoundException("Institute not found with id: " + instituteId));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));


        if (instituteMemberRepository.existsByInstituteAndUser(institute, user)) {
            throw new ResourceAlreadyExistsException("User is already a member of this institute");
        }

        InstituteMember member = new InstituteMember();
        member.setMemberId(UUID.randomUUID());
        member.setInstitute(institute);
        member.setUser(user);
        member.setMemberRole(request.getMemberRole());
        member.setStatus(UserStatus.active);
        member.setJoinedAt(OffsetDateTime.now());

        InstituteMember savedMember = instituteMemberRepository.save(member);
        return toDto(savedMember);
    }

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

    @Transactional(readOnly = true)
    public List<InstituteMemberDto> getInstituteMembers(
            UUID instituteId,
            String role,
            String status,
            int page,
            int pageSize) {

        if (!instituteRepository.existsById(instituteId)) {
            throw new ResourceNotFoundException("Institute not found with id: " + instituteId);
        }

        int pageIndex = page > 0 ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        Page<InstituteMember> memberPage;

        MemberRole memberRole = null;
        UserStatus userStatus = null;

        if (role != null && !role.isBlank()) {
            memberRole = MemberRole.valueOf(role.toLowerCase());
        }

        if (status != null && !status.isBlank()) {
            userStatus = UserStatus.valueOf(status.toLowerCase());
        }

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

        List<InstituteMemberDto> dtos = new ArrayList<>();

        for (InstituteMember member : memberPage.getContent()) {
            dtos.add(toDto(member));
        }

        return dtos;
    }

    @Transactional
    public InstituteMemberDto updateMember(
            UUID instituteId,
            UUID memberId,
            InstituteMemberDto request) {

        InstituteMember member = instituteMemberRepository
                .findByMemberIdAndInstituteInstituteId(memberId, instituteId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member not found with id: " + memberId));

        if (request.getMemberRole() != null) {
            member.setMemberRole(request.getMemberRole());
        }

        if (request.getStatus() != null) {
            member.setStatus(request.getStatus());
        }

        InstituteMember updatedMember = instituteMemberRepository.save(member);

        return toDto(updatedMember);
    }

}
