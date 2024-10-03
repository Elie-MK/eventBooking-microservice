package com.eventbooking.event_service.service;

import com.eventbooking.event_service.dto.EventDto;
import com.eventbooking.event_service.entities.Event;
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

    public EventDto createEvent(EventDto eventDto) {
        Event event = Event.builder()
                .name(eventDto.getName())
                .date(eventDto.getDate())
                .location(eventDto.getLocation())
                .build();

        log.info("Creating event with id : {}", event.getId());
        Event newEvent = eventRepository.save(event);
        return mapToDto(newEvent);
    }

    public List<EventDto> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<EventDto> getEventById(Long id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event != null) {
            return Optional.of(mapToDto(event));
        }
        return Optional.empty();
    }

    public String deleteEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            eventRepository.deleteById(event.getId());
            return "Event with id " + eventId + " was deleted";
        }
        return "Event with id " + eventId + " not found";
    }

    public Optional<EventDto> updateEvent(Long eventId, EventDto eventDto) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            event.setName(eventDto.getName());
            event.setDate(eventDto.getDate());
            event.setLocation(eventDto.getLocation());
            Event newEvent = eventRepository.save(event);
            return Optional.of(mapToDto(newEvent));
        }
        return Optional.empty();
    }

    private EventDto mapToDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .createdAt(event.getCreatedAt())
                .location(event.getLocation())
                .date(event.getDate())
                .build();
    }
}
