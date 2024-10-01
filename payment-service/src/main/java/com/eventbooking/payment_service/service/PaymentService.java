package com.eventbooking.payment_service.service;

import com.eventbooking.payment_service.constants.PaymentStatus;
import com.eventbooking.payment_service.dto.PaymentDto;
import com.eventbooking.payment_service.entities.Payment;
import com.eventbooking.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

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
        boolean isBookingExists = paymentRepository.existsById(paymentDto.getBookingId());
        if (!isBookingExists) {
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
        return mapToDto(savedPayment);
    }

    private PaymentDto mapToDto(Payment savedPayment) {
        return  PaymentDto.builder()
                .id(savedPayment.getId())
                .bookingId(savedPayment.getBookingId())
                .amount(savedPayment.getAmount())
                .paymentDate(LocalDateTime.now())
                .paymentStatus(PaymentStatus.PAYMENT_APPROVED.name())
                .build();
    }
}
