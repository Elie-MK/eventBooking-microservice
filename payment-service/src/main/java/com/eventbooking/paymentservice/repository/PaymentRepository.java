package com.eventbooking.paymentservice.repository;

import com.eventbooking.paymentservice.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.bookingId = :bookingId")
    List<Payment> findByBookingId(Long bookingId);
}
