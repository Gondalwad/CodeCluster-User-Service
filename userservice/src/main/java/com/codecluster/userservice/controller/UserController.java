package com.codecluster.userservice.controller;

import com.codecluster.userservice.dto.UserDto;
import com.codecluster.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/*
 * REST controller responsible for handling
 * user-related API requests.
 *
 * Receives HTTP requests, delegates business
 * logic to the service layer, and returns
 * HTTP responses.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    /*
     * Service dependency used to perform
     * user-related business operations.
     */
    private final UserService userService;

    /*
     * Constructor-based dependency injection.
     */
    public UserController(UserService userService) {
            this.userService = userService;
    }

    /*
     * Registers a new user.
     *
     * Steps:
     * 1. Accept the request body.
     * 2. Validate the request.
     * 3. Delegate the registration logic to the service.
     * 4. Return HTTP 201 (Created) with the created user.
     */
    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody UserDto request) {
        UserDto response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*
     * Retrieves all users with pagination.
     *
     * The request supports page and pageSize
     * query parameters.
     *
     * X-User-Id and X-User-Role headers represent
     * the authenticated user making the request.
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestHeader("X-User-Id") UUID xUserId,
            @RequestHeader(value = "X-User-Role", required = false) String xUserRole,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        // Delegate the request to the service layer.
        List<UserDto> response = userService.getAllUsers(page, pageSize);
        return ResponseEntity.ok(response);
    }

    /*
     * Retrieves the profile of the currently
     * authenticated user.
     *
     * The user is identified using the
     * X-User-Id request header.
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUserProfile(
            @RequestHeader("X-User-Id") UUID xUserId) {

        // Delegate the request to the service layer.
        UserDto response = userService.getUserProfile(xUserId);
        return ResponseEntity.ok(response);
    }

    /*
     * Updates the profile of the currently
     * authenticated user.
     *
     * The user is identified using the
     * X-User-Id request header.
     */
    @PutMapping("/me")
    public ResponseEntity<UserDto> updateCurrentUserProfile(
            @RequestHeader("X-User-Id") UUID xUserId,
            @RequestBody UserDto request) {

        // Delegate the request to the service layer.
        UserDto response = userService.updateUserProfile(xUserId, request);
        return ResponseEntity.ok(response);
    }

}
