package com.LibraryManagementSystem.LMS.service;

import com.LibraryManagementSystem.LMS.entity.Publisher;
import com.LibraryManagementSystem.LMS.exception.ResourceNotFoundException;
import com.LibraryManagementSystem.LMS.repository.PublisherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {
    
    private final PublisherRepository publisherRepository;
    
    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }
    
    @Override
    public Publisher create(Publisher publisher) {
        return publisherRepository.save(publisher);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Publisher getById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "id", id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Publisher> getAll() {
        return publisherRepository.findAll();
    }
    
    @Override
    public Publisher update(Long id, Publisher publisher) {
        Publisher existingPublisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "id", id));
        
        existingPublisher.setName(publisher.getName());
        existingPublisher.setAddress(publisher.getAddress());
        existingPublisher.setEmail(publisher.getEmail());
        existingPublisher.setCountry(publisher.getCountry());
        
        return publisherRepository.save(existingPublisher);
    }
    
    @Override
    public void delete(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "id", id));
        
        publisherRepository.delete(publisher);
    }
}

