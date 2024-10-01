package com.eventbooking.event_service.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "event")
public class Event {
    /**
     * The id of each Event
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * The name of event
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, unique = true, nullable = false)
    private String name;

    /**
     * The place where the event will happen
     */
    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    /**
     * The date when the event is schedule
     */
    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    /**
     * The date when the event is created
     */
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
