package com.eventbooking.event_service.service;

import com.eventbooking.event_service.dto.EventDto;
import com.eventbooking.event_service.entities.Event;
import com.eventbooking.event_service.exceptionshandler.NotFoundException;
import com.eventbooking.event_service.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;

    /**
     * Creates a new event from the provided EventDto.
     *
     * @param eventDto the data transfer object containing the event details
     * @return the created EventDto representing the newly created event
     */
    public EventDto createEvent(EventDto eventDto) {
        Event event = Event.builder()
                .name(eventDto.getName())
                .date(eventDto.getDate())
                .location(eventDto.getLocation())
                .ticketsAvailable(eventDto.getTicketsAvailable())
                .build();

        log.info("Creating event with id : {}", event.getId());
        Event newEvent = eventRepository.save(event);
        return mapToDto(newEvent);
    }

    /**
     * Retrieves all events.
     *
     * @return a list of EventDto representing all events
     */
    public List<EventDto> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an event by its ID.
     *
     * @param id the ID of the event to retrieve
     * @return an Optional containing the EventDto if found, or an empty Optional if not found
     */
    public Optional<EventDto> getEventById(Long id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null) {
            throw new NotFoundException("There no event with id : " + id);
        }
        return Optional.of(mapToDto(event));
    }

    /**
     * Deletes an event by its ID.
     *
     * @param eventId the ID of the event to delete
     * @return a message indicating the result of the deletion
     */
    public String deleteEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            eventRepository.deleteById(event.getId());
            return "Event with id " + eventId + " was deleted";
        }
        throw new NotFoundException( "Event with id " + eventId + " not found");
    }

    /**
     * Updates an existing event based on the provided EventDto.
     *
     * @param eventId the ID of the event to update
     * @param eventDto the data transfer object containing the updated event details
     * @return an Optional containing the updated EventDto if successful, or an empty Optional if the event is not found
     */
    public Optional<EventDto> updateEvent(Long eventId, EventDto eventDto) {
        Event event = eventRepository.findById(eventId).orElse(null);

        if (event != null) {
            if (eventDto.getName() != null && !eventDto.getName().equals(event.getName())) {
                event.setName(eventDto.getName());
            }
            if (eventDto.getDate() != null && !eventDto.getDate().equals(event.getDate())) {
                event.setDate(eventDto.getDate());
            }
            if (eventDto.getLocation() != null && !eventDto.getLocation().equals(event.getLocation())) {
                event.setLocation(eventDto.getLocation());
            }
            if (eventDto.getTicketsAvailable() != null && !eventDto.getTicketsAvailable().equals(event.getTicketsAvailable())) {
                event.setTicketsAvailable(eventDto.getTicketsAvailable());
            }

            Event updatedEvent = eventRepository.save(event);
            return Optional.of(mapToDto(updatedEvent));
        }

        return Optional.empty();
    }

    /**
     * Searches for an event by its name in a case-insensitive manner.
     *
     * @param eventName the name of the event to search for
     * @return an Optional containing the EventDto if found, or an empty Optional if not found
     */
    public Optional<EventDto> searchEvent(String eventName) {
        Optional<Event> event = eventRepository.findByNameIgnoreCase(eventName);
        return event.map(this::mapToDto);
    }

    private EventDto mapToDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .createdAt(event.getCreatedAt())
                .location(event.getLocation())
                .date(event.getDate())
                .ticketsAvailable(event.getTicketsAvailable())
                .build();
    }
}
