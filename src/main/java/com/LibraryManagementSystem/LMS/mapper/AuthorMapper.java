package com.LibraryManagementSystem.LMS.mapper;

import com.LibraryManagementSystem.LMS.dto.AuthorRequestDTO;
import com.LibraryManagementSystem.LMS.dto.AuthorResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    
    public AuthorResponseDTO toResponseDTO(Author author) {
        if (author == null) {
            return null;
        }
        
        AuthorResponseDTO dto = new AuthorResponseDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setBiography(author.getBiography());
        dto.setBirthDate(author.getBirthDate());
        dto.setNationality(author.getNationality());
        
        return dto;
    }
    
    public Author toEntity(AuthorRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        
        Author author = new Author();
        author.setName(requestDTO.getName());
        author.setBiography(requestDTO.getBiography());
        author.setBirthDate(requestDTO.getBirthDate());
        author.setNationality(requestDTO.getNationality());
        
        return author;
    }
    
    public void updateEntityFromRequest(Author author, AuthorRequestDTO requestDTO) {
        if (author == null || requestDTO == null) {
            return;
        }
        
        if (requestDTO.getName() != null) {
            author.setName(requestDTO.getName());
        }
        if (requestDTO.getBiography() != null) {
            author.setBiography(requestDTO.getBiography());
        }
        if (requestDTO.getBirthDate() != null) {
            author.setBirthDate(requestDTO.getBirthDate());
        }
        if (requestDTO.getNationality() != null) {
            author.setNationality(requestDTO.getNationality());
        }
    }
}

