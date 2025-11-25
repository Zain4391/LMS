package com.LibraryManagementSystem.LMS.service.interfaces;

import com.LibraryManagementSystem.LMS.entity.Genre;
import java.util.List;

public interface GenreService {
    
    Genre create(Genre genre);
    
    Genre getById(Long id);
    
    List<Genre> getAll();
    
    Genre update(Long id, Genre genre);
    
    void delete(Long id);
}

