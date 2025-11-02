package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.LibraryManagementSystem.LMS.entity.User;
import com.LibraryManagementSystem.LMS.enums.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find user by email
    Optional<User> findByEmail(String email);
    
    // Find users by status
    List<User> findByStatus(Status status);
    
    // Find users by name containing (case-insensitive)
    List<User> findByNameContainingIgnoreCase(String name);
    
    // Find users by phone number
    Optional<User> findByPhoneNumber(String phoneNumber);
    
    // Find users by membership date range
    @Query("SELECT u FROM User u WHERE u.membershipDate BETWEEN :startDate AND :endDate")
    List<User> findByMembershipDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Find users with overdue books
    @Query("SELECT DISTINCT u FROM User u JOIN Borrowed b ON u.id = b.user.id " +
           "WHERE b.dueDate < :currentDate AND b.status = 'BORROWED'")
    List<User> findUsersWithOverdueBooks(@Param("currentDate") LocalDate currentDate);
    
    // Find users with pending fines
    @Query("SELECT DISTINCT u FROM User u JOIN Borrowed b ON u.id = b.user.id " +
           "JOIN Fine f ON b.id = f.borrowed.id WHERE f.status = 'PENDING'")
    List<User> findUsersWithPendingFines();
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Check if phone number exists
    boolean existsByPhoneNumber(String phoneNumber);
    
    // Count users by status
    long countByStatus(Status status);
}
