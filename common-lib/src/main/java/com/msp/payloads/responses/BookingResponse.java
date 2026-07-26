package com.msp.payloads.responses;

import com.msp.embaddables.ContactInfo;
import com.msp.enums.BookingStatus;
import com.msp.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {

    private Long id;
    private String bookingReference;

    private Long userId;
    private String userName;
    private String userEmail;

    private Long flightId;
    private String flightNumber;
    private String flightName;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private BookingStatus status;
    private Instant bookingDate;
    private Instant lastModified;

    private List<PassengerResponse> passengers;
    private List<SeatInstanceResponse> seatInstances;
    private PaymentLinkResponse payment;
    private List<FlightCabinAncillaryResponse> ancillaries;
    private List<FlightMealResponse> meals;
    private List<TicketResponse> tickets;

    //payment details
    private PaymentStatus paymentStatus;
    private String paymentLink;

    private FareResponse fare;

    private Integer totalPassengers;
    private Double totalAmount;

    //flight duration and other derived information
    private String flightDuration;
    private Boolean isUpcoming;
    private Boolean isPast;

    private ContactInfo contactInfo;
}

