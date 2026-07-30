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

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody UserDto request) {
        UserDto response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestHeader("X-User-Id") UUID xUserId,
            @RequestHeader(value = "X-User-Role", required = false) String xUserRole,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        List<UserDto> response = userService.getAllUsers(page, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUserProfile(
            @RequestHeader("X-User-Id") UUID xUserId) {
        UserDto response = userService.getUserProfile(xUserId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateCurrentUserProfile(
            @RequestHeader("X-User-Id") UUID xUserId,
            @RequestBody UserDto request) {
        UserDto response = userService.updateUserProfile(xUserId, request);
        return ResponseEntity.ok(response);
    }

}
