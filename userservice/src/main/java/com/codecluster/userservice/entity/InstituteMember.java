package com.codecluster.userservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

/*
 * Entity class representing the institute_members table.
 *
 * Each object of this class corresponds to one row
 * in the institute_members table.
 *
 * JPA/Hibernate uses this class to map Java objects
 * to database records.
 */
@Entity
@Table(name = "institute_members")
public class InstituteMember {

    /*
     * Primary key of the institute_members table.
     * Uniquely identifies each institute member.
     */
    @Id
    @Column(name = "member_id")
    private UUID memberId;

    /*
     * Institute to which this member belongs.
     *
     * Many members can belong to one institute.
     * institute_id is the foreign key column.
     *
     * FetchType.LAZY loads the Institute entity
     * only when it is accessed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id", nullable = false)
    private Institute institute;

    /*
     * User associated with this membership.
     *
     * Many institute memberships can reference
     * the same user.
     *
     * user_id is the foreign key column.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /*
     * Role assigned to the user within the institute
     * (e.g. admin, instructor, student).
     *
     * Stored as a PostgreSQL named enum.
     */
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "member_role", nullable = false)
    private MemberRole memberRole;

    /*
     * Current status of the institute member
     * (e.g. active, inactive, suspended).
     *
     * Stored as a PostgreSQL named enum.
     */
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    /*
     * Date and time when the user joined
     * the institute.
     */
    @Column(name = "joined_at", nullable = false)
    private OffsetDateTime joinedAt;

    /*
     * Default constructor required by JPA.
     */
    public InstituteMember() {
    }

    /*
     * Parameterized constructor used to initialize
     * all fields when creating an InstituteMember object.
     */
    public InstituteMember(UUID memberId,
                           Institute institute,
                           User user,
                           MemberRole memberRole,
                           UserStatus status,
                           OffsetDateTime joinedAt) {
        this.memberId = memberId;
        this.institute = institute;
        this.user = user;
        this.memberRole = memberRole;
        this.status = status;
        this.joinedAt = joinedAt;
    }

    /*
     * Executes automatically before a new entity
     * is inserted into the database.
     *
     * Generates default values for fields that
     * have not been initialized.
     */
    @PrePersist
    public void onCreate() {

        // Generate a UUID if one hasn't been assigned.
        if (this.memberId == null) {
            this.memberId = UUID.randomUUID();
        }

        // Record the date and time the user joined.
        if (this.joinedAt == null) {
            this.joinedAt = OffsetDateTime.now();
        }
    }

    // Standard getters and setters.

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