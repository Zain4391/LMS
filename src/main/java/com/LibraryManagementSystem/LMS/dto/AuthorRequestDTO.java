package com.LibraryManagementSystem.LMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class AuthorRequestDTO {
    
    @NotBlank(message = "Author name is required")
    @Size(max = 200, message = "Author name must not exceed 200 characters")
    private String name;
    
    @Size(max = 2000, message = "Biography must not exceed 2000 characters")
    private String biography;
    
    private LocalDate birthDate;
    
    @Size(max = 100, message = "Nationality must not exceed 100 characters")
    private String nationality;
    
    public AuthorRequestDTO() {
    }
    
    public AuthorRequestDTO(String name, String biography, LocalDate birthDate, String nationality) {
        this.name = name;
        this.biography = biography;
        this.birthDate = birthDate;
        this.nationality = nationality;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getBiography() {
        return biography;
    }
    
    public void setBiography(String biography) {
        this.biography = biography;
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}

