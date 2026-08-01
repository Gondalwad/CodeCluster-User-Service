package com.codecluster.userservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @Column(name = "enrollment_id")
    private UUID enrollmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id", nullable = false)
    private Institute institute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false)
    private EnrollmentStatus status;

    @Column(name = "course_start", nullable = false)
    private OffsetDateTime courseStart;

    @Column(name = "course_end")
    private OffsetDateTime courseEnd;

    @Column(name = "enrolled_at", nullable = false)
    private OffsetDateTime enrolledAt;

    public Enrollment() {
    }

    public Enrollment(UUID enrollmentId, Institute institute, User user, EnrollmentStatus status, OffsetDateTime courseStart, OffsetDateTime courseEnd, OffsetDateTime enrolledAt) {
        this.enrollmentId = enrollmentId;
        this.institute = institute;
        this.user = user;
        this.status = status;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.enrolledAt = enrolledAt;
    }

    @PrePersist
    public void onCreate() {
        if (this.enrollmentId == null) {
            this.enrollmentId = UUID.randomUUID();
        }
        if (this.enrolledAt == null) {
            this.enrolledAt = OffsetDateTime.now();
        }
        if (this.status == null) {
            this.status = EnrollmentStatus.active;
        }
    }

    public UUID getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(UUID enrollmentId) {
        this.enrollmentId = enrollmentId;
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
