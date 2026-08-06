package com.msp.mappers;

import com.msp.enums.BookingStatus;
import com.msp.models.Booking;
import com.msp.models.Passenger;
import com.msp.payloads.DTO.PaymentDTO;
import com.msp.payloads.requests.BookingRequest;
import com.msp.payloads.responses.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

public class BookingMapper {

    public static Booking toEntity(
            BookingRequest request,
            Long userId,
            Set<Passenger> passengers,
            String bookingReference) {
        if(request == null) return null;

        return Booking.builder()
                .bookingReference(bookingReference)
                .userId(userId)
                .flightId(request.getFlightId())
                .flightInstanceId(request.getFlightInstanceId())
                .cabinClassType(request.getCabinClass())
                .fareId(request.getFareId())
                .contactInfo(request.getContactInfo())
                .passengers(passengers)
                .ancillaryIds(request.getAncillaryIds())
                .mealIds(request.getMealIds())
                .status(BookingStatus.PENDING)
                .build();
    }

    public static BookingResponse toResponse(
            Booking booking,
            PaymentDTO paymentDTO,
            FareResponse fareResponse,
            FlightResponse flightResponse,
            FlightInstanceResponse flightInstanceResponse,
            List<FlightCabinAncillaryResponse> ancillaries,
            List<FlightMealResponse> meals,
            List<SeatInstanceResponse> seats) {

        if(booking == null) return null;

        List<PassengerResponse> passengerResponses = booking.getPassengers() == null
                ? null : booking.getPassengers().stream()
                .map(PassengerMapper::toResponse)
                .toList();

        List<TicketResponse> ticketResponses = booking.getTickets() == null ? null :
                booking.getTickets().stream()
                        .map(TicketMapper::toResponse)
                        .toList();

        return BookingResponse.builder()
                .id(booking.getId())
                .bookingReference(booking.getBookingReference())
                .userId(booking.getUserId())

                //flight details
                .flightId(booking.getFlightId())
                .flightNumber(flightResponse == null ? null : flightResponse.getFlightNumber())
                .flightName((flightResponse == null
                        || flightResponse.getDepartureAirport() == null
                        || flightResponse.getArrivalAirport() == null)  ? null :
                        flightResponse.getDepartureAirport().getName() + " → " +
                                flightResponse.getArrivalAirport().getName())

                //flight time and duration details
                .departureTime(flightInstanceResponse == null ? null :
                        flightInstanceResponse.getDepartureDateTime())
                .arrivalTime(flightInstanceResponse == null ? null :
                        flightInstanceResponse.getArrivalDateTime())
                .flightDuration(flightInstanceResponse == null ? null :
                        flightInstanceResponse.getFormattedDuration())

                //location details
                .departureAirport((flightResponse == null
                        || flightResponse.getDepartureAirport() == null) ? null :
                        flightResponse.getDepartureAirport().getName())
                .arrivalAirport((flightResponse == null
                        || flightResponse.getArrivalAirport() == null) ? null :
                        flightResponse.getArrivalAirport().getName())
                .status(booking.getStatus())
                .bookingDate(booking.getBookingDate())
                .lastModified(booking.getLastModified())

                //lists generated locally in this method
                .passengers(passengerResponses)
                .tickets(ticketResponses)

                .totalPassengers(booking.getPassengers().size())

                //fields passed in this method
                .ancillaries(ancillaries)
                .meals(meals)
                .seatInstances(seats)
                .paymentStatus(paymentDTO != null ? paymentDTO.getStatus() : null)
                .payment(paymentDTO == null ? null :
                        PaymentLinkResponse.builder()
                                 .id(paymentDTO.getId())
                                .build())

                //fare details
                .fare(fareResponse)
                .totalAmount(fareResponse!=null ? fareResponse.getTotalPrice() : null)
                .contactInfo(booking.getContactInfo())
                .build();
    }

    public static void updateEntityFromRequest(
            BookingRequest request, Booking booking,
            Set<Passenger> passengers) {
        booking.setFlightInstanceId(request.getFlightInstanceId());
        booking.setFlightId(request.getFlightId());
        booking.setFareId(request.getFareId());
        booking.setPassengers(passengers);
        booking.setLastModified(Instant.from(LocalDateTime.now()));
    }

}
