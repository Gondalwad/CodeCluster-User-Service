package com.codecluster.userservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;

import java.time.OffsetDateTime;

/*
 * Entity class representing the user_roles table.
 *
 * This is a junction (join) table that implements the
 * many-to-many relationship between users and roles.
 *
 * Each object of this class corresponds to one row
 * in the user_roles table.
 */
@Entity
@Table(name = "user_roles")
public class UserRole {

    /*
     * Composite primary key consisting of:
     * - userId
     * - roleId
     *
     * The UserRoleId class is marked with @Embeddable
     * and contains both key fields.
     */
    @EmbeddedId
    private UserRoleId id;

    /*
     * References the user associated with this role assignment.
     *
     * @MapsId("userId") tells JPA to use the userId
     * from the embedded primary key.
     *
     * Many UserRole records can reference one User.
     */
    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /*
     * References the role assigned to the user.
     *
     * @MapsId("roleId") tells JPA to use the roleId
     * from the embedded primary key.
     *
     * Many UserRole records can reference one Role.
     */
    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    /*
     * Date and time when the role
     * was assigned to the user.
     */
    @Column(name = "assigned_at", nullable = false)
    private OffsetDateTime assignedAt;

    /*
     * Default constructor required by JPA.
     */
    public UserRole() {
    }

    /*
     * Convenience constructor used when assigning
     * a role to a user.
     *
     * Creates the composite primary key automatically
     * using the user's ID and the role's ID.
     */
    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
        this.id = new UserRoleId(user.getUserId(), role.getRoleId());
        this.assignedAt = OffsetDateTime.now();
    }

    /*
     * Parameterized constructor used to initialize
     * all fields explicitly.
     */
    public UserRole(UserRoleId id,
                    User user,
                    Role role,
                    OffsetDateTime assignedAt) {
        this.id = id;
        this.user = user;
        this.role = role;
        this.assignedAt = assignedAt;
    }

    /*
     * Executes automatically before a new entity
     * is inserted into the database.
     *
     * Initializes the assignment timestamp if it
     * has not already been provided.
     */
    @PrePersist
    public void onCreate() {

        // Record the assignment timestamp.
        if (this.assignedAt == null) {
            this.assignedAt = OffsetDateTime.now();
        }
    }

    // Standard getters and setters.

    public UserRoleId getId() {
        return id;
    }

    public void setId(UserRoleId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public OffsetDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(OffsetDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
}