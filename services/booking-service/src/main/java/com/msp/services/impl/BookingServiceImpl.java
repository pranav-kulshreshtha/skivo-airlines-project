package com.msp.services.impl;

import com.msp.enums.BookingStatus;
import com.msp.mappers.BookingMapper;
import com.msp.models.Booking;
import com.msp.models.Passenger;
import com.msp.payloads.DTO.PaymentDTO;
import com.msp.payloads.requests.BookingRequest;
import com.msp.payloads.requests.PassengerRequest;
import com.msp.payloads.responses.*;
import com.msp.repositories.BookingRepository;
import com.msp.services.BookingService;
import com.msp.services.PassengerService;
import com.msp.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerService passengerService;
    private final TicketService ticketService;

    @Override
    public BookingResponse createBooking(BookingRequest request, Long userId) {
        //step 1 : creating unique booking reference
        String bookingReference = generateBookingReference();

        //step 2 : create passengers
        Set<Passenger> passengers = new HashSet<>();
        for(PassengerRequest passengerRequest : request.getPassengers()) {
            Passenger passenger = passengerService.createPassenger(
                    passengerRequest, userId);
            passengers.add(passenger);
        }

        //todo step 3 : check if flight exists

        //step 4 : create booking with pending status
        Booking booking = BookingMapper.toEntity(
                request,
                userId,
                passengers,
                bookingReference
        );
        //todo : set airlineId from flight response
        booking.setAirlineId(1L);

        //step 5 : set seat instance ids
        List<Long> seatInstanceIds = request.getPassengers()
                .stream()
                .map(PassengerRequest::getSeatInstanceId)
                .toList();
        booking.setSeatInstanceIds(seatInstanceIds);

        Booking savedBooking = bookingRepository.save(booking);

        //set booking reference on passengers
        for(Passenger passenger : passengers) {
            passenger.setBooking(savedBooking);
        }

        //step 6 : generate tickets for each passenger
        ticketService.generateTicketsForBooking(booking);

        //todo step 7 : calculate price

        //todo step 8 : initiate payment using payment service

        return convertToBookingResponse(savedBooking);
    }

    @Override
    public BookingResponse updateBooking(Long id, BookingRequest request) {
        return null;
    }

    @Override
    public BookingResponse getBookingById(Long id) throws Exception {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new Exception("Booking with given id not found!"));
        return convertToBookingResponse(booking);
    }

    @Override
    public List<BookingResponse> getAllBookingsByAirline(
            Long airlineId,
            String searchQuery,
            BookingStatus status,
            Long flightInstanceId,
            String sortDirection) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ?
                Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction,"bookingDate");
        List<Booking> bookings = bookingRepository.findByAirlineWithFilter(
                airlineId,
                searchQuery,
                status,
                flightInstanceId,
                sort
        );
        return bookings.stream()
                .map(this::convertToBookingResponse)
                .toList();
    }

    @Override
    public List<BookingResponse> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(this::convertToBookingResponse)
                .toList();
    }

    @Override
    public BookingResponse cancelBooking(Long id) throws Exception {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new Exception("Booking with given id not found!"));
        booking.setStatus(BookingStatus.CANCELLED);
        Booking updated = bookingRepository.save(booking);
        return convertToBookingResponse(updated);
    }

    @Override
    public void deleteBooking(Long id) throws Exception {
        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new Exception("Booking with given id not found!"));
        bookingRepository.delete(existing);
    }
    
    private String generateBookingReference() {
        String reference;
        do {
            reference = "BK" + UUID.randomUUID().toString().substring(0,8)
                    .toUpperCase();
        } while(bookingRepository.existsByBookingReference(reference));

        return reference;
    }

    private BookingResponse convertToBookingResponse(Booking booking) {
        //todo : implement service to service communication later

        List<FlightCabinAncillaryResponse> fcaResponses = new ArrayList<>();
        List<FlightMealResponse> fmResponses = new ArrayList<>();
        PaymentDTO paymentDTO = new PaymentDTO();
        FareResponse fareResponse = new FareResponse();
        FlightResponse flightResponse = new FlightResponse();

        List<SeatInstanceResponse> seatResponses = new ArrayList<>();
        FlightInstanceResponse flightInstanceResponse = new FlightInstanceResponse();

        return BookingMapper.toResponse(
                booking,
                paymentDTO,
                fareResponse,
                flightResponse,
                flightInstanceResponse,
                fcaResponses,
                fmResponses,
                seatResponses
        );
    }
}
