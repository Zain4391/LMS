package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.PublisherRequestDTO;
import com.LibraryManagementSystem.LMS.dto.PublisherResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Publisher;
import com.LibraryManagementSystem.LMS.mapper.PublisherMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.PublisherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/publishers")
@Tag(name = "Publishers", description = "Publisher management APIs - Manage book publisher information including names, addresses, and contact details")
public class PublisherController {
    
    private final PublisherService publisherService;
    private final PublisherMapper publisherMapper;
    
    public PublisherController(PublisherService publisherService, PublisherMapper publisherMapper) {
        this.publisherService = publisherService;
        this.publisherMapper = publisherMapper;
    }
    
    // Create a new publisher
    @Operation(
            summary = "Create a new publisher",
            description = "Creates a new book publisher with name, address, and contact information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Publisher created successfully",
                    content = @Content(schema = @Schema(implementation = PublisherResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "409", description = "Publisher with the same name already exists")
    })
    @PostMapping
    public ResponseEntity<PublisherResponseDTO> createPublisher(
            @Parameter(description = "Publisher details to create", required = true)
            @Valid @RequestBody PublisherRequestDTO requestDTO) {
        Publisher publisher = publisherMapper.toEntity(requestDTO);
        Publisher createdPublisher = publisherService.create(publisher);
        PublisherResponseDTO responseDTO = publisherMapper.toResponseDTO(createdPublisher);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get publisher by ID
    @Operation(
            summary = "Get publisher by ID",
            description = "Retrieves a specific publisher's details by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publisher found successfully",
                    content = @Content(schema = @Schema(implementation = PublisherResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Publisher not found with the given ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponseDTO> getPublisherById(
            @Parameter(description = "Publisher ID", required = true, example = "1")
            @PathVariable Long id) {
        Publisher publisher = publisherService.getById(id);
        PublisherResponseDTO responseDTO = publisherMapper.toResponseDTO(publisher);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all publishers
    @Operation(
            summary = "Get all publishers",
            description = "Retrieves a complete list of all book publishers in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publishers retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<PublisherResponseDTO>> getAllPublishers() {
        List<Publisher> publishers = publisherService.getAll();
        List<PublisherResponseDTO> responseDTOs = publishers.stream()
                .map(publisherMapper::toResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }
    
    // Update publisher by ID
    @Operation(
            summary = "Update publisher",
            description = "Updates an existing publisher's information with the provided details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publisher updated successfully",
                    content = @Content(schema = @Schema(implementation = PublisherResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "404", description = "Publisher not found with the given ID"),
            @ApiResponse(responseCode = "409", description = "Publisher name conflicts with existing publisher")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponseDTO> updatePublisher(
            @Parameter(description = "Publisher ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated publisher details", required = true)
            @Valid @RequestBody PublisherRequestDTO requestDTO) {
        Publisher publisher = publisherMapper.toEntity(requestDTO);
        Publisher updatedPublisher = publisherService.update(id, publisher);
        PublisherResponseDTO responseDTO = publisherMapper.toResponseDTO(updatedPublisher);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete publisher by ID
    @Operation(
            summary = "Delete publisher",
            description = "Permanently deletes a publisher from the system. Note: Cannot delete publishers with associated books."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Publisher deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Publisher not found with the given ID"),
            @ApiResponse(responseCode = "409", description = "Cannot delete publisher - books are associated with this publisher")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(
            @Parameter(description = "Publisher ID", required = true, example = "1")
            @PathVariable Long id) {
        publisherService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

