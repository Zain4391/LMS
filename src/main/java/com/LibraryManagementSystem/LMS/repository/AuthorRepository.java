package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.LibraryManagementSystem.LMS.entity.Author;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    
    Optional<Author> findByName(String name);
    
    List<Author> findByNameContainingIgnoreCase(String name);
    
    List<Author> findByNationality(String nationality);
    
    List<Author> findByNationalityContainingIgnoreCase(String nationality);
    
    boolean existsByName(String name);
}
