package com.LibraryManagementSystem.LMS.service.interfaces;

import com.LibraryManagementSystem.LMS.entity.Publisher;
import java.util.List;

public interface PublisherService {
    
    Publisher create(Publisher publisher);
    
    Publisher getById(Long id);
    
    List<Publisher> getAll();
    
    Publisher update(Long id, Publisher publisher);
    
    void delete(Long id);
}

