package com.eventbooking.paymentservice.exceptionhandler;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
