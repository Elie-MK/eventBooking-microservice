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
        Event event1 = eventRepository.save(event);
        return mapToDto(event1);
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
