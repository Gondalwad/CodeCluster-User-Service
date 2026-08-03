package com.codecluster.userservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

/*
 * Entity class representing the enrollments table.
 *
 * Each object of this class corresponds to one row
 * in the enrollments table.
 *
 * JPA/Hibernate uses this class to map Java objects
 * to database records.
 */
@Entity
@Table(name = "enrollments")
public class Enrollment {

    /*
     * Primary key of the enrollments table.
     * Uniquely identifies each enrollment.
     */
    @Id
    @Column(name = "enrollment_id")
    private UUID enrollmentId;

    /*
     * Many enrollments can belong to one institute.
     *
     * FetchType.LAZY means the institute object is loaded
     * from the database only when it is actually accessed.
     *
     * institute_id is the foreign key column.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id", nullable = false)
    private Institute institute;

    /*
     * Many enrollments can belong to one user.
     *
     * user_id is the foreign key referencing
     * the users table.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /*
     * Stores the enrollment status as a PostgreSQL
     * named enum instead of an integer.
     *
     * EnumType.STRING stores the enum name
     * (e.g. active, completed).
     */
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false)
    private EnrollmentStatus status;

    /*
     * Date and time when the course starts.
     */
    @Column(name = "course_start", nullable = false)
    private OffsetDateTime courseStart;

    /*
     * Date and time when the course ends.
     */
    @Column(name = "course_end")
    private OffsetDateTime courseEnd;

    /*
     * Timestamp indicating when the enrollment
     * was created.
     */
    @Column(name = "enrolled_at", nullable = false)
    private OffsetDateTime enrolledAt;

    /*
     * Default constructor required by JPA.
     */
    public Enrollment() {
    }

    /*
     * Parameterized constructor used to initialize
     * all fields when creating an Enrollment object.
     */
    public Enrollment(UUID enrollmentId,
                      Institute institute,
                      User user,
                      EnrollmentStatus status,
                      OffsetDateTime courseStart,
                      OffsetDateTime courseEnd,
                      OffsetDateTime enrolledAt) {
        this.enrollmentId = enrollmentId;
        this.institute = institute;
        this.user = user;
        this.status = status;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.enrolledAt = enrolledAt;
    }

    /*
     * Executes automatically before a new entity
     * is inserted into the database.
     *
     * Initializes default values if they
     * have not already been provided.
     */
    @PrePersist
    public void onCreate() {

        // Generate a UUID if one hasn't been assigned.
        if (this.enrollmentId == null) {
            this.enrollmentId = UUID.randomUUID();
        }

        // Record the current timestamp.
        if (this.enrolledAt == null) {
            this.enrolledAt = OffsetDateTime.now();
        }

        // Set the default enrollment status.
        if (this.status == null) {
            this.status = EnrollmentStatus.active;
        }
    }

    // Standard getters and setters.

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