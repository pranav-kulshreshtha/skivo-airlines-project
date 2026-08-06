package com.msp.services;

import com.msp.enums.BookingStatus;
import com.msp.exceptions.PaymentException;
import com.msp.payloads.requests.BookingRequest;
import com.msp.payloads.responses.BookingResponse;
import com.msp.payloads.responses.PaymentInitiateResponse;
import org.apache.kafka.common.errors.ResourceNotFoundException;

import java.util.List;

public interface BookingService {

    PaymentInitiateResponse createBooking(BookingRequest request, Long userId)
            throws ResourceNotFoundException, PaymentException;

    BookingResponse updateBooking(Long id, BookingRequest request)
            throws ResourceNotFoundException;

    BookingResponse getBookingById(Long id) throws ResourceNotFoundException;



    List<BookingResponse> getBookingsByAirline(
            Long userId,
            String searchQuery,
            BookingStatus status,
            Long flightInstanceId,
            String sortDirection
    );

    List<BookingResponse> getBookingsByUser(Long userId);

    BookingResponse cancelBooking(Long id) throws ResourceNotFoundException;

    void deleteBooking(Long id) throws ResourceNotFoundException;

    boolean existsById(Long id);

    long count();

    long countByFlightId(Long flightId);

//    BookingStatisticsResponse getBookingStatisticsForAirline(Long airlineId);
}
