package com.LibraryManagementSystem.LMS.dto;

import com.LibraryManagementSystem.LMS.enums.Role;
import com.LibraryManagementSystem.LMS.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class LibrarianResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private Role role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    private Status status;

    public LibrarianResponseDTO() {
    }

    public LibrarianResponseDTO(Long id, String name, String email, String phoneNumber,
                                String address, Role role, LocalDate hireDate, Status status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
        this.hireDate = hireDate;
        this.status = status;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
