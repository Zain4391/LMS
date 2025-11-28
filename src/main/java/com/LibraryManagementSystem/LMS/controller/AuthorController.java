package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.AuthorRequestDTO;
import com.LibraryManagementSystem.LMS.dto.AuthorResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Author;
import com.LibraryManagementSystem.LMS.mapper.AuthorMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.AuthorService;

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
@RequestMapping("/api/authors")
@Tag(name = "Authors", description = "Author management APIs - Manage author information including biographies and nationalities")
public class AuthorController {
    
    private final AuthorService authorService;
    private final AuthorMapper authorMapper;
    
    public AuthorController(AuthorService authorService, AuthorMapper authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }
    
    @Operation(
            summary = "Create a new author",
            description = "Creates a new author with the provided details including name, biography, birth date, and nationality"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author created successfully",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "409", description = "Author with the same name already exists")
    })
    @PostMapping
    public ResponseEntity<AuthorResponseDTO> createAuthor(
            @Parameter(description = "Author details to create", required = true)
            @Valid @RequestBody AuthorRequestDTO requestDTO) {
        Author author = authorMapper.toEntity(requestDTO);
        Author createdAuthor = authorService.create(author);
        AuthorResponseDTO responseDTO = authorMapper.toResponseDTO(createdAuthor);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    @Operation(
            summary = "Get author by ID",
            description = "Retrieves a specific author's details by their unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author found successfully",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Author not found with the given ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> getAuthorById(
            @Parameter(description = "Author ID", required = true, example = "1")
            @PathVariable Long id) {
        Author author = authorService.getById(id);
        AuthorResponseDTO responseDTO = authorMapper.toResponseDTO(author);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    @Operation(
            summary = "Get all authors",
            description = "Retrieves a complete list of all authors in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authors retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors() {
        List<Author> authors = authorService.getAll();
        List<AuthorResponseDTO> responseDTOs = authors.stream()
                .map(authorMapper::toResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }
    
    @Operation(
            summary = "Update author",
            description = "Updates an existing author's information with the provided details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated successfully",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "404", description = "Author not found with the given ID"),
            @ApiResponse(responseCode = "409", description = "Author name conflicts with existing author")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> updateAuthor(
            @Parameter(description = "Author ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated author details", required = true)
            @Valid @RequestBody AuthorRequestDTO requestDTO) {
        Author author = authorMapper.toEntity(requestDTO);
        Author updatedAuthor = authorService.update(id, author);
        AuthorResponseDTO responseDTO = authorMapper.toResponseDTO(updatedAuthor);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    @Operation(
            summary = "Delete author",
            description = "Permanently deletes an author from the system. Note: Cannot delete authors who have books associated with them."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found with the given ID"),
            @ApiResponse(responseCode = "409", description = "Cannot delete author - books are associated with this author")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(
            @Parameter(description = "Author ID", required = true, example = "1")
            @PathVariable Long id) {
        authorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

