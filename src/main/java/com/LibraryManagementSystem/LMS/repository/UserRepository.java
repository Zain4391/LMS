package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<User> findByStatus(Status status, Pageable pageable);
    
    // Find users by name containing (case-insensitive)
    List<User> findByNameContainingIgnoreCase(String name);
    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // Find users by phone number
    Optional<User> findByPhoneNumber(String phoneNumber);
    
    // Find users by membership date range
    @Query("SELECT u FROM User u WHERE u.membershipDate BETWEEN :startDate AND :endDate")
    List<User> findByMembershipDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT u FROM User u WHERE u.membershipDate BETWEEN :startDate AND :endDate")
    Page<User> findByMembershipDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    
    // Find users with overdue books
    @Query("SELECT DISTINCT u FROM User u JOIN Borrowed b ON u.id = b.user.id " +
           "WHERE b.dueDate < :currentDate AND b.status = com.LibraryManagementSystem.LMS.enums.BorrowStatus.BORROWED")
    List<User> findUsersWithOverdueBooks(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT DISTINCT u FROM User u JOIN Borrowed b ON u.id = b.user.id " +
           "WHERE b.dueDate < :currentDate AND b.status = com.LibraryManagementSystem.LMS.enums.BorrowStatus.BORROWED")
    Page<User> findUsersWithOverdueBooks(@Param("currentDate") LocalDate currentDate, Pageable pageable);
    
    // Find users with pending fines
    @Query("SELECT DISTINCT u FROM User u JOIN Borrowed b ON u.id = b.user.id " +
           "JOIN Fine f ON b.id = f.borrowed.id WHERE f.status = com.LibraryManagementSystem.LMS.enums.FineStatus.PENDING")
    List<User> findUsersWithPendingFines();
    
    @Query("SELECT DISTINCT u FROM User u JOIN Borrowed b ON u.id = b.user.id " +
           "JOIN Fine f ON b.id = f.borrowed.id WHERE f.status = com.LibraryManagementSystem.LMS.enums.FineStatus.PENDING")
    Page<User> findUsersWithPendingFines(Pageable pageable);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Check if phone number exists
    boolean existsByPhoneNumber(String phoneNumber);
    
    // Count users by status
    long countByStatus(Status status);
}
