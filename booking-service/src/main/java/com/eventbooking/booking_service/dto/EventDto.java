package com.eventbooking.booking_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long id;
    /**
     * The name of event
     */
    private String name;

    /**
     * The place where the event will happen
     */
    private String location;

    /**
     * The date when the event is schedule
     */
    private LocalDate date;


    /**
     * The date when the event is created
     */
    private LocalDateTime createdAt;
}
