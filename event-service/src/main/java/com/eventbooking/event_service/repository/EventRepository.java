package com.eventbooking.event_service.repository;

import com.eventbooking.event_service.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Finds an event by its name, ignoring case.
     *
     * @param name the name of the event to search for, case insensitive
     * @return an Optional containing the found Event if exists, or an empty Optional if no event is found with the given name
     */
    @Query("SELECT e FROM Event e WHERE LOWER(e.name) = LOWER(:name)")
    Optional<Event> findByNameIgnoreCase(@Param("name") String name);
}