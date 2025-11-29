package com.LibraryManagementSystem.LMS.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserPatchDTO {
    
    @Email(message = "Must be a valid Email")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
