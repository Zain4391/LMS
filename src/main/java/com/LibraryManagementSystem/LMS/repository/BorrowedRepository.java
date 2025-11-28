package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    List<Borrowed> findByUser(User user);
    Page<Borrowed> findByUser(User user, Pageable pageable);
    
    List<Borrowed> findByUserId(Long userId);
    Page<Borrowed> findByUserId(Long userId, Pageable pageable);
    
    List<Borrowed> findByStatus(BorrowStatus status);
    Page<Borrowed> findByStatus(BorrowStatus status, Pageable pageable);
    
    List<Borrowed> findByUserAndStatus(User user, BorrowStatus status);
    Page<Borrowed> findByUserAndStatus(User user, BorrowStatus status, Pageable pageable);
    
    List<Borrowed> findByUserIdAndStatus(Long userId, BorrowStatus status);
    Page<Borrowed> findByUserIdAndStatus(Long userId, BorrowStatus status, Pageable pageable);
    
    @Query("SELECT b FROM Borrowed b WHERE b.user.id = :userId AND b.status = com.LibraryManagementSystem.LMS.enums.BorrowStatus.BORROWED")
    List<Borrowed> findActiveBorrowsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT b FROM Borrowed b WHERE b.user.id = :userId AND b.status = com.LibraryManagementSystem.LMS.enums.BorrowStatus.BORROWED")
    Page<Borrowed> findActiveBorrowsByUserId(@Param("userId") Long userId, Pageable pageable);
    
    // Find overdue borrowed records
    @Query("SELECT b FROM Borrowed b WHERE b.dueDate < :currentDate AND b.status = com.LibraryManagementSystem.LMS.enums.BorrowStatus.BORROWED")
    List<Borrowed> findOverdueRecords(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT b FROM Borrowed b WHERE b.dueDate < :currentDate AND b.status = com.LibraryManagementSystem.LMS.enums.BorrowStatus.BORROWED")
    Page<Borrowed> findOverdueRecords(@Param("currentDate") LocalDate currentDate, Pageable pageable);
    
    // Find overdue records by user
    @Query("SELECT b FROM Borrowed b WHERE b.user.id = :userId AND b.dueDate < :currentDate AND b.status = com.LibraryManagementSystem.LMS.enums.BorrowStatus.BORROWED")
    List<Borrowed> findOverdueRecordsByUserId(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT b FROM Borrowed b WHERE b.user.id = :userId AND b.dueDate < :currentDate AND b.status = com.LibraryManagementSystem.LMS.enums.BorrowStatus.BORROWED")
    Page<Borrowed> findOverdueRecordsByUserId(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate, Pageable pageable);
    
    List<Borrowed> findByBookCopy(BookCopy bookCopy);
    Page<Borrowed> findByBookCopy(BookCopy bookCopy, Pageable pageable);
    
    List<Borrowed> findByBookCopyId(Long bookCopyId);
    Page<Borrowed> findByBookCopyId(Long bookCopyId, Pageable pageable);
    
    @Query("SELECT b FROM Borrowed b WHERE b.bookCopy.id = :bookCopyId AND b.status = com.LibraryManagementSystem.LMS.enums.BorrowStatus.BORROWED ORDER BY b.borrowDate DESC")
    Optional<Borrowed> findCurrentBorrowByBookCopyId(@Param("bookCopyId") Long bookCopyId);
    
    @Query("SELECT b FROM Borrowed b WHERE b.borrowDate BETWEEN :startDate AND :endDate")
    List<Borrowed> findByBorrowDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT b FROM Borrowed b WHERE b.borrowDate BETWEEN :startDate AND :endDate")
    Page<Borrowed> findByBorrowDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    
    @Query("SELECT b FROM Borrowed b WHERE b.dueDate BETWEEN :today AND :futureDate AND b.status = com.LibraryManagementSystem.LMS.enums.BorrowStatus.BORROWED")
    List<Borrowed> findRecordsDueSoon(@Param("today") LocalDate today, @Param("futureDate") LocalDate futureDate);
    
    @Query("SELECT b FROM Borrowed b WHERE b.dueDate BETWEEN :today AND :futureDate AND b.status = com.LibraryManagementSystem.LMS.enums.BorrowStatus.BORROWED")
    Page<Borrowed> findRecordsDueSoon(@Param("today") LocalDate today, @Param("futureDate") LocalDate futureDate, Pageable pageable);
    
    @Query("SELECT COUNT(b) FROM Borrowed b WHERE b.user.id = :userId AND b.status = com.LibraryManagementSystem.LMS.enums.BorrowStatus.BORROWED")
    long countActiveBorrowsByUserId(@Param("userId") Long userId);
    
    long countByStatus(BorrowStatus status);
    
    @Query("SELECT COUNT(b) FROM Borrowed b WHERE b.dueDate < :currentDate AND b.status = com.LibraryManagementSystem.LMS.enums.BorrowStatus.BORROWED")
    long countOverdueRecords(@Param("currentDate") LocalDate currentDate);
}
