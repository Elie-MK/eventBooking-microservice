package com.eventbooking;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
