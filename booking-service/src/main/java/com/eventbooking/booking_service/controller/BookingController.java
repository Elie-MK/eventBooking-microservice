package com.eventbooking.booking_service.controller;

import com.eventbooking.booking_service.dto.BookingDto;
import com.eventbooking.booking_service.entities.Booking;
import com.eventbooking.booking_service.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        var result = bookingService.getAllBookings();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Optional<BookingDto>> getBookingById(@PathVariable Long bookingId) {
        var result = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userName}")
    public ResponseEntity<List<BookingDto>> getBookingsByUser(@PathVariable String userName) {
        var result = bookingService.getBookingsByUserName(userName);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody BookingDto bookingDto) {
        var result = bookingService.createBooking(bookingDto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingDto> cancelBooking(@PathVariable Long id) {
        var result = bookingService.cancelBooking(id);
        return ResponseEntity.ok(result);
    }
}
