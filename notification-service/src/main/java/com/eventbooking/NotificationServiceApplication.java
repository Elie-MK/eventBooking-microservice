package com.eventbooking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notification", groupId = "notificationId")
    public void handleNotification(String message) {
        log.info("Received raw message: {}", message);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PaymentEvent event = objectMapper.readValue(message, PaymentEvent.class);
            log.info("Converted to PaymentEvent: {}", event);
            // Process the PaymentEvent
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize message to PaymentEvent: {}", e.getMessage());
        }
    }
}
