package com.eventbooking.paymentservice.exceptionhandler;

public class BookingIsCancelledException extends RuntimeException {
    public BookingIsCancelledException(String message) {
        super(message);
    }
}
