package com.eventbooking.event_service.controller;

import com.eventbooking.event_service.dto.EventDto;
import com.eventbooking.event_service.entities.Event;
import com.eventbooking.event_service.service.EventService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/events")
public class EventController {
    private final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;


    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<EventDto> getEventById(@PathVariable Long eventId) {
        log.debug("Request to get an event with id: {}", eventId);
        var result = eventService.getEventById(eventId);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<List<EventDto>> getAllEvents() {
        log.debug("Request to get all events");
        var result = eventService.getAllEvents();
        return ResponseEntity.ok(result);
    }

    /**
     * Request to create a new event
     *
     * @param event
     * @return
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EventDto> createEvent(@RequestBody EventDto event) {
        log.debug("Request to create an event: {}", event);
        var result = eventService.createEvent(event);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Optional<EventDto>> updateEvent(@PathVariable Long eventId, @RequestBody EventDto event) {
        log.debug("Request to update an event with id: {}", eventId);
        var result = eventService.updateEvent(eventId, event);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        log.debug("Request to delete an event with id: {}", eventId);
        var result = eventService.deleteEventById(eventId);
        return ResponseEntity.ok(result);
    }


}
