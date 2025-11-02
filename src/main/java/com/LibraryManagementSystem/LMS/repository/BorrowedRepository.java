package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.LibraryManagementSystem.LMS.entity.Borrowed;
import com.LibraryManagementSystem.LMS.entity.User;
import com.LibraryManagementSystem.LMS.entity.BookCopy;
import com.LibraryManagementSystem.LMS.enums.BorrowStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowedRepository extends JpaRepository<Borrowed, Long> {
    
    // Find all borrowed records by user
    List<Borrowed> findByUser(User user);
    
    // Find borrowed records by user ID
    List<Borrowed> findByUserId(Long userId);
    
    // Find borrowed records by status
    List<Borrowed> findByStatus(BorrowStatus status);
    
    // Find borrowed records by user and status
    List<Borrowed> findByUserAndStatus(User user, BorrowStatus status);
    
    // Find active borrowed records by user (not returned yet)
    @Query("SELECT b FROM Borrowed b WHERE b.user.id = :userId AND b.status = 'BORROWED'")
    List<Borrowed> findActiveBorrowsByUserId(@Param("userId") Long userId);
    
    // Find overdue borrowed records
    @Query("SELECT b FROM Borrowed b WHERE b.dueDate < :currentDate AND b.status = 'BORROWED'")
    List<Borrowed> findOverdueRecords(@Param("currentDate") LocalDate currentDate);
    
    // Find overdue records by user
    @Query("SELECT b FROM Borrowed b WHERE b.user.id = :userId AND b.dueDate < :currentDate AND b.status = 'BORROWED'")
    List<Borrowed> findOverdueRecordsByUserId(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);
    
    // Find borrowed records by book copy
    List<Borrowed> findByBookCopy(BookCopy bookCopy);
    
    // Find current borrow record for a book copy
    @Query("SELECT b FROM Borrowed b WHERE b.bookCopy.id = :bookCopyId AND b.status = 'BORROWED' ORDER BY b.borrowDate DESC")
    Optional<Borrowed> findCurrentBorrowByBookCopyId(@Param("bookCopyId") Long bookCopyId);
    
    // Find borrowed records by date range
    @Query("SELECT b FROM Borrowed b WHERE b.borrowDate BETWEEN :startDate AND :endDate")
    List<Borrowed> findByBorrowDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Count active borrows by user
    @Query("SELECT COUNT(b) FROM Borrowed b WHERE b.user.id = :userId AND b.status = 'BORROWED'")
    long countActiveBorrowsByUserId(@Param("userId") Long userId);
    
    // Find records due soon (within specified days)
    @Query("SELECT b FROM Borrowed b WHERE b.dueDate BETWEEN :today AND :futureDate AND b.status = 'BORROWED'")
    List<Borrowed> findRecordsDueSoon(@Param("today") LocalDate today, @Param("futureDate") LocalDate futureDate);
}
