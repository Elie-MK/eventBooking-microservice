package com.eventbooking.booking_service.exceptionshandller;

public class BookingCancelledException extends RuntimeException {
    public BookingCancelledException(String message) {
        super(message);
    }
}
