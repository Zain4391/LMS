package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.UserRequestDTO;
import com.LibraryManagementSystem.LMS.dto.UserResponseDTO;
import com.LibraryManagementSystem.LMS.entity.User;
import com.LibraryManagementSystem.LMS.enums.Status;
import com.LibraryManagementSystem.LMS.mapper.UserMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "User management APIs - Manage library member accounts, memberships, and borrowing privileges")
public class UserController {
    
    private final UserService userService;
    private final UserMapper userMapper;
    
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }
    
    // Create new User
    @Operation(
            summary = "Create a new user",
            description = "Registers a new library member with personal details and membership information. Password is automatically encrypted."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "409", description = "User with the same email or phone number already exists")
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Parameter(description = "User details for registration", required = true)
            @Valid @RequestBody UserRequestDTO requestDTO) {
        User user = userMapper.toEntity(requestDTO);
        User createdUser = userService.create(user);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(createdUser);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get User by ID
    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a specific user's details by their unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found with the given ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id) {
        User user = userService.getById(id);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(user);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all Users (with optional pagination)
    @Operation(
            summary = "Get all users",
            description = "Retrieves all library members with optional pagination and sorting"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
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
    @Operation(
            summary = "Update user",
            description = "Updates an existing user's information with the provided details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "404", description = "User not found with the given ID"),
            @ApiResponse(responseCode = "409", description = "Email or phone number conflicts with existing user")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated user details", required = true)
            @Valid @RequestBody UserRequestDTO requestDTO) {
        User user = userMapper.toEntity(requestDTO);
        User updatedUser = userService.update(id, user);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(updatedUser);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete User by ID
    @Operation(
            summary = "Delete user",
            description = "Permanently deletes a user from the system. Note: Cannot delete users with active borrows or pending fines."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found with the given ID"),
            @ApiResponse(responseCode = "409", description = "Cannot delete user - active borrows or pending fines exist")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Get User by Email
    @Operation(
            summary = "Get user by email",
            description = "Retrieves a user's details by their email address"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found with the given email")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(
            @Parameter(description = "User email address", required = true, example = "john.doe@example.com")
            @PathVariable String email) {
        User user = userService.findByEmail(email);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(user);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get Users by Status
    @Operation(
            summary = "Get users by status",
            description = "Retrieves all users with a specific status (ACTIVE, INACTIVE, SUSPENDED) with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getUsersByStatus(
            @Parameter(description = "User status", required = true, example = "ACTIVE")
            @PathVariable Status status,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
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
    @Operation(
            summary = "Search users by name",
            description = "Searches for users whose names contain the specified text (case-insensitive) with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping("/search/name")
    public ResponseEntity<?> searchUsersByName(
            @Parameter(description = "Name to search for", required = true, example = "John")
            @RequestParam String name,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
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
    @Operation(
            summary = "Get user by phone number",
            description = "Retrieves a user's details by their phone number"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found with the given phone number")
    })
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<UserResponseDTO> getUserByPhoneNumber(
            @Parameter(description = "User phone number", required = true, example = "123-456-7890")
            @PathVariable String phoneNumber) {
        User user = userService.findByPhoneNumber(phoneNumber);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(user);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get Users by Membership Date Range
    @Operation(
            summary = "Get users by membership date range",
            description = "Retrieves users who joined within a specific date range with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping("/search/membership-date")
    public ResponseEntity<?> getUsersByMembershipDateRange(
            @Parameter(description = "Start date", required = true, example = "2024-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", required = true, example = "2024-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "membershipDate")
            @RequestParam(defaultValue = "membershipDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
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
    @Operation(
            summary = "Get users with overdue books",
            description = "Retrieves all users who currently have overdue borrow records with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping("/overdue-books")
    public ResponseEntity<?> getUsersWithOverdueBooks(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
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
    @Operation(
            summary = "Get users with pending fines",
            description = "Retrieves all users who have unpaid fines with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping("/pending-fines")
    public ResponseEntity<?> getUsersWithPendingFines(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
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
    @Operation(
            summary = "Activate user account",
            description = "Changes a user's status to ACTIVE, restoring their borrowing privileges"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User activated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/{id}/activate")
    public ResponseEntity<UserResponseDTO> activateUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id) {
        User activatedUser = userService.activateUser(id);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(activatedUser);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Deactivate User
    @Operation(
            summary = "Deactivate user account",
            description = "Changes a user's status to INACTIVE, temporarily suspending their borrowing privileges"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deactivated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<UserResponseDTO> deactivateUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id) {
        User deactivatedUser = userService.deactivateUser(id);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(deactivatedUser);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Suspend User
    @Operation(
            summary = "Suspend user account",
            description = "Changes a user's status to SUSPENDED, blocking all borrowing privileges due to violations or unpaid fines"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User suspended successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/{id}/suspend")
    public ResponseEntity<UserResponseDTO> suspendUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id) {
        User suspendedUser = userService.suspendUser(id);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(suspendedUser);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Change Password
    @Operation(
            summary = "Change user password",
            description = "Updates a user's password after verifying the old password. New password must be at least 6 characters."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password - Old password incorrect or new password too short"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Current password", required = true, example = "OldPass123")
            @RequestParam String oldPassword,
            @Parameter(description = "New password (min 6 characters)", required = true, example = "NewSecurePass456")
            @RequestParam String newPassword) {
        userService.changePassword(id, oldPassword, newPassword);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Check if Email Exists
    @Operation(
            summary = "Check if email exists",
            description = "Validates whether an email address is already registered in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(
            @Parameter(description = "Email address to check", required = true, example = "john.doe@example.com")
            @PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Check if Phone Number Exists
    @Operation(
            summary = "Check if phone number exists",
            description = "Validates whether a phone number is already registered in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    @GetMapping("/exists/phone/{phoneNumber}")
    public ResponseEntity<Map<String, Boolean>> checkPhoneNumberExists(
            @Parameter(description = "Phone number to check", required = true, example = "123-456-7890")
            @PathVariable String phoneNumber) {
        boolean exists = userService.existsByPhoneNumber(phoneNumber);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Count Users by Status
    @Operation(
            summary = "Count users by status",
            description = "Returns the total count of users with a specific status (ACTIVE, INACTIVE, SUSPENDED)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Map<String, Long>> countUsersByStatus(
            @Parameter(description = "User status", required = true, example = "ACTIVE")
            @PathVariable Status status) {
        long count = userService.countByStatus(status);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Check if User Can Borrow
    @Operation(
            summary = "Check if user can borrow",
            description = "Validates whether a user is eligible to borrow books (active status, no pending fines, within borrow limit)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eligibility check completed successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}/can-borrow")
    public ResponseEntity<Map<String, Boolean>> checkCanUserBorrow(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id) {
        boolean canBorrow = userService.canUserBorrow(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("canBorrow", canBorrow);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

