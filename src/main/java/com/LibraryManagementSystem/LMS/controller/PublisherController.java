package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.PublisherRequestDTO;
import com.LibraryManagementSystem.LMS.dto.PublisherResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Publisher;
import com.LibraryManagementSystem.LMS.mapper.PublisherMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.PublisherService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {
    
    private final PublisherService publisherService;
    private final PublisherMapper publisherMapper;
    
    public PublisherController(PublisherService publisherService, PublisherMapper publisherMapper) {
        this.publisherService = publisherService;
        this.publisherMapper = publisherMapper;
    }
    
    // Create a new publisher
    @PostMapping
    public ResponseEntity<PublisherResponseDTO> createPublisher(@Valid @RequestBody PublisherRequestDTO requestDTO) {
        Publisher publisher = publisherMapper.toEntity(requestDTO);
        Publisher createdPublisher = publisherService.create(publisher);
        PublisherResponseDTO responseDTO = publisherMapper.toResponseDTO(createdPublisher);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get publisher by ID
    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponseDTO> getPublisherById(@PathVariable Long id) {
        Publisher publisher = publisherService.getById(id);
        PublisherResponseDTO responseDTO = publisherMapper.toResponseDTO(publisher);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all publishers
    @GetMapping
    public ResponseEntity<List<PublisherResponseDTO>> getAllPublishers() {
        List<Publisher> publishers = publisherService.getAll();
        List<PublisherResponseDTO> responseDTOs = publishers.stream()
                .map(publisherMapper::toResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }
    
    // Update publisher by ID
    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponseDTO> updatePublisher(
            @PathVariable Long id,
            @Valid @RequestBody PublisherRequestDTO requestDTO) {
        Publisher publisher = publisherMapper.toEntity(requestDTO);
        Publisher updatedPublisher = publisherService.update(id, publisher);
        PublisherResponseDTO responseDTO = publisherMapper.toResponseDTO(updatedPublisher);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete publisher by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        publisherService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

