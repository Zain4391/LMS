package com.LibraryManagementSystem.LMS.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PublisherRequestDTO {
    
    @NotBlank(message = "Publisher name is required")
    @Size(max = 200, message = "Publisher name must not exceed 200 characters")
    private String name;
    
    @Size(max = 300, message = "Address must not exceed 300 characters")
    private String address;
    
    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    private String email;
    
    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;
    
    public PublisherRequestDTO() {
    }
    
    public PublisherRequestDTO(String name, String address, String email, String country) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.country = country;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
}

