package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.AuthResponseDTO;
import com.LibraryManagementSystem.LMS.dto.LoginRequestDTO;
import com.LibraryManagementSystem.LMS.dto.UserRequestDTO;
import com.LibraryManagementSystem.LMS.dto.UserResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Librarian;
import com.LibraryManagementSystem.LMS.entity.User;
import com.LibraryManagementSystem.LMS.enums.Status;
import com.LibraryManagementSystem.LMS.mapper.UserMapper;
import com.LibraryManagementSystem.LMS.security.JwtUtil;
import com.LibraryManagementSystem.LMS.service.interfaces.LibrarianService;
import com.LibraryManagementSystem.LMS.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints for Users and Librarians")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final UserService userService;
    private final LibrarianService librarianService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthController(UserService userService, 
                         LibrarianService librarianService, 
                         JwtUtil jwtUtil,
                         PasswordEncoder passwordEncoder,
                         UserMapper userMapper) {
        this.userService = userService;
        this.librarianService = librarianService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    // Register new User
    @Operation(
            summary = "Register a new user",
            description = "Creates a new library member account with personal details and membership information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "409", description = "User with the same email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(
            @Parameter(description = "User registration details", required = true)
            @Valid @RequestBody UserRequestDTO requestDTO) {
        
        logger.info("Registration attempt for email: {}", requestDTO.getEmail());
        
        User user = userMapper.toEntity(requestDTO);
        User createdUser = userService.create(user);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(createdUser);
        
        logger.info("User registered successfully: {}", createdUser.getEmail());
        
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // User Login
    @Operation(
            summary = "User login",
            description = "Authenticates a library member and returns a JWT token for accessing protected resources"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid email or password"),
            @ApiResponse(responseCode = "403", description = "Account is disabled or inactive")
    })
    @PostMapping("/user/login")
    public ResponseEntity<AuthResponseDTO> userLogin(
            @Parameter(description = "User login credentials", required = true)
            @Valid @RequestBody LoginRequestDTO loginRequest) {
        
        logger.info("User login attempt for email: {}", loginRequest.getEmail());
        
        // Find user by email
        User user = userService.findByEmail(loginRequest.getEmail());
        if (user == null) {
            logger.warn("User login failed - email not found: {}", loginRequest.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }
        
        // Check account status
        if (user.getStatus() != Status.ACTIVE) {
            logger.warn("User login failed - account not active: {}", loginRequest.getEmail());
            throw new DisabledException("Account is not active");
        }
        
        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            logger.warn("User login failed - invalid password: {}", loginRequest.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), "USER");
        
        logger.info("User login successful: {}", user.getEmail());
        
        AuthResponseDTO response = new AuthResponseDTO(
            token,
            user.getEmail(),
            "USER",
            "Login successful"
        );
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Librarian Login
    @Operation(
            summary = "Librarian login",
            description = "Authenticates a librarian (staff or admin) and returns a JWT token for accessing protected resources"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid email or password"),
            @ApiResponse(responseCode = "403", description = "Account is disabled or inactive")
    })
    @PostMapping("/librarian/login")
    public ResponseEntity<AuthResponseDTO> librarianLogin(
            @Parameter(description = "Librarian login credentials", required = true)
            @Valid @RequestBody LoginRequestDTO loginRequest) {
        
        logger.info("Librarian login attempt for email: {}", loginRequest.getEmail());
        
        // Find librarian by email
        Librarian librarian = librarianService.findByEmail(loginRequest.getEmail());
        if (librarian == null) {
            logger.warn("Librarian login failed - email not found: {}", loginRequest.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }
        
        // Check account status
        if (librarian.getStatus() != Status.ACTIVE) {
            logger.warn("Librarian login failed - account not active: {}", loginRequest.getEmail());
            throw new DisabledException("Account is not active");
        }
        
        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), librarian.getPassword())) {
            logger.warn("Librarian login failed - invalid password: {}", loginRequest.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }
        
        // Generate JWT token with role
        String token = jwtUtil.generateToken(librarian.getEmail(), librarian.getRole().name());
        
        logger.info("Librarian login successful: {} with role: {}", librarian.getEmail(), librarian.getRole());
        
        AuthResponseDTO response = new AuthResponseDTO(
            token,
            librarian.getEmail(),
            librarian.getRole().name(),
            "Login successful"
        );
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Verify token endpoint
    @Operation(
            summary = "Verify JWT token",
            description = "Validates a JWT token and returns the associated user information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Token is invalid or expired")
    })
    @GetMapping("/verify")
    public ResponseEntity<AuthResponseDTO> verifyToken(
            @RequestHeader("Authorization") String authHeader) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("Invalid authorization header");
        }
        
        String token = authHeader.substring(7);
        
        if (jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractEmail(token);
            String role = jwtUtil.extractRole(token);
            
            logger.info("Token verified for: {} with role: {}", email, role);
            
            AuthResponseDTO response = new AuthResponseDTO(
                token,
                email,
                role,
                "Token is valid"
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        
        throw new BadCredentialsException("Invalid token");
    }

}
