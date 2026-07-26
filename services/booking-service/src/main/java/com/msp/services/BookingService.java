package com.msp.services;

import com.msp.enums.BookingStatus;
import com.msp.payloads.requests.BookingRequest;
import com.msp.payloads.responses.BookingResponse;
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request, Long userId);
    BookingResponse updateBooking(Long id, BookingRequest request);
    BookingResponse getBookingById(Long id) throws Exception;
    List<BookingResponse> getAllBookingsByAirline(Long airlineId,
                                                  String searchQuery,
                                                  BookingStatus status,
                                                  Long flightInstanceId,
                                                  String sortDirection
    );

    List<BookingResponse> getBookingsByUser(Long userId);
    BookingResponse cancelBooking(Long id) throws Exception;
    void deleteBooking(Long id) throws Exception;
}
