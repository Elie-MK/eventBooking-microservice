package com.eventbooking.paymentservice.controller;

import com.eventbooking.paymentservice.dto.PaymentDto;
import com.eventbooking.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/booking/{bookingId}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<Optional<PaymentDto>> getPaymentBooking(@PathVariable() Long bookingId) {
        var result = paymentService.getPaymentByBookingId(bookingId);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PaymentDto> processPayment(@RequestBody PaymentDto paymentDto) {
        var result = paymentService.processPayment(paymentDto);
        return  ResponseEntity.ok(result);
    }
}
