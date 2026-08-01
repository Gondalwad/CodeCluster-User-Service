package com.codecluster.userservice.controller;

import com.codecluster.userservice.dto.EnrollmentDto;
import com.codecluster.userservice.dto.InstituteDto;
import com.codecluster.userservice.dto.InstituteMemberDto;
import com.codecluster.userservice.service.EnrollmentService;
import com.codecluster.userservice.service.InstituteMemberService;
import com.codecluster.userservice.service.InstituteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/institutes")
public class InstituteController {

    private final InstituteService instituteService;
    private final InstituteMemberService instituteMemberService;
    private final EnrollmentService enrollmentService;

    public InstituteController(InstituteService instituteService,
                               InstituteMemberService instituteMemberService,
                               EnrollmentService enrollmentService) {
        this.instituteService = instituteService;
        this.instituteMemberService = instituteMemberService;
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<InstituteDto> createInstitute(
            @RequestHeader("X-User-Id") UUID xUserId,
            @Valid @RequestBody InstituteDto request) {
        InstituteDto response = instituteService.createInstitute(xUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<InstituteDto>> getAllInstitutes(
            @RequestHeader("X-User-Id") UUID xUserId) {
        List<InstituteDto> response = instituteService.getAllInstitutes();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{instituteId}/members")
    public ResponseEntity<InstituteMemberDto> addMember(
            @RequestHeader("X-User-Id") UUID xUserId,
            @PathVariable UUID instituteId,
            @Valid @RequestBody InstituteMemberDto request) {
        InstituteMemberDto response = instituteMemberService.addMemberToInstitute(instituteId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{instituteId}/enrollments")
    public ResponseEntity<EnrollmentDto> enrollStudent(
            @RequestHeader("X-User-Id") UUID xUserId,
            @PathVariable UUID instituteId,
            @Valid @RequestBody EnrollmentDto request) {
        EnrollmentDto response = enrollmentService.enrollStudent(instituteId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{instituteId}/members")
    public ResponseEntity<List<InstituteMemberDto>> getInstituteMembers(
            @PathVariable UUID instituteId,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<InstituteMemberDto> response = instituteMemberService.getInstituteMembers(
                instituteId, role, status, page, pageSize);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{instituteId}/members/{memberId}")
    public ResponseEntity<InstituteMemberDto> updateMember(
            @RequestHeader("X-User-Id") UUID xUserId,
            @PathVariable UUID instituteId,
            @PathVariable UUID memberId,
            @Valid @RequestBody InstituteMemberDto request) {

        InstituteMemberDto response =
                instituteMemberService.updateMember(instituteId, memberId, request);

        return ResponseEntity.ok(response);
    }

}
