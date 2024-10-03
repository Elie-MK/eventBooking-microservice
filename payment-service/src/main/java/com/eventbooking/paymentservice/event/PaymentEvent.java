package com.eventbooking.paymentservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentEvent {
    private String eventName;
    private LocalDate eventDate;
    private String eventLocation;
    private String userName;
    private String ticketType;
    private int numberOfTicket;
    private Double paymentAmount;
}
