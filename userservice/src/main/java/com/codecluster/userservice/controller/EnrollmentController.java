package com.codecluster.userservice.controller;

import com.codecluster.userservice.dto.EnrollmentDto;
import com.codecluster.userservice.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// Marks this class as a REST controller.
// Spring automatically converts returned objects into JSON.
@RestController

// Base URL for all endpoints in this controller.
@RequestMapping("/api/v1/enrollments")
public class EnrollmentController {

    // Service layer responsible for business logic.
    private final EnrollmentService enrollmentService;

    // Constructor Injection:
    // Spring injects an EnrollmentService bean when creating this controller.
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // Handles PATCH requests like:
    // PATCH /api/v1/enrollments/{enrollmentId}/status
    @PatchMapping("/{enrollmentId}/status")
    public ResponseEntity<EnrollmentDto> updateEnrollmentStatus(

            // Reads the X-User-Id value from the request header.
            // Usually used to identify the authenticated user.
            @RequestHeader("X-User-Id") UUID xUserId,

            // Extracts enrollmentId from the URL path.
            @PathVariable UUID enrollmentId,

            // Reads the JSON request body and validates it
            // using validation annotations in EnrollmentDto.
            @Valid @RequestBody EnrollmentDto request) {

        // Delegate the update operation to the service layer.
        // The controller should contain minimal business logic.
        EnrollmentDto response =
                enrollmentService.updateEnrollmentStatus(enrollmentId, request);

        // Return HTTP 200 OK along with the updated enrollment data.
        return ResponseEntity.ok(response);
    }
}