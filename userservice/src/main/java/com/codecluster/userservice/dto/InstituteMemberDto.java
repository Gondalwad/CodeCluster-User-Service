package com.codecluster.userservice.dto;

import com.codecluster.userservice.entity.MemberRole;
import com.codecluster.userservice.entity.UserStatus;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public class InstituteMemberDto {

    private UUID memberId;

    private UUID instituteId;

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Member role is required")
    private MemberRole memberRole;

    private UserStatus status;

    private OffsetDateTime joinedAt;

    public InstituteMemberDto() {
    }

    public InstituteMemberDto(UUID memberId, UUID instituteId, UUID userId, MemberRole memberRole, UserStatus status, OffsetDateTime joinedAt) {
        this.memberId = memberId;
        this.instituteId = instituteId;
        this.userId = userId;
        this.memberRole = memberRole;
        this.status = status;
        this.joinedAt = joinedAt;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public UUID getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(UUID instituteId) {
        this.instituteId = instituteId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public MemberRole getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(MemberRole memberRole) {
        this.memberRole = memberRole;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public OffsetDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(OffsetDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}