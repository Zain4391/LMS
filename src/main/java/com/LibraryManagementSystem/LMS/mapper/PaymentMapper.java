package com.LibraryManagementSystem.LMS.mapper;

import com.LibraryManagementSystem.LMS.dto.PaymentRequestDTO;
import com.LibraryManagementSystem.LMS.dto.PaymentResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Fine;
import com.LibraryManagementSystem.LMS.entity.Payment;
import com.LibraryManagementSystem.LMS.enums.PaymentStatus;
import com.LibraryManagementSystem.LMS.repository.FineRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PaymentMapper {
    
    private final FineMapper fineMapper;
    private final FineRepository fineRepository;

    public PaymentMapper(FineMapper fineMapper, FineRepository fineRepository) {
        this.fineMapper = fineMapper;
        this.fineRepository = fineRepository;
    }

    public PaymentResponseDTO toResponseDTO(Payment payment) {
        if (payment == null) {
            return null;
        }
        
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setTransactionId(payment.getTransactionId());
        dto.setStatus(payment.getStatus());
        
        // Map Fine entity to FineResponseDTO
        if (payment.getFine() != null) {
            dto.setFine(fineMapper.toResponseDTO(payment.getFine()));
        }
        
        return dto;
    }
    
    public Payment toEntity(PaymentRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        
        Payment payment = new Payment();
        payment.setAmount(requestDTO.getAmount());
        payment.setPaymentMethod(requestDTO.getPaymentMethod());
        payment.setTransactionId(requestDTO.getTransactionId());
        
        // Set payment date to current date
        payment.setPaymentDate(LocalDate.now());
        
        // Set status to PENDING by default
        payment.setStatus(PaymentStatus.PENDING);
        
        // Fetch and set Fine entity
        if (requestDTO.getFineId() != null) {
            Fine fine = fineRepository.findById(requestDTO.getFineId())
                    .orElse(null);
            payment.setFine(fine);
        }
        
        return payment;
    }
    
    public void updateEntityFromRequest(Payment payment, PaymentRequestDTO requestDTO) {
        if (payment == null || requestDTO == null) {
            return;
        }
        
        if (requestDTO.getAmount() != null) {
            payment.setAmount(requestDTO.getAmount());
        }
        
        if (requestDTO.getPaymentMethod() != null) {
            payment.setPaymentMethod(requestDTO.getPaymentMethod());
        }
        
        if (requestDTO.getTransactionId() != null) {
            payment.setTransactionId(requestDTO.getTransactionId());
        }
        
        // Update Fine if fineId is provided
        if (requestDTO.getFineId() != null) {
            Fine fine = fineRepository.findById(requestDTO.getFineId())
                    .orElse(null);
            payment.setFine(fine);
        }
    }
}

