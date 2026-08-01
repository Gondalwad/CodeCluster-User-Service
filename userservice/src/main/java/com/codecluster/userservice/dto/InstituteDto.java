package com.codecluster.userservice.dto;

import com.codecluster.userservice.entity.InstituteStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class InstituteDto {

    private UUID instituteId;

    @NotBlank(message = "Institute name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    private String subscriptionPlan;

    private InstituteStatus status;

    private UUID createdBy;

    public InstituteDto() {
    }

    public InstituteDto(UUID instituteId, String name, String email, String subscriptionPlan, InstituteStatus status, UUID createdBy) {
        this.instituteId = instituteId;
        this.name = name;
        this.email = email;
        this.subscriptionPlan = subscriptionPlan;
        this.status = status;
        this.createdBy = createdBy;
    }

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

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }
}
