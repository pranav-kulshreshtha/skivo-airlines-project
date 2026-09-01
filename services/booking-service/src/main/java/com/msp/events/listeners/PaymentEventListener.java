package com.msp.events.listeners;

import com.msp.client.FlightClient;
import com.msp.client.PricingClient;
import com.msp.client.UserClient;
import com.msp.enums.BookingStatus;
import com.msp.events.PaymentCompletedEvent;
import com.msp.events.PaymentFailedEvent;
import com.msp.events.publishers.BookingEventProducer;
import com.msp.models.Booking;
import com.msp.payloads.DTO.UserDTO;
import com.msp.payloads.responses.FareResponse;
import com.msp.payloads.responses.FlightInstanceResponse;
import com.msp.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentEventListener {

    private final BookingRepository bookingRepository;
    private final FlightClient flightClient;
    private final PricingClient pricingClient;
    private final UserClient userClient;

    private final BookingEventProducer bookingEventProducer;

    @KafkaListener(topics = "payment-completed", groupId = "booking-service-group")
    @Transactional
    public void handlePaymentCompleted(PaymentCompletedEvent event) throws Exception {
        Booking booking = bookingRepository.findById(event.getBookingId())
                .orElse(null);

        if(booking == null)return;

        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        FlightInstanceResponse flightInstanceResponse = flightClient.getFlightInstanceById(
                booking.getFlightInstanceId());
        FareResponse fareResponse = pricingClient.getFareById(booking.getFareId());

        UserDTO userDTO = userClient.getUserById(booking.getUserId());

        //publish the event for seat-service and notification service

        bookingEventProducer.sendBookingConfirmed(booking, event, flightInstanceResponse,
                fareResponse, userDTO);
    }

    @KafkaListener(topics = "payment-failed", groupId = "booking-service-group")
    public void handlePaymentFailed(PaymentFailedEvent event) throws Exception {
        Booking booking = bookingRepository.findById(event.getBookingId())
                .orElse(null);

        if(booking == null)return;

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }
}
