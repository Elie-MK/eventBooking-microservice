package com.eventbooking.paymentservice.service;

import com.eventbooking.paymentservice.constants.PaymentStatus;
import com.eventbooking.paymentservice.dto.BookingDto;
import com.eventbooking.paymentservice.dto.PaymentDto;
import com.eventbooking.paymentservice.entities.Payment;
import com.eventbooking.paymentservice.exceptionhandler.BookingIsCancelledException;
import com.eventbooking.paymentservice.exceptionhandler.NotFoundException;
import com.eventbooking.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final WebClient.Builder webClientBuilder;

    /**
     * Retrieve a list of all payments.
     * <p>
     * This method fetches all the payment records from the database,
     * maps each payment entity to a PaymentDto object, and returns a list of them.
     *
     * @return a List of PaymentDto representing all payments in the system
     */
    public List<PaymentDto> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves payment details associated with a given booking ID.
     *
     * @param bookingId The ID of the booking for which payment details are requested.
     * @return An Optional containing the PaymentDto if payment exists, or empty if no payment is found.
     */
    public List<PaymentDto> getPaymentByBookingId(Long bookingId) {
        List<Payment> payment = paymentRepository.findByBookingId(bookingId);

        if (payment.isEmpty()) {
            throw new NotFoundException("Payment with id " + bookingId + " not found.");
        }

        return payment.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Processes the payment for a given booking. It retrieves booking details from the booking service
     * and creates a Payment entity, marking it as pending.
     *
     * @param paymentDto The payment data transfer object containing payment details.
     * @return The PaymentDto representing the processed payment.
     * @throws RuntimeException if the associated booking is not found.
     */
    public PaymentDto processPayment(PaymentDto paymentDto) {
        Long bookingId = paymentDto.getBookingId();
        BookingDto bookingResponse = webClientBuilder.build().get()
                .uri("http://booking-service/api/booking/{bookingId}", bookingId)
                .retrieve()
                .bodyToMono(BookingDto.class)
                .block();
        if (bookingResponse == null) {
            throw new NotFoundException("Booking not found with id: " + bookingId + " . Cannot process payment");
        }

        if (bookingResponse.isCancelled()) {
            throw new BookingIsCancelledException("Booking with id " + bookingId + " is cancelled. Cannot process payment.");
        }

        Payment payment = Payment.builder()
                .id(paymentDto.getId())
                .bookingId(paymentDto.getBookingId())
                .amount(bookingResponse.getTotalAmount().doubleValue())
                .paymentDate(LocalDateTime.now())
                .paymentStatus(PaymentStatus.PAYMENT_PENDING)
                .build();
        Payment savedPayment = paymentRepository.save(payment);
        return mapToDto(savedPayment);
    }

    /**
     * Maps a Payment entity to a PaymentDto.
     *
     * @param savedPayment The Payment entity to be converted.
     * @return The corresponding PaymentDto containing payment details.
     */
    private PaymentDto mapToDto(Payment savedPayment) {
        return PaymentDto.builder()
                .id(savedPayment.getId())
                .bookingId(savedPayment.getBookingId())
                .amount(savedPayment.getAmount())
                .paymentDate(LocalDateTime.now())
                .paymentStatus(savedPayment.getPaymentStatus().name())
                .build();
    }

}
