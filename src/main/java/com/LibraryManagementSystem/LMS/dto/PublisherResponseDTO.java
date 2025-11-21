package com.LibraryManagementSystem.LMS.dto;

public class PublisherResponseDTO {
    
    private Long id;
    private String name;
    private String address;
    private String email;
    private String country;
    
    public PublisherResponseDTO() {
    }
    
    public PublisherResponseDTO(Long id, String name, String address, String email, String country) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.country = country;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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

