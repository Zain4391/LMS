package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.UserPatchDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Update user email
    @Operation(
        summary = "update email",
        description = "Updates an existing user's email"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email updated successfully",
                    content = @Content(schema = @Schema(implementation = UserPatchDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "404", description = "User not found with the given ID"),
            @ApiResponse(responseCode = "409", description = "Email or phone number conflicts with existing user")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> patchUserEmail(
        @Parameter(description = "User ID", required = true, example = "1")
        @PathVariable Long id,
        @Parameter(description = "Update user email", required = true)
        @Valid @RequestBody UserPatchDTO patchDTO
    ) {
        User updatedUser = userService.patchEmail(id, patchDTO);
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
}

