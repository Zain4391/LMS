package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.PaymentRequestDTO;
import com.LibraryManagementSystem.LMS.dto.PaymentResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Payment;
import com.LibraryManagementSystem.LMS.enums.PaymentMethod;
import com.LibraryManagementSystem.LMS.enums.PaymentStatus;
import com.LibraryManagementSystem.LMS.mapper.PaymentMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.PaymentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;
    
    public PaymentController(PaymentService paymentService, PaymentMapper paymentMapper) {
        this.paymentService = paymentService;
        this.paymentMapper = paymentMapper;
    }
    
    // Create new Payment
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentRequestDTO requestDTO) {
        Payment payment = paymentMapper.toEntity(requestDTO);
        Payment createdPayment = paymentService.create(payment);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(createdPayment);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get Payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getById(id);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(payment);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all Payments (with optional pagination)
    @GetMapping
    public ResponseEntity<?> getAllPayments(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Payment> paymentPage = paymentService.getAllPaginated(pageable);
            Page<PaymentResponseDTO> responsePage = paymentPage.map(paymentMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Payment> paymentList = paymentService.getAll();
            List<PaymentResponseDTO> responseDTOs = paymentList.stream()
                    .map(paymentMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Update Payment by ID
    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> updatePayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequestDTO requestDTO) {
        Payment payment = paymentMapper.toEntity(requestDTO);
        Payment updatedPayment = paymentService.update(id, payment);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(updatedPayment);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete Payment by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Get Payment by Transaction ID
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByTransactionId(@PathVariable String transactionId) {
        Payment payment = paymentService.findByTransactionId(transactionId);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(payment);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get Payments by Fine ID
    @GetMapping("/fine/{fineId}")
    public ResponseEntity<?> getPaymentsByFineId(
            @PathVariable Long fineId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Payment> paymentPage = paymentService.findByFineId(fineId, pageable);
            Page<PaymentResponseDTO> responsePage = paymentPage.map(paymentMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Payment> paymentList = paymentService.findByFineId(fineId);
            List<PaymentResponseDTO> responseDTOs = paymentList.stream()
                    .map(paymentMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get Payments by Status
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getPaymentsByStatus(
            @PathVariable PaymentStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Payment> paymentPage = paymentService.findByStatus(status, pageable);
            Page<PaymentResponseDTO> responsePage = paymentPage.map(paymentMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Payment> paymentList = paymentService.findByStatus(status);
            List<PaymentResponseDTO> responseDTOs = paymentList.stream()
                    .map(paymentMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get Payments by Payment Method
    @GetMapping("/method/{method}")
    public ResponseEntity<?> getPaymentsByMethod(
            @PathVariable PaymentMethod method,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Payment> paymentPage = paymentService.findByPaymentMethod(method, pageable);
            Page<PaymentResponseDTO> responsePage = paymentPage.map(paymentMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Payment> paymentList = paymentService.findByPaymentMethod(method);
            List<PaymentResponseDTO> responseDTOs = paymentList.stream()
                    .map(paymentMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get Payments by User ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPaymentsByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Payment> paymentPage = paymentService.findByUserId(userId, pageable);
            Page<PaymentResponseDTO> responsePage = paymentPage.map(paymentMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Payment> paymentList = paymentService.findByUserId(userId);
            List<PaymentResponseDTO> responseDTOs = paymentList.stream()
                    .map(paymentMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get Payments by Payment Date Range
    @GetMapping("/search/payment-date")
    public ResponseEntity<?> getPaymentsByPaymentDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Payment> paymentPage = paymentService.findByPaymentDateBetween(startDate, endDate, pageable);
            Page<PaymentResponseDTO> responsePage = paymentPage.map(paymentMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Payment> paymentList = paymentService.findByPaymentDateBetween(startDate, endDate);
            List<PaymentResponseDTO> responseDTOs = paymentList.stream()
                    .map(paymentMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get Completed Payments by Method
    @GetMapping("/completed/method/{method}")
    public ResponseEntity<?> getCompletedPaymentsByMethod(
            @PathVariable PaymentMethod method,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Payment> paymentPage = paymentService.findCompletedPaymentsByMethod(method, pageable);
            Page<PaymentResponseDTO> responsePage = paymentPage.map(paymentMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Payment> paymentList = paymentService.findCompletedPaymentsByMethod(method);
            List<PaymentResponseDTO> responseDTOs = paymentList.stream()
                    .map(paymentMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Process a Payment
    @PostMapping("/{id}/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(@PathVariable Long id) {
        Payment processedPayment = paymentService.processPayment(id);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(processedPayment);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Complete a Payment
    @PostMapping("/{id}/complete")
    public ResponseEntity<PaymentResponseDTO> completePayment(
            @PathVariable Long id,
            @RequestParam(required = false) String transactionId) {
        Payment completedPayment = paymentService.completePayment(id, transactionId);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(completedPayment);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Fail a Payment
    @PostMapping("/{id}/fail")
    public ResponseEntity<PaymentResponseDTO> failPayment(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        Payment failedPayment = paymentService.failPayment(id, reason);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(failedPayment);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Refund a Payment
    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentResponseDTO> refundPayment(@PathVariable Long id) {
        Payment refundPayment = paymentService.refundPayment(id);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(refundPayment);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get Total Paid Amount by Fine ID
    @GetMapping("/fine/{fineId}/total-paid")
    public ResponseEntity<Map<String, BigDecimal>> getTotalPaidAmountByFineId(@PathVariable Long fineId) {
        BigDecimal total = paymentService.calculateTotalPaidAmountByFineId(fineId);
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("totalPaid", total);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Get Total Paid Amount by User ID
    @GetMapping("/user/{userId}/total-paid")
    public ResponseEntity<Map<String, BigDecimal>> getTotalPaidAmountByUserId(@PathVariable Long userId) {
        BigDecimal total = paymentService.calculateTotalPaidAmountByUserId(userId);
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("totalPaid", total);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Get Total Revenue by Date Range
    @GetMapping("/revenue")
    public ResponseEntity<Map<String, BigDecimal>> getTotalRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BigDecimal total = paymentService.calculateTotalRevenueBetween(startDate, endDate);
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("totalRevenue", total);
        response.put("startDate", new BigDecimal(startDate.toString().length())); // Just for map consistency
        response.put("endDate", new BigDecimal(endDate.toString().length())); // Just for map consistency
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Check if Transaction ID Exists
    @GetMapping("/exists/transaction/{transactionId}")
    public ResponseEntity<Map<String, Boolean>> checkTransactionIdExists(@PathVariable String transactionId) {
        boolean exists = paymentService.existsByTransactionId(transactionId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
