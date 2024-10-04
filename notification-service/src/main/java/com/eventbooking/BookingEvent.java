package com.eventbooking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * BookingEvent represents the details of a booking event, including event and user information.
 * <p>
 * This class is used to transfer booking details such as event name, date, location, and
 * user details like the type and number of tickets booked, as well as the total payment amount. </p>
 *
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingEvent {
    private String eventName;
    private LocalDate eventDate;
    private String eventLocation;
    private String userName;
    private String ticketType;
    private Integer numberOfTicket;
    private BigDecimal paymentAmount;
}