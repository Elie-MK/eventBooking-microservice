package com.eventbooking.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private Long id;

    private Long bookingId;

    private double amount;

    private LocalDateTime paymentDate;

    private String paymentStatus;
}
