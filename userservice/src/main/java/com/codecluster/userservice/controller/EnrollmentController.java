package com.codecluster.userservice.controller;

import com.codecluster.userservice.dto.EnrollmentDto;
import com.codecluster.userservice.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PatchMapping("/{enrollmentId}/status")
    public ResponseEntity<EnrollmentDto> updateEnrollmentStatus(
            @RequestHeader("X-User-Id") UUID xUserId,
            @PathVariable UUID enrollmentId,
            @Valid @RequestBody EnrollmentDto request) {

        EnrollmentDto response =
                enrollmentService.updateEnrollmentStatus(enrollmentId, request);

        return ResponseEntity.ok(response);
    }
}