package com.LibraryManagementSystem.LMS.service;

import com.LibraryManagementSystem.LMS.entity.Author;
import com.LibraryManagementSystem.LMS.exception.ResourceNotFoundException;
import com.LibraryManagementSystem.LMS.repository.AuthorRepository;
import com.LibraryManagementSystem.LMS.service.interfaces.AuthorService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {
    
    private final AuthorRepository authorRepository;
    
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    
    @Override
    public Author create(Author author) {
        return authorRepository.save(author);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Author getById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Author> getAll() {
        return authorRepository.findAll();
    }
    
    @Override
    public Author update(Long id, Author author) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        
        existingAuthor.setName(author.getName());
        existingAuthor.setBiography(author.getBiography());
        existingAuthor.setBirthDate(author.getBirthDate());
        existingAuthor.setNationality(author.getNationality());
        
        return authorRepository.save(existingAuthor);
    }
    
    @Override
    public void delete(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        
        authorRepository.delete(author);
    }
}

