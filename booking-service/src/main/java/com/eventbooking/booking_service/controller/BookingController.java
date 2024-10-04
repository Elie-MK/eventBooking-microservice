package com.eventbooking.booking_service.controller;

import com.eventbooking.booking_service.dto.BookingDto;
import com.eventbooking.booking_service.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    /**
     * Retrieves a list of all bookings.
     *
     * @return A ResponseEntity containing a list of BookingDto objects and an HTTP status of FOUND (302).
     */
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        var result = bookingService.getAllBookings();
        return ResponseEntity.ok(result);
    }

    /**
     * Creates a new booking.
     *
     * @param bookingDto The BookingDto object containing the booking details.
     * @return A ResponseEntity containing the created BookingDto object and an HTTP status of CREATED (201).
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookingDto> createBooking(@RequestBody BookingDto bookingDto) {
        var result = bookingService.createBooking(bookingDto);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param bookingId The ID of the booking to retrieve.
     * @return A ResponseEntity containing the BookingDto object if found, wrapped in an Optional, and an HTTP status of FOUND (302).
     */
    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<Optional<BookingDto>> getBookingById(@PathVariable Long bookingId) {
        var result = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(result);
    }

    /**
     * Cancels a booking by its ID.
     *
     * @param id The ID of the booking to cancel.
     * @return A ResponseEntity containing a cancellation confirmation message and an HTTP status of OK (200).
     */
    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        var result = bookingService.cancelBooking(id);
        return ResponseEntity.ok(result);
    }
}
