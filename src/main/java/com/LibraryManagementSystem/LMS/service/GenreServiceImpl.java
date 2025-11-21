package com.LibraryManagementSystem.LMS.service;

import com.LibraryManagementSystem.LMS.entity.Genre;
import com.LibraryManagementSystem.LMS.exception.ResourceNotFoundException;
import com.LibraryManagementSystem.LMS.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GenreServiceImpl implements GenreService {
    
    private final GenreRepository genreRepository;
    
    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }
    
    @Override
    public Genre create(Genre genre) {
        return genreRepository.save(genre);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Genre getById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", "id", id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }
    
    @Override
    public Genre update(Long id, Genre genre) {
        Genre existingGenre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", "id", id));
        
        existingGenre.setName(genre.getName());
        existingGenre.setDescription(genre.getDescription());
        
        return genreRepository.save(existingGenre);
    }
    
    @Override
    public void delete(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", "id", id));
        
        genreRepository.delete(genre);
    }
}

