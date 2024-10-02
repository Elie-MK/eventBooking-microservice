package com.eventbooking.booking_service.services;

import com.eventbooking.booking_service.dto.EventDto;
import com.eventbooking.booking_service.repository.BookingRepository;
import com.eventbooking.booking_service.dto.BookingDto;
import com.eventbooking.booking_service.entities.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final WebClient.Builder webClientBuilder;

    public List<BookingDto> getAllBookings(){
        List<Booking> bookings = bookingRepository.findAll();
        if (bookings.isEmpty()) return null;
        return bookings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<BookingDto> getBookingById(long id){
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) return Optional.empty();
        return booking.map(value -> BookingDto.builder()
                .id(value.getId())
                .eventId(value.getEventId())
                .userName(value.getUserName())
                .numberOfTickets(value.getNumberOfTickets())
                .isCancelled(value.isCancelled())
                .bookingTime(value.getBookingTime())
                .build());
    }

    public List<BookingDto> getBookingsByUserName(String userName){
        List<Booking> bookings = bookingRepository.findByUserName(userName);
        if (bookings.isEmpty()) return new ArrayList<>();
        return bookings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    public BookingDto createBooking(BookingDto bookingDto){
        //Call event service, and book if the event exist
        Long eventId = bookingDto.getEventId();

        EventDto eventResponse = webClientBuilder.build().get()
                .uri("http://event-service/api/events/{eventId}", eventId)
                .retrieve()
                .bodyToMono(EventDto.class)
                .block();

        Booking booking = new Booking();
        booking.setEventId(bookingDto.getEventId());
        booking.setUserName(bookingDto.getUserName());
        booking.setNumberOfTickets(bookingDto.getNumberOfTickets());
        booking.setBookingTime(LocalDateTime.now());
        booking.setCancelled(false);

        if (eventResponse != null){
            Booking bookingEvent = bookingRepository.save(booking);
            return mapToDto(bookingEvent);
        } else {
            throw new IllegalArgumentException("There no event with Id {}" + bookingDto.getEventId());
        }
    }

    public BookingDto cancelBooking(long id){
        Booking bookingEvent = bookingRepository.findById(id).map(booking -> {
            booking.setCancelled(true);
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking not found"));

        return mapToDto(bookingEvent);
    }

    private BookingDto mapToDto(Booking booking){
        return BookingDto.builder()
                .id(booking.getId())
                .eventId(booking.getEventId())
                .userName(booking.getUserName())
                .numberOfTickets(booking.getNumberOfTickets())
                .isCancelled(booking.isCancelled())
                .bookingTime(booking.getBookingTime())
                .build();
    }
}
