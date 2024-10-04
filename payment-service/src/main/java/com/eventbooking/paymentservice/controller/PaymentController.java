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

    /**
     * Retrieves payment information for a specific booking.
     *
     * @param bookingId The ID of the booking for which payment details are requested.
     * @return A ResponseEntity containing an Optional of PaymentDto,
     *         wrapped in an OK response status.
     */
    @GetMapping("/booking/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Optional<PaymentDto>> getPaymentBooking(@PathVariable() Long bookingId) {
        var result = paymentService.getPaymentByBookingId(bookingId);
        return ResponseEntity.ok(result);
    }

    /**
     * Processes a payment based on the provided PaymentDto.
     *
     * @param paymentDto The PaymentDto containing payment details to be processed.
     * @return A ResponseEntity containing the processed PaymentDto,
     *         wrapped in a CREATED response status.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PaymentDto> processPayment(@RequestBody PaymentDto paymentDto) {
        var result = paymentService.processPayment(paymentDto);
        return  ResponseEntity.ok(result);
    }
}
