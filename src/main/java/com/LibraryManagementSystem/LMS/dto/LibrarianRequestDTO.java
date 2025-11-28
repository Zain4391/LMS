package com.LibraryManagementSystem.LMS.dto;

import com.LibraryManagementSystem.LMS.enums.Role;
import com.LibraryManagementSystem.LMS.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class LibrarianRequestDTO {

    @NotBlank(message = "Librarian name is required")
    @Size(max = 100, message = "Librarian name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    @Size(max = 200, message = "Address must not exceed 200 characters")
    private String address;

    private Role role; // Optional - defaults to STAFF if not provided

    private LocalDate hireDate; // Optional - defaults to current date if not provided

    private Status status; // Optional - defaults to ACTIVE if not provided

    public LibrarianRequestDTO() {
    }

    public LibrarianRequestDTO(String name, String email, String password, String phoneNumber,
                               String address, Role role, LocalDate hireDate, Status status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
        this.hireDate = hireDate;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
