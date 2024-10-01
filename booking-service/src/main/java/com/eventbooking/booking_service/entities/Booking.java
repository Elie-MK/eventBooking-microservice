package com.eventbooking.booking_service.entities;

import com.eventbooking.booking_service.constants.TicketType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "number_of_tickets", nullable = false)
    private int numberOfTickets;

    @Column(name = "booking-time", nullable = false)
    private LocalDateTime bookingTime;

    @Column(name = "ticket_type")
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @Column(name = "is_cancelled", nullable = false)
    private boolean isCancelled;

    @PrePersist()
    protected void onCreate() {
        bookingTime = LocalDateTime.now();
    }
}
