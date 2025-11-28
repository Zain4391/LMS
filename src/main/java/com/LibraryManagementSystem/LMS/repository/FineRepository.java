package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.LibraryManagementSystem.LMS.entity.Fine;
import com.LibraryManagementSystem.LMS.entity.Borrowed;
import com.LibraryManagementSystem.LMS.enums.FineStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    
    Optional<Fine> findByBorrowed(Borrowed borrowed);
    
    List<Fine> findByStatus(FineStatus status);
    Page<Fine> findByStatus(FineStatus status, Pageable pageable);
    
    @Query("SELECT f FROM Fine f WHERE f.borrowed.user.id = :userId")
    List<Fine> findByUserId(@Param("userId") Long userId);

    @Query("SELECT f FROM Fine f WHERE f.borrowed.user.id = :userId")
    Page<Fine> findByUserId(@Param("userId") Long userId, Pageable pageable);
    
    boolean existsByBorrowed(Borrowed borrowed);
}
