package com.LibraryManagementSystem.LMS.service;

import com.LibraryManagementSystem.LMS.entity.User;
import com.LibraryManagementSystem.LMS.enums.Status;
import com.LibraryManagementSystem.LMS.exception.ResourceNotFoundException;
import com.LibraryManagementSystem.LMS.repository.UserRepository;
import com.LibraryManagementSystem.LMS.service.interfaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // Core CRUD methods
    
    @Override
    public User create(User user) {
        // Validate email uniqueness
        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalStateException("User with email " + user.getEmail() + " already exists");
        }
        
        // Validate phone number uniqueness
        if (user.getPhoneNumber() != null && userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new IllegalStateException("User with phone number " + user.getPhoneNumber() + " already exists");
        }
        
        return userRepository.save(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> getAllPaginated(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    @Override
    public User update(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // Validate email uniqueness if changed
        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalStateException("User with email " + user.getEmail() + " already exists");
            }
        }
        
        // Validate phone number uniqueness if changed
        if (user.getPhoneNumber() != null && !user.getPhoneNumber().equals(existingUser.getPhoneNumber())) {
            if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
                throw new IllegalStateException("User with phone number " + user.getPhoneNumber() + " already exists");
            }
        }
        
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setAddress(user.getAddress());
        existingUser.setMembershipDate(user.getMembershipDate());
        existingUser.setStatus(user.getStatus());
        
        return userRepository.save(existingUser);
    }
    
    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
    }
    
    // Find user by email
    
    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
    
    // Find users by status
    
    @Override
    @Transactional(readOnly = true)
    public List<User> findByStatus(Status status) {
        return userRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> findByStatus(Status status, Pageable pageable) {
        return userRepository.findByStatus(status, pageable);
    }
    
    // Find users by name
    
    @Override
    @Transactional(readOnly = true)
    public List<User> findByNameContainingIgnoreCase(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return userRepository.findByNameContainingIgnoreCase(name, pageable);
    }
    
    // Find user by phone number
    
    @Override
    @Transactional(readOnly = true)
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User", "phoneNumber", phoneNumber));
    }
    
    // Find users by membership date range
    
    @Override
    @Transactional(readOnly = true)
    public List<User> findByMembershipDateBetween(LocalDate startDate, LocalDate endDate) {
        return userRepository.findByMembershipDateBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> findByMembershipDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return userRepository.findByMembershipDateBetween(startDate, endDate, pageable);
    }
    
    // Find users with overdue books
    
    @Override
    @Transactional(readOnly = true)
    public List<User> findUsersWithOverdueBooks() {
        return userRepository.findUsersWithOverdueBooks(LocalDate.now());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> findUsersWithOverdueBooks(Pageable pageable) {
        return userRepository.findUsersWithOverdueBooks(LocalDate.now(), pageable);
    }
    
    // Find users with pending fines
    
    @Override
    @Transactional(readOnly = true)
    public List<User> findUsersWithPendingFines() {
        return userRepository.findUsersWithPendingFines();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> findUsersWithPendingFines(Pageable pageable) {
        return userRepository.findUsersWithPendingFines(pageable);
    }
    
    // Business logic methods
    
    @Override
    public User activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.setStatus(Status.ACTIVE);
        return userRepository.save(user);
    }
    
    @Override
    public User deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.setStatus(Status.INACTIVE);
        return userRepository.save(user);
    }
    
    @Override
    public User suspendUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        // Set to INACTIVE as there's no SUSPENDED status in the enum
        user.setStatus(Status.INACTIVE);
        return userRepository.save(user);
    }
    
    @Override
    public User changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        // Verify old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        
        // Validate new password
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters long");
        }
        
        // Set new password (will be encrypted by @PrePersist/@PreUpdate)
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
    
    // Validation methods
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByStatus(Status status) {
        return userRepository.countByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canUserBorrow(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        // Check if user is active
        if (user.getStatus() != Status.ACTIVE) {
            return false;
        }
        
        // Check if user has overdue books
        List<User> usersWithOverdueBooks = userRepository.findUsersWithOverdueBooks(LocalDate.now());
        if (usersWithOverdueBooks.stream().anyMatch(u -> u.getId().equals(userId))) {
            return false;
        }
        
        // Check if user has pending fines
        List<User> usersWithPendingFines = userRepository.findUsersWithPendingFines();
        if (usersWithPendingFines.stream().anyMatch(u -> u.getId().equals(userId))) {
            return false;
        }
        
        return true;
    }
}
