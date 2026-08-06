package com.msp.events;

import com.msp.client.BookingClient;
import com.msp.enums.SeatAvailabilityStatus;
import com.msp.payloads.responses.BookingResponse;
import com.msp.payloads.responses.SeatInstanceResponse;
import com.msp.services.SeatInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentEventListener {

    private final BookingClient bookingClient;
    private final SeatInstanceService seatInstanceService;

    @KafkaListener(topics = "payment-completed", groupId = "seat-service-group")
    public void handleBookingConfirmed(PaymentCompletedEvent event) {

        BookingResponse bookingResponse = bookingClient.getBookingById(event.getBookingId());

        List<SeatInstanceResponse> seatInstanceResponseList = bookingResponse.getSeatInstances();

        for(SeatInstanceResponse seatInstanceResponse : seatInstanceResponseList) {
            seatInstanceService.updateSeatInstanceStatus(
                    seatInstanceResponse.getId(), SeatAvailabilityStatus.BOOKED);
        }

    }

}
