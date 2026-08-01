package com.codecluster.userservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "institute_members")
public class InstituteMember {

    @Id
    @Column(name = "member_id")
    private UUID memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id", nullable = false)
    private Institute institute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "member_role", nullable = false)
    private MemberRole memberRole;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Column(name = "joined_at", nullable = false)
    private OffsetDateTime joinedAt;

    public InstituteMember() {
    }

    public InstituteMember(UUID memberId, Institute institute, User user,MemberRole memberRole, UserStatus status, OffsetDateTime joinedAt) {
        this.memberId = memberId;
        this.institute = institute;
        this.user = user;
        this.memberRole = memberRole;
        this.status = status;
        this.joinedAt = joinedAt;
    }

    @PrePersist
    public void onCreate() {
        if (this.memberId == null) {
            this.memberId = UUID.randomUUID();
        }
        if (this.joinedAt == null) {
            this.joinedAt = OffsetDateTime.now();
        }
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MemberRole getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(MemberRole memberRole) {
        this.memberRole = memberRole;
    }

    public UserStatus getStatus() { return status; }

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

