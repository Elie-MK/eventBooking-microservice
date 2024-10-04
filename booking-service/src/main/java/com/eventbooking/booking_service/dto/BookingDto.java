package com.eventbooking.booking_service.dto;

import com.eventbooking.booking_service.constants.TicketType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
    private Integer numberOfTickets;

    private BigDecimal totalAmount;

    private TicketType ticketType;

    /**
     * State if the user cancelled or not
     */
    private boolean isCancelled;
    /**
     * Time when user booked
     */
    private LocalDateTime bookingTime;

}
