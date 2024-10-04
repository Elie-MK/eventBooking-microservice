package com.eventbooking.booking_service.exceptionshandller;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
