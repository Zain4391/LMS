package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.GenreRequestDTO;
import com.LibraryManagementSystem.LMS.dto.GenreResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Genre;
import com.LibraryManagementSystem.LMS.mapper.GenreMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.GenreService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/genres")
public class GenreController {
    
    private final GenreService genreService;
    private final GenreMapper genreMapper;
    
    public GenreController(GenreService genreService, GenreMapper genreMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
    }
    
    // Create a new genre
    @PostMapping
    public ResponseEntity<GenreResponseDTO> createGenre(@Valid @RequestBody GenreRequestDTO requestDTO) {
        Genre genre = genreMapper.toEntity(requestDTO);
        Genre createdGenre = genreService.create(genre);
        GenreResponseDTO responseDTO = genreMapper.toResponseDTO(createdGenre);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get genre by ID
    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> getGenreById(@PathVariable Long id) {
        Genre genre = genreService.getById(id);
        GenreResponseDTO responseDTO = genreMapper.toResponseDTO(genre);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all genres
    @GetMapping
    public ResponseEntity<List<GenreResponseDTO>> getAllGenres() {
        List<Genre> genres = genreService.getAll();
        List<GenreResponseDTO> responseDTOs = genres.stream()
                .map(genreMapper::toResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }
    
    // Update genre by ID
    @PutMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> updateGenre(
            @PathVariable Long id,
            @Valid @RequestBody GenreRequestDTO requestDTO) {
        Genre genre = genreMapper.toEntity(requestDTO);
        Genre updatedGenre = genreService.update(id, genre);
        GenreResponseDTO responseDTO = genreMapper.toResponseDTO(updatedGenre);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete genre by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

