package com.codecluster.userservice.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/*
 * Entity class representing the roles table.
 *
 * Each object of this class corresponds to one row
 * in the roles table.
 *
 * JPA/Hibernate uses this class to map Java objects
 * to database records.
 */
@Entity
@Table(name = "roles")
public class Role {

    /*
     * Primary key of the roles table.
     * Uniquely identifies each role.
     */
    @Id
    @Column(name = "role_id")
    private UUID roleId;

    /*
     * Name of the role
     * (e.g. ADMIN, INSTRUCTOR, STUDENT).
     *
     * Must be unique across all roles.
     */
    @Column(name = "role_name", nullable = false, unique = true, length = 50)
    private String roleName;

    /*
     * Description explaining the purpose
     * of the role.
     */
    @Column(name = "description")
    private String description;

    /*
     * Timestamp indicating when the role
     * was created.
     */
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    /*
     * One role can be assigned to many users.
     *
     * The UserRole entity owns the relationship,
     * as indicated by mappedBy = "role".
     */
    @OneToMany(mappedBy = "role")
    private Set<UserRole> userRoles = new HashSet<>();

    /*
     * Default constructor required by JPA.
     */
    public Role() {
    }

    /*
     * Parameterized constructor used to initialize
     * all fields when creating a Role object.
     */
    public Role(UUID roleId,
                String roleName,
                String description,
                OffsetDateTime createdAt) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
        this.createdAt = createdAt;
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
        if (this.roleId == null) {
            this.roleId = UUID.randomUUID();
        }

        // Record the creation timestamp.
        if (this.createdAt == null) {
            this.createdAt = OffsetDateTime.now();
        }
    }

    // Standard getters and setters.

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
}