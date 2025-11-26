package com.LibraryManagementSystem.LMS.mapper;

import com.LibraryManagementSystem.LMS.dto.BookCopyRequestDTO;
import com.LibraryManagementSystem.LMS.dto.BookCopyResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Book;
import com.LibraryManagementSystem.LMS.entity.BookCopy;
import com.LibraryManagementSystem.LMS.repository.BookRepository;
import org.springframework.stereotype.Component;

@Component
public class BookCopyMapper {
    
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    
    public BookCopyMapper(BookMapper bookMapper, BookRepository bookRepository) {
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
    }
    
    public BookCopyResponseDTO toResponseDTO(BookCopy bookCopy) {
        if (bookCopy == null) {
            return null;
        }
        
        BookCopyResponseDTO dto = new BookCopyResponseDTO();
        dto.setId(bookCopy.getId());
        dto.setBarcode(bookCopy.getBarcode());
        dto.setCondition(bookCopy.getCondition());
        dto.setStatus(bookCopy.getStatus());
        dto.setAcquisitionDate(bookCopy.getAcquisitionDate());
        dto.setLocation(bookCopy.getLocation());
        
        // Map the related Book entity to BookResponseDTO
        if (bookCopy.getBook() != null) {
            dto.setBook(bookMapper.toResponseDTO(bookCopy.getBook()));
        }
        
        return dto;
    }
    
    public BookCopy toEntity(BookCopyRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        
        BookCopy bookCopy = new BookCopy();
        bookCopy.setBarcode(requestDTO.getBarcode());
        bookCopy.setCondition(requestDTO.getCondition());
        bookCopy.setAcquisitionDate(requestDTO.getAcquisitionDate());
        bookCopy.setLocation(requestDTO.getLocation());
        
        // Fetch and set the Book entity using BookRepository
        if (requestDTO.getBookId() != null) {
            Book book = bookRepository.findById(requestDTO.getBookId())
                    .orElse(null);
            bookCopy.setBook(book);
        }
        
        return bookCopy;
    }
    
    public void updateEntityFromRequest(BookCopy bookCopy, BookCopyRequestDTO requestDTO) {
        if (bookCopy == null || requestDTO == null) {
            return;
        }
        
        if (requestDTO.getBarcode() != null) {
            bookCopy.setBarcode(requestDTO.getBarcode());
        }
        if (requestDTO.getCondition() != null) {
            bookCopy.setCondition(requestDTO.getCondition());
        }
        if (requestDTO.getAcquisitionDate() != null) {
            bookCopy.setAcquisitionDate(requestDTO.getAcquisitionDate());
        }
        if (requestDTO.getLocation() != null) {
            bookCopy.setLocation(requestDTO.getLocation());
        }
        
        // Update the Book entity if bookId is provided
        if (requestDTO.getBookId() != null) {
            Book book = bookRepository.findById(requestDTO.getBookId())
                    .orElse(null);
            bookCopy.setBook(book);
        }
    }
}
