package com.msp.mappers;

import com.msp.models.Ticket;
import com.msp.payloads.responses.TicketResponse;

public class TicketMapper {

    public static TicketResponse toResponse(Ticket ticket) {
        if(ticket == null) return null;

        return TicketResponse.builder()
                .id(ticket.getId())
                .ticketNumber(ticket.getTicketNumber())
                .status(ticket.getStatus())
                .issuedAt(ticket.getIssuedAt())
                .bookingId(ticket.getBooking() == null ? null :
                        ticket.getBooking().getId())
                .bookingReference(ticket.getBooking() == null ? null :
                        ticket.getBooking().getBookingReference())
                .passengerId(ticket.getPassenger() == null ? null :
                        ticket.getPassenger().getId())
                .passengerFirstName(ticket.getPassenger() == null ? null :
                        ticket.getPassenger().getFirstName())
                .passengerLastName(ticket.getPassenger() == null ? null :
                        ticket.getPassenger().getLastName())
                .passengerEmail(ticket.getPassenger() == null ? null :
                        ticket.getPassenger().getEmail())
                .paymentId(null)
                .paymentAmount(null)
                .build();
    }

}
