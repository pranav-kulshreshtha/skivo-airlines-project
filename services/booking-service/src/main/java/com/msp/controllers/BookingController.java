package com.msp.controllers;

import com.msp.enums.BookingStatus;
import com.msp.payloads.requests.BookingRequest;
import com.msp.payloads.responses.ApiResponse;
import com.msp.payloads.responses.BookingResponse;
import com.msp.services.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody BookingRequest request) {

        return new ResponseEntity<>(
                bookingService.createBooking(request, userId),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingRequest request) {

        return ResponseEntity.ok(
                bookingService.updateBooking(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable Long id) throws Exception {

        return ResponseEntity.ok(
                bookingService.getBookingById(id));
    }

    @GetMapping("/airline")
    public ResponseEntity<List<BookingResponse>> getAllBookingsByAirline(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) Long flightInstanceId,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        return ResponseEntity.ok(
                bookingService.getAllBookingsByAirline(
                        userId,
                        searchQuery,
                        status,
                        flightInstanceId,
                        sortDirection));
    }

    @GetMapping("/user/history")
    public ResponseEntity<List<BookingResponse>> getBookingsByUser(
            @RequestHeader("X-User-Id") Long userId) {

        return ResponseEntity.ok(
                bookingService.getBookingsByUser(userId));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long id) throws Exception {

        return ResponseEntity.ok(
                bookingService.cancelBooking(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBooking(
            @PathVariable Long id) throws Exception {

        bookingService.deleteBooking(id);
        ApiResponse apiResponse = new ApiResponse("Booking deleted successfully");
        return ResponseEntity.ok(apiResponse);
    }
}