package com.eventbooking.booking_service.services;

import com.eventbooking.booking_service.constants.TicketType;
import com.eventbooking.booking_service.dto.EventDto;
import com.eventbooking.booking_service.exceptionshandller.BookingAlreadyCancelledException;
import com.eventbooking.booking_service.event.BookingEvent;
import com.eventbooking.booking_service.exceptionshandller.BookingCancelledException;
import com.eventbooking.booking_service.exceptionshandller.NotFoundException;
import com.eventbooking.booking_service.repository.BookingRepository;
import com.eventbooking.booking_service.dto.BookingDto;
import com.eventbooking.booking_service.entities.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;


    /**
     * Retrieves all bookings from the repository.
     *
     * @return A list of BookingDto objects or null if no bookings are found.
     */
    public List<BookingDto> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        if (bookings.isEmpty()) return null;
        return bookings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param bookingId The ID of the booking to retrieve.
     * @return An Optional containing the BookingDto if found, or an empty Optional if not.
     */
    public Optional<BookingDto> getBookingById(long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
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


    /**
     * Creates a new booking.
     * Calls the event service to verify the existence of the event before booking.
     *
     * @param bookingDto The booking details.
     * @return The created BookingDto.
     * @throws IllegalArgumentException If the event does not exist.
     */
    public BookingDto createBooking(BookingDto bookingDto) {
        //Call event service, and book if the event exist
        Long eventId = bookingDto.getEventId();

        EventDto eventResponse = webClientBuilder.build().get()
                .uri("http://event-service/api/events/{eventId}", eventId)
                .retrieve()
                .bodyToMono(EventDto.class)
                .block();

        Booking booking = Booking.builder()
                .eventId(bookingDto.getEventId())
                .userName(bookingDto.getUserName())
                .numberOfTickets(bookingDto.getNumberOfTickets())
                .ticketType(bookingDto.getTicketType())
                .bookingTime(LocalDateTime.now())
                .totalAmount(getPriceForTicketType(
                        bookingDto.getTicketType(),
                        bookingDto.getNumberOfTickets()
                )).isCancelled(false)
                .build();
        if (eventResponse == null) {
            throw new NotFoundException("There no event with Id {}" + bookingDto.getEventId());
        }

        Booking bookingEvent = bookingRepository.save(booking);
        BookingEvent newBookingEvent = getBookingEvent(bookingDto, eventResponse);

        sendNotificationBookingConfirmation(newBookingEvent);
        return mapToDto(bookingEvent);
    }

    private BookingEvent getBookingEvent(BookingDto bookingDto, EventDto eventResponse) {
        return BookingEvent.builder()
                .eventName(eventResponse.getName())
                .eventDate(eventResponse.getDate())
                .eventLocation(eventResponse.getLocation())
                .userName(bookingDto.getUserName())
                .ticketType(bookingDto.getTicketType().name())
                .numberOfTicket(bookingDto.getNumberOfTickets())
                .paymentAmount(getPriceForTicketType(
                        bookingDto.getTicketType(),
                        bookingDto.getNumberOfTickets()
                )).build();
    }

    /**
     * Cancels a booking by its ID.
     *
     * @param id The ID of the booking to cancel.
     * @return A cancellation confirmation message.
     * @throws IllegalArgumentException If the booking or event does not exist.
     * @throws BookingAlreadyCancelledException If the booking is already cancelled.
     */
    public String cancelBooking(Long id) {
        Booking bookingEvent = bookingRepository.findById(id).map(booking -> {
            if (booking.isCancelled()) {
                throw new BookingAlreadyCancelledException("The event booking with id: " + id + " was already cancelled");
            }
            booking.setCancelled(true);
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new NotFoundException("There no booking with Id {}" + id));

        EventDto eventResponse = webClientBuilder.build().get()
                .uri("http://event-service/api/events/{eventId}", bookingEvent.getEventId())
                .retrieve()
                .bodyToMono(EventDto.class)
                .block();

        if (eventResponse == null) {
            throw new NotFoundException("There no event with id: " + bookingEvent.getEventId());
        }

        BookingEvent newBookingEvent = BookingEvent.builder()
                .eventName(eventResponse.getName())
                .eventDate(eventResponse.getDate())
                .eventLocation(eventResponse.getLocation())
                .userName(bookingEvent.getUserName())
                .ticketType(bookingEvent.getTicketType().toString())
                .numberOfTicket(bookingEvent.getNumberOfTickets())
                .paymentAmount(getPriceForTicketType(
                        bookingEvent.getTicketType(),
                        bookingEvent.getNumberOfTickets()
                )).build();

        sendNotificationBookingConfirmation(newBookingEvent);
        throw new BookingCancelledException("The booking with id : " + id + " was cancelled");
    }

    /**
     * Maps a Booking entity to its corresponding BookingDto.
     *
     * @param booking The booking entity to map.
     * @return The corresponding BookingDto.
     */
    private BookingDto mapToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .eventId(booking.getEventId())
                .userName(booking.getUserName())
                .numberOfTickets(booking.getNumberOfTickets())
                .isCancelled(booking.isCancelled())
                .bookingTime(booking.getBookingTime())
                .ticketType(booking.getTicketType())
                .totalAmount(booking.getTotalAmount())
                .build();
    }

    /**
     * Gets the price for a given ticket type and number of tickets.
     *
     * @param ticketType The type of ticket.
     * @param numberOfTicket The number of tickets.
     * @return The total price as a BigDecimal.
     */
    private BigDecimal getPriceForTicketType(TicketType ticketType, Integer numberOfTicket) {
        return switch (ticketType) {
            case VIP -> BigDecimal.valueOf(150.00 * numberOfTicket);
            case REGULAR -> BigDecimal.valueOf(100.00 * numberOfTicket);
            case STUDENT -> BigDecimal.valueOf(50.00 * numberOfTicket);
            default -> throw new NotFoundException("Unknown ticket type: " + ticketType);
        };
    }

    /**
     * Sends a notification for booking confirmation or cancellation via Kafka.
     *
     * @param bookingEvent The booking event details.
     */
    private void sendNotificationBookingConfirmation(BookingEvent bookingEvent) {
        kafkaTemplate.send("notification", bookingEvent);
    }
}
