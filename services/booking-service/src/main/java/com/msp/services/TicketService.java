package com.msp.services;

import com.msp.models.Booking;
import com.msp.models.Ticket;
import java.util.List;

public interface TicketService {
    List<Ticket> generateTicketsForBooking(Booking booking);
}
