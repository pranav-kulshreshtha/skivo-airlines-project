package com.msp.services.impl;

import com.msp.enums.TicketStatus;
import com.msp.models.Booking;
import com.msp.models.Passenger;
import com.msp.models.Ticket;
import com.msp.repositories.TicketRepository;
import com.msp.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public List<Ticket> generateTicketsForBooking(Booking booking) {
        List<Ticket> tickets = new ArrayList<>();
        for(Passenger passenger : booking.getPassengers()) {
            String ticketNumber = generateUniqueTicketNumber();
            Ticket ticket = Ticket.builder()
                    .ticketNumber(ticketNumber)
                    .status(TicketStatus.BOOKED)
                    .issuedAt(LocalDateTime.now())
                    .booking(booking)
                    .passenger(passenger)
                    .build();
            Ticket savedTicket = ticketRepository.save(ticket);
            tickets.add(savedTicket);
        }

        return tickets;
    }

    private String generateUniqueTicketNumber() {
        String ticketNumber;
        do {
            String datePart = LocalDateTime.now().toString().substring(0,10);
            String randomPart = UUID.randomUUID().toString().substring(0,8);
            ticketNumber = String.format("TKT-%s-%s", datePart, randomPart);
        } while(ticketRepository.existsByTicketNumber(ticketNumber));

        return "";
    }
}
