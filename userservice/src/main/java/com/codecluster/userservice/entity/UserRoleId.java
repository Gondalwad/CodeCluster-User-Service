package com.codecluster.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/*
 * Embeddable class representing the composite primary key
 * of the user_roles table.
 *
 * The primary key consists of:
 * - userId
 * - roleId
 *
 * This class is embedded into the UserRole entity
 * using @EmbeddedId.
 */
@Embeddable
public class UserRoleId implements Serializable {

    /*
     * ID of the user.
     * Forms part of the composite primary key.
     */
    @Column(name = "user_id")
    private UUID userId;

    /*
     * ID of the role.
     * Forms part of the composite primary key.
     */
    @Column(name = "role_id")
    private UUID roleId;

    /*
     * Default constructor required by JPA.
     */
    public UserRoleId() {
    }

    /*
     * Parameterized constructor used to initialize
     * both parts of the composite primary key.
     */
    public UserRoleId(UUID userId, UUID roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    // Standard getters and setters.

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    /*
     * Determines whether two composite key objects
     * represent the same database record.
     *
     * Two UserRoleId objects are considered equal
     * when both userId and roleId are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserRoleId that = (UserRoleId) o;
        return Objects.equals(userId, that.userId)
                && Objects.equals(roleId, that.roleId);
    }

    /*
     * Returns a hash code based on the composite key fields.
     *
     * JPA requires composite key classes to implement
     * equals() and hashCode() so that entity identity
     * works correctly in collections and persistence.
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }
}