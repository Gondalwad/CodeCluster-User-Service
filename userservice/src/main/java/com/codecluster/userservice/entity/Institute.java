package com.codecluster.userservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/*
 * Entity class representing the institutes table.
 *
 * Each object of this class corresponds to one row
 * in the institutes table.
 *
 * JPA/Hibernate uses this class to map Java objects
 * to database records.
 */
@Entity
@Table(name = "institutes")
public class Institute {

    /*
     * Primary key of the institutes table.
     * Uniquely identifies each institute.
     */
    @Id
    @Column(name = "institute_id")
    private UUID instituteId;

    /*
     * Name of the institute.
     * Cannot be null.
     */
    @Column(nullable = false, length = 255)
    private String name;

    /*
     * Official email address of the institute.
     * Must be unique across all institutes.
     */
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /*
     * Subscription plan assigned to the institute
     * (e.g. Free, Basic, Premium).
     */
    @Column(name = "subscription_plan", nullable = false, length = 50)
    private String subscriptionPlan;

    /*
     * Current status of the institute.
     *
     * EnumType.STRING stores the enum value as text,
     * while JdbcTypeCode maps it to PostgreSQL's
     * named enum type.
     */
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "institute_status", nullable = false)
    private InstituteStatus status;

    /*
     * User who created this institute.
     *
     * Many institutes can be created by one user.
     * created_by is the foreign key column.
     *
     * FetchType.LAZY loads the User entity only
     * when it is accessed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    /*
     * Timestamp indicating when the institute
     * was created.
     */
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    /*
     * Timestamp indicating the last time
     * the institute was updated.
     */
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    /*
     * One institute can have many members.
     *
     * mappedBy = "institute" indicates that
     * the InstituteMember entity owns the relationship.
     */
    @OneToMany(mappedBy = "institute")
    private Set<InstituteMember> members = new HashSet<>();

    /*
     * Default constructor required by JPA.
     */
    public Institute() {
    }

    /*
     * Parameterized constructor used to initialize
     * all fields when creating an Institute object.
     */
    public Institute(UUID instituteId,
                     String name,
                     String email,
                     String subscriptionPlan,
                     InstituteStatus status,
                     User createdBy,
                     OffsetDateTime createdAt,
                     OffsetDateTime updatedAt) {
        this.instituteId = instituteId;
        this.name = name;
        this.email = email;
        this.subscriptionPlan = subscriptionPlan;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
        if (this.instituteId == null) {
            this.instituteId = UUID.randomUUID();
        }

        // Record the creation timestamp.
        if (this.createdAt == null) {
            this.createdAt = OffsetDateTime.now();
        }

        // Initialize the last updated timestamp.
        if (this.updatedAt == null) {
            this.updatedAt = OffsetDateTime.now();
        }
    }

    /*
     * Executes automatically before an existing
     * entity is updated in the database.
     *
     * Refreshes the updatedAt timestamp.
     */
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    // Standard getters and setters.

    public UUID getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(UUID instituteId) {
        this.instituteId = instituteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(String subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public InstituteStatus getStatus() {
        return status;
    }

    public void setStatus(InstituteStatus status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<InstituteMember> getMembers() {
        return members;
    }

    public void setMembers(Set<InstituteMember> members) {
        this.members = members;
    }
}