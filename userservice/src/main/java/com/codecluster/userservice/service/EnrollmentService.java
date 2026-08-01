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

@Service
public class EnrollmentService {

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

    @Transactional
    public EnrollmentDto enrollStudent(UUID instituteId, EnrollmentDto request) {
        Institute institute = instituteRepository.findById(instituteId)
                .orElseThrow(() -> new ResourceNotFoundException("Institute not found with id: " + instituteId));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        if (enrollmentRepository.existsByInstituteAndUser(institute, user)) {
            throw new ResourceAlreadyExistsException("User is already enrolled in this institute");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(UUID.randomUUID());
        enrollment.setInstitute(institute);
        enrollment.setUser(user);
        enrollment.setStatus(EnrollmentStatus.active);
        enrollment.setCourseStart(request.getCourseStart());
        enrollment.setCourseEnd(request.getCourseEnd());
        enrollment.setEnrolledAt(OffsetDateTime.now());

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return toDto(savedEnrollment);
    }

    @Transactional
    public EnrollmentDto updateEnrollmentStatus(
            UUID enrollmentId,
            EnrollmentDto request) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enrollment not found with id: " + enrollmentId));

        if (request.getStatus() != null) {
            enrollment.setStatus(request.getStatus());
        }

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        return toDto(updatedEnrollment);
    }

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
