package com.eventbooking.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingDto {
    /**
     * Id of the Booking
     */
    private Long id;
    /**
     * Id of the event
     */
    private Long eventId;
    /**
     * Username of the user who booked
     */
    private String userName;
    /**
     * Number of tickets booked
     */
    private int numberOfTickets;
    /**
     * Time when user booked
     */
    private LocalDateTime bookingTime;
    /**
     * State if the user cancelled or not
     */
    private boolean isCancelled;

}
