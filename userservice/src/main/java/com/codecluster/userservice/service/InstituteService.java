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

/*
 * Service class responsible for managing
 * institute-related business operations.
 *
 * Contains business logic for creating
 * institutes and retrieving institute data.
 */
@Service
public class InstituteService {

    /*
     * Repository dependencies used to access
     * institute and user data.
     */
    private final InstituteRepository instituteRepository;
    private final UserRepository userRepository;

    /*
     * Constructor-based dependency injection.
     */
    public InstituteService(InstituteRepository instituteRepository,
                            UserRepository userRepository) {
        this.instituteRepository = instituteRepository;
        this.userRepository = userRepository;
    }

    /*
     * Creates a new institute.
     *
     * Steps:
     * 1. Verify the creator exists.
     * 2. Ensure the institute email is unique.
     * 3. Create and populate the Institute entity.
     * 4. Save it to the database.
     * 5. Convert the entity into a DTO and return it.
     *
     * @Transactional ensures all database
     * operations are executed as a single transaction.
     */
    @Transactional
    public InstituteDto createInstitute(UUID userId, InstituteDto request) {

        // Find the creator or throw an exception.
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        // Prevent duplicate institute email addresses.
        if (request.getEmail() != null &&
                instituteRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    "Institute email already exists");
        }

        // Create and populate a new Institute entity.
        Institute institute = new Institute();
        institute.setInstituteId(UUID.randomUUID());
        institute.setName(request.getName());
        institute.setEmail(request.getEmail());
        institute.setSubscriptionPlan(
                request.getSubscriptionPlan() != null
                        ? request.getSubscriptionPlan()
                        : "free");
        institute.setStatus(InstituteStatus.pending_approval);
        institute.setCreatedBy(creator);
        institute.setCreatedAt(OffsetDateTime.now());
        institute.setUpdatedAt(OffsetDateTime.now());

        // Persist the institute.
        Institute savedInstitute = instituteRepository.save(institute);

        // Return the saved institute as a DTO.
        return toDto(savedInstitute);
    }

    /*
     * Retrieves all institutes.
     *
     * Converts every Institute entity into
     * an InstituteDto before returning it.
     *
     * @Transactional(readOnly = true) optimizes
     * the transaction for read-only operations.
     */
    @Transactional(readOnly = true)
    public List<InstituteDto> getAllInstitutes() {

        // Retrieve all institutes from the database.
        List<Institute> institutes = instituteRepository.findAll();

        // Convert each entity into a DTO.
        List<InstituteDto> dtos = new ArrayList<>();

        for (Institute institute : institutes) {
            dtos.add(toDto(institute));
        }

        return dtos;
    }

    /*
     * Converts an Institute entity into
     * an InstituteDto.
     *
     * This separates the persistence model
     * from the data returned by the API.
     */
    private InstituteDto toDto(Institute institute) {

        InstituteDto dto = new InstituteDto();
        dto.setInstituteId(institute.getInstituteId());
        dto.setName(institute.getName());
        dto.setEmail(institute.getEmail());
        dto.setSubscriptionPlan(institute.getSubscriptionPlan());
        dto.setStatus(institute.getStatus());

        // Include the creator's ID if available.
        if (institute.getCreatedBy() != null) {
            dto.setCreatedBy(institute.getCreatedBy().getUserId());
        }

        return dto;
    }
}