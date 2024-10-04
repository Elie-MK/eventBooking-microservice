package com.eventbooking.event_service.exceptionshandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleBookingCancelled(NotFoundException ex){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ErrorResponse(HttpStatus.ACCEPTED.value(), ex.getMessage())
        );
    }

    // Define an error response class if necessary
    public static class ErrorResponse {
        private int status;
        private String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        // Getters and setters
        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}