package com.codecluster.userservice.service;

import com.codecluster.userservice.dto.EnrollmentDto;
import com.codecluster.userservice.entity.Enrollment;
import com.codecluster.userservice.entity.Institute;
import com.codecluster.userservice.entity.User;
import com.codecluster.userservice.exception.ResourceAlreadyExistsException;
import com.codecluster.userservice.exception.ResourceNotFoundException;
import com.codecluster.userservice.repository.EnrollmentRepository;
import com.codecluster.userservice.repository.InstituteRepository;
import com.codecluster.userservice.repository.UserRepository;
import com.codecluster.userservice.entity.EnrollmentStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

/*
 * Service class responsible for managing
 * student enrollments.
 *
 * Contains business logic for enrolling
 * students and updating enrollment status.
 */
@Service
public class EnrollmentService {

    /*
     * Repository dependencies used to access
     * enrollment, institute, and user data.
     */
    private final EnrollmentRepository enrollmentRepository;
    private final InstituteRepository instituteRepository;
    private final UserRepository userRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             InstituteRepository instituteRepository,
                             UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.instituteRepository = instituteRepository;
        this.userRepository = userRepository;
    }


    /*
     * Enrolls a user into an institute.
     *
     * Steps:
     * 1. Verify the institute exists.
     * 2. Verify the user exists.
     * 3. Ensure the user is not already enrolled.
     * 4. Create a new Enrollment entity.
     * 5. Save it to the database.
     * 6. Convert the entity into a DTO and return it.
     *
     * @Transactional ensures that all database
     * operations are executed as a single transaction.
     */
    @Transactional
    public EnrollmentDto enrollStudent(UUID instituteId, EnrollmentDto request) {
        // Find the institute or throw an exception.
        Institute institute = instituteRepository.findById(instituteId)
                .orElseThrow(() -> new ResourceNotFoundException("Institute not found with id: " + instituteId));

        // Find the user or throw an exception
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        // Prevent duplicate enrollments.
        if (enrollmentRepository.existsByInstituteAndUser(institute, user)) {
            throw new ResourceAlreadyExistsException("User is already enrolled in this institute");
        }

        /*
         * Create and populate a new Enrollment entity.
         */
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(UUID.randomUUID());
        enrollment.setInstitute(institute);
        enrollment.setUser(user);
        enrollment.setStatus(EnrollmentStatus.active);
        enrollment.setCourseStart(request.getCourseStart());
        enrollment.setCourseEnd(request.getCourseEnd());
        enrollment.setEnrolledAt(OffsetDateTime.now());

        // Persist the enrollment.
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        // Return the saved enrollment as a DTO.
        return toDto(savedEnrollment);
    }

    /*
     * Updates the status of an existing enrollment.
     *
     * Steps:
     * 1. Retrieve the enrollment.
     * 2. Update the status if provided.
     * 3. Save the updated entity.
     * 4. Convert it to a DTO.
     */
    @Transactional
    public EnrollmentDto updateEnrollmentStatus(
            UUID enrollmentId,
            EnrollmentDto request) {

        // Retrieve the enrollment or throw an exception.
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enrollment not found with id: " + enrollmentId));

        // Update only the status field.
        if (request.getStatus() != null) {
            enrollment.setStatus(request.getStatus());
        }

        // Save the updated enrollment
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        return toDto(updatedEnrollment);
    }

    /*
     * Converts an Enrollment entity into
     * an EnrollmentDto.
     *
     * This separates the persistence model
     * from the data returned by the API.
     */
    private EnrollmentDto toDto(Enrollment enrollment) {
        EnrollmentDto dto = new EnrollmentDto();
        dto.setEnrollmentId(enrollment.getEnrollmentId());
        dto.setInstituteId(enrollment.getInstitute().getInstituteId());
        dto.setUserId(enrollment.getUser().getUserId());
        dto.setStatus(enrollment.getStatus());
        dto.setCourseStart(enrollment.getCourseStart());
        dto.setCourseEnd(enrollment.getCourseEnd());
        dto.setEnrolledAt(enrollment.getEnrolledAt());
        return dto;
    }
}
