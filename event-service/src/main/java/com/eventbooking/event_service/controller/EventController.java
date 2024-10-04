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

    /**
     * Retrieve an event by its ID.
     *
     * @param eventId the ID of the event to retrieve
     * @return a ResponseEntity containing the EventDto if found, or a 404 NOT FOUND status if not found
     */
    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EventDto> getEventById(@PathVariable Long eventId) {
        log.debug("Request to get an event with id: {}", eventId);
        var result = eventService.getEventById(eventId);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Retrieve an event by its name.
     *
     * @param eventName the name of the event to retrieve
     * @return a ResponseEntity containing the EventDto if found, or a 404 NOT FOUND status if not found
     */
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EventDto> getEventByName(@RequestParam String eventName){
        log.debug("Request to get an event with name : {}", eventName);
        var result = eventService.searchEvent(eventName);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Retrieve a list of all events.
     *
     * @return a ResponseEntity containing a list of EventDto representing all events
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<EventDto>> getAllEvents() {
        log.debug("Request to get all events");
        var result = eventService.getAllEvents();
        return ResponseEntity.ok(result);
    }


    /**
     * Create a new event.
     *
     * @param event the event data to create
     * @return a ResponseEntity containing the created EventDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EventDto> createEvent(@RequestBody EventDto event) {
        log.debug("Request to create an event: {}", event);
        var result = eventService.createEvent(event);
        return ResponseEntity.ok(result);
    }

    /**
     * Update an existing event by its ID.
     *
     * @param eventId the ID of the event to update
     * @param event   the updated event data
     * @return a ResponseEntity containing the updated EventDto
     */
    @PutMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Optional<EventDto>> updateEvent(@PathVariable Long eventId, @RequestBody EventDto event) {
        log.debug("Request to update an event with id: {}", eventId);
        var result = eventService.updateEvent(eventId, event);
        return ResponseEntity.ok(result);
    }

    /**
     * Delete an event by its ID.
     *
     * @param eventId the ID of the event to delete
     * @return a ResponseEntity containing a confirmation message
     */
    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        log.debug("Request to delete an event with id: {}", eventId);
        var result = eventService.deleteEventById(eventId);
        return ResponseEntity.ok(result);
    }


}
