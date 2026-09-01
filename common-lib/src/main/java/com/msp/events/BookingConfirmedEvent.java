package com.msp.events;


import com.msp.dto.PassengerNotificationData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingConfirmedEvent {

    //Booking information
    private Long bookingId;
    private String bookingReference;
    private LocalDateTime confirmedAt;
    private LocalDateTime bookingDate;
    private String cabinClass;
    private boolean flexibleTicket;

    //Contact information
    private Long userId;
    private String userName;
    private String contactEmail;
    private String contactPhone;

    //Passenger information
    private List<PassengerNotificationData> passengers;

    //Flight information
    private Long flightInstanceId;
    private String flightNumber;
    private String airlineName;
    private String airlineLogo;
    private String aircraftModel;

    //Departure information
    private String departureAirportCode;
    private String departureAirportName;
    private String departureCity;
    private String departureCountry;
    private LocalDateTime departureDateTime;

    // Arrival information
    private String arrivalAirportCode;
    private String arrivalAirportName;
    private String arrivalCity;
    private String arrivalCountry;
    private LocalDateTime arrivalDateTime;
    private String flightDuration;

    //Payment information
    private Double totalAmount;
    private String currency;
    private String transactionId;
    private String providerPaymentId;
    private String paymentGateway;
    private LocalDateTime paidAt;

    //Fare information
    private String fareName;
    private Double baseFare;
    private Double taxesAndFees;
    private Double seatFees;
    private Double ancillaryFees;
    private Double mealFees;

    //Baggage information
    private Integer checkinBaggagePieces;
    private Double checkinBaggageWeightPerPiece;
    private Integer cabinBaggagePieces;
    private Double cabinBaggageWeightPerPiece;

    //Fare benefits information
    private Boolean freeDateChange;
    private Boolean partialRefund;
    private Boolean fullRefund;
    private Boolean priorityBoarding;
    private Boolean loungeAccess;
    private Boolean complimentaryMeals;

    private List<Long> seatInstanceIds;

}
