package com.eventbooking.payment_service.service;

import com.eventbooking.payment_service.constants.PaymentStatus;
import com.eventbooking.payment_service.dto.BookingDto;
import com.eventbooking.payment_service.dto.PaymentDto;
import com.eventbooking.payment_service.entities.Payment;
import com.eventbooking.payment_service.event.PaymentEvent;
import com.eventbooking.payment_service.repository.PaymentRepository;
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
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

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
                .paymentStatus(PaymentStatus.PAYMENT_APPROVED)
                .build();
        Payment savedPayment = paymentRepository.save(payment);
        PaymentEvent paymentEvent = PaymentEvent.builder()
                .paymentAmount(paymentDto.getAmount())
                .numberOfTicket(2)
                .eventLocation("Tunis")
                .eventName("UNGA")
                .userName("John")
                .eventDate(LocalDate.ofEpochDay(2024 - 10 - 2023)).build();

        kafkaTemplate.send("notification", paymentEvent);
        return mapToDto(savedPayment);
    }

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
