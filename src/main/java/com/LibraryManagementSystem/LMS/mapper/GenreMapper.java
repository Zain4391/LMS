package com.LibraryManagementSystem.LMS.mapper;

import com.LibraryManagementSystem.LMS.dto.GenreRequestDTO;
import com.LibraryManagementSystem.LMS.dto.GenreResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {
    
    public GenreResponseDTO toResponseDTO(Genre genre) {
        if (genre == null) {
            return null;
        }
        
        GenreResponseDTO dto = new GenreResponseDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        dto.setDescription(genre.getDescription());
        
        return dto;
    }
    
    public Genre toEntity(GenreRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        
        Genre genre = new Genre();
        genre.setName(requestDTO.getName());
        genre.setDescription(requestDTO.getDescription());
        
        return genre;
    }
    
    public void updateEntityFromRequest(Genre genre, GenreRequestDTO requestDTO) {
        if (genre == null || requestDTO == null) {
            return;
        }
        
        if (requestDTO.getName() != null) {
            genre.setName(requestDTO.getName());
        }
        if (requestDTO.getDescription() != null) {
            genre.setDescription(requestDTO.getDescription());
        }
    }
}

