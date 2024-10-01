package com.eventbooking.payment_service.repository;

import com.eventbooking.payment_service.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByBookingId(Long bookingId);
}
