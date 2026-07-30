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

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDto registerUser(UserDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exists");
        }

        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPassword() != null ? request.getPassword() : "");
        user.setStatus(UserStatus.active);
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());

        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers(int page, int pageSize) {
        // Page in Spring Data is 0-indexed, OpenAPI query default is 1
        int pageIndex = page > 0 ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserDto> dtos = new ArrayList<>();
        for (User user : userPage.getContent()) {
            dtos.add(toDto(user));
        }
        return dtos;
    }

    @Transactional(readOnly = true)
    public UserDto getUserProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return toDto(user);
    }

    @Transactional
    public UserDto updateUserProfile(UUID userId, UserDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        user.setUpdatedAt(OffsetDateTime.now());
        User updatedUser = userRepository.save(user);
        return toDto(updatedUser);
    }

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

