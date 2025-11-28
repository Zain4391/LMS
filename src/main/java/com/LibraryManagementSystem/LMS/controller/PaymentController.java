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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    

}
