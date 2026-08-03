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

/*
 * Marks this class as a REST Controller.
 * Every method inside this class handles HTTP requests and
 * automatically returns JSON responses.
 *
 * All endpoints in this controller start with:
 * /api/v1/institutes
 */
@RestController
@RequestMapping("/api/v1/institutes")
public class InstituteController {

    /*
     * Service objects contain the business logic.
     * The controller's responsibility is only to:
     * 1. Receive the HTTP request.
     * 2. Extract request data.
     * 3. Call the appropriate service method.
     * 4. Return the HTTP response.
     */
    private final InstituteService instituteService;
    private final InstituteMemberService instituteMemberService;
    private final EnrollmentService enrollmentService;

    /*
     * Constructor Injection.
     * Spring automatically creates the service objects
     * and injects them into this controller.
     */
    public InstituteController(InstituteService instituteService,
                               InstituteMemberService instituteMemberService,
                               EnrollmentService enrollmentService) {
        this.instituteService = instituteService;
        this.instituteMemberService = instituteMemberService;
        this.enrollmentService = enrollmentService;
    }

    /*
     * Creates a new institute.
     *
     * Flow:
     * Client -> Controller -> InstituteService -> Database
     *
     * - Reads X-User-Id from request header.
     * - Reads institute details from request body.
     * - Validates the request.
     * - Returns HTTP 201 Created with the created institute.
     */
    @PostMapping
    public ResponseEntity<InstituteDto> createInstitute(
            @RequestHeader("X-User-Id") UUID xUserId,
            @Valid @RequestBody InstituteDto request) {

        InstituteDto response = instituteService.createInstitute(xUserId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*
     * Returns a list of all institutes.
     *
     * Flow:
     * Client -> Controller -> Service -> Database
     *
     * Response:
     * HTTP 200 OK
     * List<InstituteDto>
     */
    @GetMapping
    public ResponseEntity<List<InstituteDto>> getAllInstitutes(
            @RequestHeader("X-User-Id") UUID xUserId) {

        List<InstituteDto> response = instituteService.getAllInstitutes();

        return ResponseEntity.ok(response);
    }

    /*
     * Adds a member to an institute.
     *
     * Reads:
     * - instituteId from URL
     * - X-User-Id from request header
     * - Member details from request body
     *
     * Delegates the actual business logic to the service layer.
     */
    @PostMapping("/{instituteId}/members")
    public ResponseEntity<InstituteMemberDto> addMember(
            @RequestHeader("X-User-Id") UUID xUserId,
            @PathVariable UUID instituteId,
            @Valid @RequestBody InstituteMemberDto request) {

        InstituteMemberDto response =
                instituteMemberService.addMemberToInstitute(instituteId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*
     * Enrolls a student into an institute.
     *
     * Reads the instituteId from the URL and the enrollment
     * information from the request body.
     *
     * Returns HTTP 201 Created after successful enrollment.
     */
    @PostMapping("/{instituteId}/enrollments")
    public ResponseEntity<EnrollmentDto> enrollStudent(
            @RequestHeader("X-User-Id") UUID xUserId,
            @PathVariable UUID instituteId,
            @Valid @RequestBody EnrollmentDto request) {

        EnrollmentDto response =
                enrollmentService.enrollStudent(instituteId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*
     * Retrieves institute members.
     *
     * Optional query parameters:
     * - role
     * - status
     * - page
     * - pageSize
     *
     * Example:
     * GET /api/v1/institutes/{id}/members?role=instructor&page=1&pageSize=10
     */
    @GetMapping("/{instituteId}/members")
    public ResponseEntity<List<InstituteMemberDto>> getInstituteMembers(
            @PathVariable UUID instituteId,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<InstituteMemberDto> response =
                instituteMemberService.getInstituteMembers(
                        instituteId,
                        role,
                        status,
                        page,
                        pageSize);

        return ResponseEntity.ok(response);
    }

    /*
     * Updates an existing institute member.
     *
     * Reads:
     * - instituteId from URL
     * - memberId from URL
     * - Updated member information from request body
     *
     * The service locates the member, updates the required
     * fields, saves the changes, and returns the updated DTO.
     *
     * Returns HTTP 200 OK.
     */
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