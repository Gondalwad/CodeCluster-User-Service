package com.codecluster.userservice.service;

import com.codecluster.userservice.dto.InstituteDto;
import com.codecluster.userservice.entity.Institute;
import com.codecluster.userservice.entity.InstituteStatus;
import com.codecluster.userservice.entity.User;
import com.codecluster.userservice.exception.ResourceAlreadyExistsException;
import com.codecluster.userservice.exception.ResourceNotFoundException;
import com.codecluster.userservice.repository.InstituteRepository;
import com.codecluster.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class InstituteService {

    private final InstituteRepository instituteRepository;
    private final UserRepository userRepository;

    public InstituteService(InstituteRepository instituteRepository, UserRepository userRepository) {
        this.instituteRepository = instituteRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public InstituteDto createInstitute(UUID userId, InstituteDto request) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (request.getEmail() != null && instituteRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Institute email already exists");
        }

        Institute institute = new Institute();
        institute.setInstituteId(UUID.randomUUID());
        institute.setName(request.getName());
        institute.setEmail(request.getEmail());
        institute.setSubscriptionPlan(request.getSubscriptionPlan() != null ? request.getSubscriptionPlan() : "free");
        institute.setStatus(InstituteStatus.pending_approval);
        institute.setCreatedBy(creator);
        institute.setCreatedAt(OffsetDateTime.now());
        institute.setUpdatedAt(OffsetDateTime.now());


        Institute savedInstitute = instituteRepository.save(institute);
        return toDto(savedInstitute);
    }

    @Transactional(readOnly = true)
    public List<InstituteDto> getAllInstitutes() {
        List<Institute> institutes = instituteRepository.findAll();
        List<InstituteDto> dtos = new ArrayList<>();
        for (Institute institute : institutes) {
            dtos.add(toDto(institute));
        }
        return dtos;
    }

    private InstituteDto toDto(Institute institute) {
        InstituteDto dto = new InstituteDto();
        dto.setInstituteId(institute.getInstituteId());
        dto.setName(institute.getName());
        dto.setEmail(institute.getEmail());
        dto.setSubscriptionPlan(institute.getSubscriptionPlan());
        dto.setStatus(institute.getStatus());
        if (institute.getCreatedBy() != null) {
            dto.setCreatedBy(institute.getCreatedBy().getUserId());
        }
        return dto;
    }
}