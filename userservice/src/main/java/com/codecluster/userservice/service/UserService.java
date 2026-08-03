package com.codecluster.userservice.service;

import com.codecluster.userservice.dto.UserDto;
import com.codecluster.userservice.entity.User;
import com.codecluster.userservice.entity.UserStatus;
import com.codecluster.userservice.exception.ResourceAlreadyExistsException;
import com.codecluster.userservice.exception.ResourceNotFoundException;
import com.codecluster.userservice.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 * Service class responsible for managing
 * user-related business operations.
 *
 * Contains business logic for user registration,
 * retrieving users, viewing profiles,
 * and updating user profiles.
 */
@Service
public class UserService {

    /*
     * Repository dependency used to access
     * user data.
     */
    private final UserRepository userRepository;

    /*
     * Constructor-based dependency injection.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * Registers a new user.
     *
     * Steps:
     * 1. Verify the email is unique.
     * 2. Verify the username is unique.
     * 3. Create a new User entity.
     * 4. Save it to the database.
     * 5. Convert the entity into a DTO and return it.
     *
     * @Transactional ensures all database
     * operations are executed as a single transaction.
     */
    @Transactional
    public UserDto registerUser(UserDto request) {

        // Prevent duplicate email addresses.
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        // Prevent duplicate usernames.
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exists");
        }

        // Create and populate a new User entity.
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPassword() != null ? request.getPassword() : "");
        user.setStatus(UserStatus.active);
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());

        // Persist the user.
        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }

    /*
     * Retrieves all users with pagination.
     *
     * Converts every User entity into
     * a UserDto before returning it.
     *
     * @Transactional(readOnly = true) optimizes
     * the transaction for read-only operations.
     */
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers(int page, int pageSize) {

        // Convert the requested page number
        // into a zero-based page index.
        // Page in Spring Data is 0-indexed, OpenAPI query default is 1
        int pageIndex = page > 0 ? page - 1 : 0;

        // Create pagination information
        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        // Retrieve users from the database.
        Page<User> userPage = userRepository.findAll(pageable);

        // Convert each entity into a DTO.
        List<UserDto> dtos = new ArrayList<>();
        for (User user : userPage.getContent()) {
            dtos.add(toDto(user));
        }
        return dtos;
    }

    /*
     * Retrieves the profile of a specific user.
     *
     * Finds the user by ID and returns
     * the corresponding DTO.
     */
    @Transactional(readOnly = true)
    public UserDto getUserProfile(UUID userId) {

        // Find the user or throw an exception.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return toDto(user);
    }

    /*
     * Updates the current user's profile.
     *
     * Only the fields provided in the request
     * are updated.
     */
    @Transactional
    public UserDto updateUserProfile(UUID userId, UserDto request) {

        // Find the user or throw an exception.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Update the user's name if provided.
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        // Record the modification time.
        user.setUpdatedAt(OffsetDateTime.now());

        // Persist the updated user.
        User updatedUser = userRepository.save(user);
        return toDto(updatedUser);
    }

    /*
     * Converts a User entity into
     * a UserDto.
     *
     * This separates the persistence model
     * from the data returned by the API.
     */
    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}

