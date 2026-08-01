package com.codecluster.userservice.dto;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.codecluster.userservice.entity.EnrollmentStatus;

public class EnrollmentDto {

    private UUID enrollmentId;

    private UUID instituteId;

    @NotNull(message = "User ID is required")
    private UUID userId;

    private EnrollmentStatus status;

    @NotNull(message = "Course start date is required")
    private OffsetDateTime courseStart;

    private OffsetDateTime courseEnd;

    private OffsetDateTime enrolledAt;

    public EnrollmentDto() {
    }

    public EnrollmentDto(UUID enrollmentId, UUID instituteId, UUID userId, EnrollmentStatus status, OffsetDateTime courseStart, OffsetDateTime courseEnd, OffsetDateTime enrolledAt) {
        this.enrollmentId = enrollmentId;
        this.instituteId = instituteId;
        this.userId = userId;
        this.status = status;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.enrolledAt = enrolledAt;
    }

    public UUID getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(UUID enrollmentId) {
        this.enrollmentId = enrollmentId;
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

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public OffsetDateTime getCourseStart() {
        return courseStart;
    }

    public void setCourseStart(OffsetDateTime courseStart) {
        this.courseStart = courseStart;
    }

    public OffsetDateTime getCourseEnd() {
        return courseEnd;
    }

    public void setCourseEnd(OffsetDateTime courseEnd) {
        this.courseEnd = courseEnd;
    }

    public OffsetDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(OffsetDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
}
