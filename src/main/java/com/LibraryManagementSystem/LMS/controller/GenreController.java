package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.GenreRequestDTO;
import com.LibraryManagementSystem.LMS.dto.GenreResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Genre;
import com.LibraryManagementSystem.LMS.mapper.GenreMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.GenreService;

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
@RequestMapping("/api/genres")
@Tag(name = "Genres", description = "Genre management APIs - Manage book genre categories and classifications")
public class GenreController {
    
    private final GenreService genreService;
    private final GenreMapper genreMapper;
    
    public GenreController(GenreService genreService, GenreMapper genreMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
    }
    
    // Create a new genre
    @Operation(
            summary = "Create a new genre",
            description = "Creates a new book genre category with name and optional description"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Genre created successfully",
                    content = @Content(schema = @Schema(implementation = GenreResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "409", description = "Genre with the same name already exists")
    })
    @PostMapping
    public ResponseEntity<GenreResponseDTO> createGenre(
            @Parameter(description = "Genre details to create", required = true)
            @Valid @RequestBody GenreRequestDTO requestDTO) {
        Genre genre = genreMapper.toEntity(requestDTO);
        Genre createdGenre = genreService.create(genre);
        GenreResponseDTO responseDTO = genreMapper.toResponseDTO(createdGenre);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get genre by ID
    @Operation(
            summary = "Get genre by ID",
            description = "Retrieves a specific genre's details by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre found successfully",
                    content = @Content(schema = @Schema(implementation = GenreResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Genre not found with the given ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> getGenreById(
            @Parameter(description = "Genre ID", required = true, example = "1")
            @PathVariable Long id) {
        Genre genre = genreService.getById(id);
        GenreResponseDTO responseDTO = genreMapper.toResponseDTO(genre);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all genres
    @Operation(
            summary = "Get all genres",
            description = "Retrieves a complete list of all book genres in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genres retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<GenreResponseDTO>> getAllGenres() {
        List<Genre> genres = genreService.getAll();
        List<GenreResponseDTO> responseDTOs = genres.stream()
                .map(genreMapper::toResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }
    
    // Update genre by ID
    @Operation(
            summary = "Update genre",
            description = "Updates an existing genre's information with the provided details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre updated successfully",
                    content = @Content(schema = @Schema(implementation = GenreResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "404", description = "Genre not found with the given ID"),
            @ApiResponse(responseCode = "409", description = "Genre name conflicts with existing genre")
    })
    @PutMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> updateGenre(
            @Parameter(description = "Genre ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated genre details", required = true)
            @Valid @RequestBody GenreRequestDTO requestDTO) {
        Genre genre = genreMapper.toEntity(requestDTO);
        Genre updatedGenre = genreService.update(id, genre);
        GenreResponseDTO responseDTO = genreMapper.toResponseDTO(updatedGenre);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete genre by ID
    @Operation(
            summary = "Delete genre",
            description = "Permanently deletes a genre from the system. Note: Cannot delete genres associated with books."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Genre deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Genre not found with the given ID"),
            @ApiResponse(responseCode = "409", description = "Cannot delete genre - books are associated with this genre")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(
            @Parameter(description = "Genre ID", required = true, example = "1")
            @PathVariable Long id) {
        genreService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

