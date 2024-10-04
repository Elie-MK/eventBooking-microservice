package com.eventbooking.paymentservice.service;

import com.eventbooking.paymentservice.constants.PaymentStatus;
import com.eventbooking.paymentservice.dto.BookingDto;
import com.eventbooking.paymentservice.dto.PaymentDto;
import com.eventbooking.paymentservice.entities.Payment;
import com.eventbooking.paymentservice.event.PaymentEvent;
import com.eventbooking.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final WebClient.Builder webClientBuilder;

    /**
     * Retrieves payment details associated with a given booking ID.
     *
     * @param bookingId The ID of the booking for which payment details are requested.
     * @return An Optional containing the PaymentDto if payment exists, or empty if no payment is found.
     */
    public Optional<PaymentDto> getPaymentByBookingId(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId);
        PaymentDto paymentDto = PaymentDto.builder()
                .id(payment.getId())
                .bookingId(payment.getBookingId())
                .amount(payment.getAmount())
                .paymentDate(LocalDateTime.now())
                .paymentStatus(null)
                .build();
        return Optional.ofNullable(paymentDto);
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
                .uri("http://booking-service/api/bookings/{bookingId}", bookingId)
                .retrieve()
                .bodyToMono(BookingDto.class)
                .block();
        if (bookingResponse == null) {
            throw new RuntimeException("Booking not found. Cannot process payment");
        }

        Payment payment = Payment.builder()
                .id(paymentDto.getId())
                .bookingId(paymentDto.getBookingId())
                .amount(paymentDto.getAmount())
                .paymentDate(LocalDateTime.now())
                .paymentStatus(PaymentStatus.PENDING)
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
                .paymentStatus(PaymentStatus.PAYMENT_APPROVED.name())
                .build();
    }
}
