package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.UserRequestDTO;
import com.LibraryManagementSystem.LMS.dto.UserResponseDTO;
import com.LibraryManagementSystem.LMS.entity.User;
import com.LibraryManagementSystem.LMS.enums.Status;
import com.LibraryManagementSystem.LMS.mapper.UserMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    private final UserMapper userMapper;
    
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }
    
    // Create new User
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        User user = userMapper.toEntity(requestDTO);
        User createdUser = userService.create(user);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(createdUser);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get User by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(user);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all Users (with optional pagination)
    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<User> userPage = userService.getAllPaginated(pageable);
            Page<UserResponseDTO> responsePage = userPage.map(userMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<User> userList = userService.getAll();
            List<UserResponseDTO> responseDTOs = userList.stream()
                    .map(userMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Update User by ID
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO requestDTO) {
        User user = userMapper.toEntity(requestDTO);
        User updatedUser = userService.update(id, user);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(updatedUser);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete User by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Get User by Email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(user);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get Users by Status
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getUsersByStatus(
            @PathVariable Status status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<User> userPage = userService.findByStatus(status, pageable);
            Page<UserResponseDTO> responsePage = userPage.map(userMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<User> userList = userService.findByStatus(status);
            List<UserResponseDTO> responseDTOs = userList.stream()
                    .map(userMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Search Users by Name
    @GetMapping("/search/name")
    public ResponseEntity<?> searchUsersByName(
            @RequestParam String name,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<User> userPage = userService.findByNameContainingIgnoreCase(name, pageable);
            Page<UserResponseDTO> responsePage = userPage.map(userMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<User> userList = userService.findByNameContainingIgnoreCase(name);
            List<UserResponseDTO> responseDTOs = userList.stream()
                    .map(userMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get User by Phone Number
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<UserResponseDTO> getUserByPhoneNumber(@PathVariable String phoneNumber) {
        User user = userService.findByPhoneNumber(phoneNumber);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(user);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get Users by Membership Date Range
    @GetMapping("/search/membership-date")
    public ResponseEntity<?> getUsersByMembershipDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "membershipDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<User> userPage = userService.findByMembershipDateBetween(startDate, endDate, pageable);
            Page<UserResponseDTO> responsePage = userPage.map(userMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<User> userList = userService.findByMembershipDateBetween(startDate, endDate);
            List<UserResponseDTO> responseDTOs = userList.stream()
                    .map(userMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get Users with Overdue Books
    @GetMapping("/overdue-books")
    public ResponseEntity<?> getUsersWithOverdueBooks(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<User> userPage = userService.findUsersWithOverdueBooks(pageable);
            Page<UserResponseDTO> responsePage = userPage.map(userMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<User> userList = userService.findUsersWithOverdueBooks();
            List<UserResponseDTO> responseDTOs = userList.stream()
                    .map(userMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get Users with Pending Fines
    @GetMapping("/pending-fines")
    public ResponseEntity<?> getUsersWithPendingFines(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<User> userPage = userService.findUsersWithPendingFines(pageable);
            Page<UserResponseDTO> responsePage = userPage.map(userMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<User> userList = userService.findUsersWithPendingFines();
            List<UserResponseDTO> responseDTOs = userList.stream()
                    .map(userMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Activate User
    @PostMapping("/{id}/activate")
    public ResponseEntity<UserResponseDTO> activateUser(@PathVariable Long id) {
        User activatedUser = userService.activateUser(id);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(activatedUser);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Deactivate User
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<UserResponseDTO> deactivateUser(@PathVariable Long id) {
        User deactivatedUser = userService.deactivateUser(id);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(deactivatedUser);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Suspend User
    @PostMapping("/{id}/suspend")
    public ResponseEntity<UserResponseDTO> suspendUser(@PathVariable Long id) {
        User suspendedUser = userService.suspendUser(id);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(suspendedUser);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Change Password
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @PathVariable Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        userService.changePassword(id, oldPassword, newPassword);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Check if Email Exists
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Check if Phone Number Exists
    @GetMapping("/exists/phone/{phoneNumber}")
    public ResponseEntity<Map<String, Boolean>> checkPhoneNumberExists(@PathVariable String phoneNumber) {
        boolean exists = userService.existsByPhoneNumber(phoneNumber);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Count Users by Status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Map<String, Long>> countUsersByStatus(@PathVariable Status status) {
        long count = userService.countByStatus(status);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Check if User Can Borrow
    @GetMapping("/{id}/can-borrow")
    public ResponseEntity<Map<String, Boolean>> checkCanUserBorrow(@PathVariable Long id) {
        boolean canBorrow = userService.canUserBorrow(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("canBorrow", canBorrow);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

