package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.AuthorRequestDTO;
import com.LibraryManagementSystem.LMS.dto.AuthorResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Author;
import com.LibraryManagementSystem.LMS.mapper.AuthorMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.AuthorService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    
    private final AuthorService authorService;
    private final AuthorMapper authorMapper;
    
    public AuthorController(AuthorService authorService, AuthorMapper authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }
    
    // Create a new author
    @PostMapping
    public ResponseEntity<AuthorResponseDTO> createAuthor(@Valid @RequestBody AuthorRequestDTO requestDTO) {
        Author author = authorMapper.toEntity(requestDTO);
        Author createdAuthor = authorService.create(author);
        AuthorResponseDTO responseDTO = authorMapper.toResponseDTO(createdAuthor);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get author by ID
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> getAuthorById(@PathVariable Long id) {
        Author author = authorService.getById(id);
        AuthorResponseDTO responseDTO = authorMapper.toResponseDTO(author);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all authors
    @GetMapping
    public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors() {
        List<Author> authors = authorService.getAll();
        List<AuthorResponseDTO> responseDTOs = authors.stream()
                .map(authorMapper::toResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }
    
    // Update author by ID
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequestDTO requestDTO) {
        Author author = authorMapper.toEntity(requestDTO);
        Author updatedAuthor = authorService.update(id, author);
        AuthorResponseDTO responseDTO = authorMapper.toResponseDTO(updatedAuthor);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete author by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

