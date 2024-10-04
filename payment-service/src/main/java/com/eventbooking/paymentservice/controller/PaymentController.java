package com.eventbooking.paymentservice.controller;

import com.eventbooking.paymentservice.dto.PaymentDto;
import com.eventbooking.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    /**
     * Retrieve a list of all payments.
     * <p>
     * This method handles the HTTP GET request to fetch all payments.
     * It calls the PaymentService to retrieve a list of PaymentDto objects,
     * which represent the payment records. </p>
     *
     * @return a ResponseEntity containing a list of PaymentDto and an HTTP 200 OK status
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PaymentDto>> getAllPayments(){
        var result = paymentService.getAllPayments();
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves payment information for a specific booking.
     *
     * @param bookingId The ID of the booking for which payment details are requested.
     * @return A ResponseEntity containing an Optional of PaymentDto,
     *         wrapped in an OK response status.
     */
    @GetMapping("/booking")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PaymentDto>> getPaymentBooking(@RequestParam() Long bookingId) {
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
