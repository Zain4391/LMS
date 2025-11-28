package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.PaymentRequestDTO;
import com.LibraryManagementSystem.LMS.dto.PaymentResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Payment;
import com.LibraryManagementSystem.LMS.enums.PaymentMethod;
import com.LibraryManagementSystem.LMS.enums.PaymentStatus;
import com.LibraryManagementSystem.LMS.mapper.PaymentMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Payments", description = "Payment management APIs - Process and track fine payments with various payment methods and statuses")
public class PaymentController {
    
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;
    
    public PaymentController(PaymentService paymentService, PaymentMapper paymentMapper) {
        this.paymentService = paymentService;
        this.paymentMapper = paymentMapper;
    }
    
    // Create new Payment
    @Operation(
            summary = "Create a new payment",
            description = "Creates a new payment record for a fine with specified amount and payment method"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment created successfully",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "404", description = "Fine not found")
    })
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @Parameter(description = "Payment details to create", required = true)
            @Valid @RequestBody PaymentRequestDTO requestDTO) {
        Payment payment = paymentMapper.toEntity(requestDTO);
        Payment createdPayment = paymentService.create(payment);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(createdPayment);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get Payment by ID
    @Operation(
            summary = "Get payment by ID",
            description = "Retrieves a specific payment's details by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment found successfully",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Payment not found with the given ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(
            @Parameter(description = "Payment ID", required = true, example = "1")
            @PathVariable Long id) {
        Payment payment = paymentService.getById(id);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(payment);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all Payments (with optional pagination)
    @Operation(
            summary = "Get all payments",
            description = "Retrieves all payment records with optional pagination and sorting"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<?> getAllPayments(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "paymentDate")
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
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
    @Operation(
            summary = "Update payment by ID",
            description = "Updates an existing payment record's details by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment updated successfully",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "404", description = "Payment not found with the given ID")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> updatePayment(
            @Parameter(description = "Payment ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated payment details", required = true)
            @Valid @RequestBody PaymentRequestDTO requestDTO) {
        Payment payment = paymentMapper.toEntity(requestDTO);
        Payment updatedPayment = paymentService.update(id, payment);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(updatedPayment);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete Payment by ID
    @Operation(
            summary = "Delete payment by ID",
            description = "Permanently deletes a payment record by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Payment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found with the given ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(
            @Parameter(description = "Payment ID", required = true, example = "1")
            @PathVariable Long id) {
        paymentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Get Payment by Transaction ID
    @Operation(
            summary = "Get payment by transaction ID",
            description = "Retrieves a payment record by its unique transaction ID from the payment gateway"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment found successfully",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Payment not found with the given transaction ID")
    })
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByTransactionId(
            @Parameter(description = "Transaction ID", required = true, example = "TXN-123456")
            @PathVariable String transactionId) {
        Payment payment = paymentService.findByTransactionId(transactionId);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(payment);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get Payments by Fine ID
    @Operation(
            summary = "Get payments by fine ID",
            description = "Retrieves all payment transactions for a specific fine with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully")
    })
    @GetMapping("/fine/{fineId}")
    public ResponseEntity<?> getPaymentsByFineId(
            @Parameter(description = "Fine ID", required = true, example = "1")
            @PathVariable Long fineId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "paymentDate")
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
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
    @Operation(
            summary = "Get payments by status",
            description = "Retrieves all payments with a specific status (PENDING, PROCESSING, COMPLETED, FAILED, REFUNDED) with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getPaymentsByStatus(
            @Parameter(description = "Payment status", required = true, example = "COMPLETED")
            @PathVariable PaymentStatus status,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "paymentDate")
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
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
    @Operation(
            summary = "Get payments by payment method",
            description = "Retrieves all payments made with a specific payment method (CASH, CARD, ONLINE, CHECK) with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully")
    })
    @GetMapping("/method/{method}")
    public ResponseEntity<?> getPaymentsByMethod(
            @Parameter(description = "Payment method", required = true, example = "CARD")
            @PathVariable PaymentMethod method,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "paymentDate")
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
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
    @Operation(
            summary = "Get payments by user",
            description = "Retrieves all payment transactions made by a specific user with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPaymentsByUserId(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "paymentDate")
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
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
    @Operation(
            summary = "Get payments by date range",
            description = "Retrieves all payments made within a specific date range with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully")
    })
    @GetMapping("/search/payment-date")
    public ResponseEntity<?> getPaymentsByPaymentDateRange(
            @Parameter(description = "Start date", required = true, example = "2024-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", required = true, example = "2024-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "paymentDate")
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
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
    @Operation(
            summary = "Get completed payments by payment method",
            description = "Retrieves all successfully completed payments for a specific payment method with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Completed payments retrieved successfully")
    })
    @GetMapping("/completed/method/{method}")
    public ResponseEntity<?> getCompletedPaymentsByMethod(
            @Parameter(description = "Payment method", required = true, example = "CARD")
            @PathVariable PaymentMethod method,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "paymentDate")
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
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
    @Operation(
            summary = "Process a payment",
            description = "Initiates payment processing and updates status to PROCESSING"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment processing initiated",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Payment cannot be processed in current state"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PostMapping("/{id}/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(
            @Parameter(description = "Payment ID", required = true, example = "1")
            @PathVariable Long id) {
        Payment processedPayment = paymentService.processPayment(id);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(processedPayment);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Complete a Payment
    @Operation(
            summary = "Complete a payment",
            description = "Marks a payment as completed with optional transaction ID. Updates fine status accordingly."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment completed successfully",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Payment cannot be completed in current state"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PostMapping("/{id}/complete")
    public ResponseEntity<PaymentResponseDTO> completePayment(
            @Parameter(description = "Payment ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Transaction ID from payment gateway", example = "TXN-123456")
            @RequestParam(required = false) String transactionId) {
        Payment completedPayment = paymentService.completePayment(id, transactionId);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(completedPayment);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Fail a Payment
    @Operation(
            summary = "Fail a payment",
            description = "Marks a payment as failed with an optional reason. Updates status to FAILED."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment marked as failed",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PostMapping("/{id}/fail")
    public ResponseEntity<PaymentResponseDTO> failPayment(
            @Parameter(description = "Payment ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Reason for failure", example = "Insufficient funds")
            @RequestParam(required = false) String reason) {
        Payment failedPayment = paymentService.failPayment(id, reason);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(failedPayment);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Refund a Payment
    @Operation(
            summary = "Refund a payment",
            description = "Creates a refund record for a completed payment and updates payment status"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Refund created successfully",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Payment cannot be refunded - not in completed state"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentResponseDTO> refundPayment(
            @Parameter(description = "Payment ID", required = true, example = "1")
            @PathVariable Long id) {
        Payment refundPayment = paymentService.refundPayment(id);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(refundPayment);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get Total Paid Amount by Fine ID
    @Operation(
            summary = "Get total paid amount for a fine",
            description = "Calculates the total amount paid towards a specific fine across all payment transactions"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total amount calculated successfully")
    })
    @GetMapping("/fine/{fineId}/total-paid")
    public ResponseEntity<Map<String, BigDecimal>> getTotalPaidAmountByFineId(
            @Parameter(description = "Fine ID", required = true, example = "1")
            @PathVariable Long fineId) {
        BigDecimal total = paymentService.calculateTotalPaidAmountByFineId(fineId);
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("totalPaid", total);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Get Total Paid Amount by User ID
    @Operation(
            summary = "Get total paid amount by user",
            description = "Calculates the total amount paid by a specific user across all their payment transactions"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total amount calculated successfully")
    })
    @GetMapping("/user/{userId}/total-paid")
    public ResponseEntity<Map<String, BigDecimal>> getTotalPaidAmountByUserId(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId) {
        BigDecimal total = paymentService.calculateTotalPaidAmountByUserId(userId);
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("totalPaid", total);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Get Total Revenue by Date Range
    @Operation(
            summary = "Get total revenue by date range",
            description = "Calculates the total revenue from all completed payments within a specific date range"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Revenue calculated successfully")
    })
    @GetMapping("/revenue")
    public ResponseEntity<Map<String, BigDecimal>> getTotalRevenue(
            @Parameter(description = "Start date", required = true, example = "2024-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", required = true, example = "2024-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BigDecimal total = paymentService.calculateTotalRevenueBetween(startDate, endDate);
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("totalRevenue", total);
        response.put("startDate", new BigDecimal(startDate.toString().length())); // Just for map consistency
        response.put("endDate", new BigDecimal(endDate.toString().length())); // Just for map consistency
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Check if Transaction ID Exists
    @Operation(
            summary = "Check if transaction ID exists",
            description = "Validates whether a transaction ID already exists in the system to prevent duplicates"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    @GetMapping("/exists/transaction/{transactionId}")
    public ResponseEntity<Map<String, Boolean>> checkTransactionIdExists(
            @Parameter(description = "Transaction ID", required = true, example = "TXN-123456")
            @PathVariable String transactionId) {
        boolean exists = paymentService.existsByTransactionId(transactionId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
