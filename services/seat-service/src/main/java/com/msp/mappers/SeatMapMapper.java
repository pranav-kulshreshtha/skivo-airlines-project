package com.msp.mappers;

import com.msp.models.CabinClass;
import com.msp.models.Seat;
import com.msp.models.SeatMap;
import com.msp.payloads.requests.SeatMapRequest;
import com.msp.payloads.responses.SeatMapResponse;
import com.msp.payloads.responses.SeatResponse;

import java.util.List;

public class SeatMapMapper {
    public static SeatMap toEntity(SeatMapRequest request, CabinClass cabinClass) {
        if(request == null)return null;

        return SeatMap.builder()
                .name(request.getName())
                .totalRows(request.getTotalRows())
                .rightSeatsPerRow(request.getRightSeatsPerRow())
                .leftSeatsPerRow(request.getLeftSeatsPerRow())
//                .airlineId(airlineId)
                .cabinClass(cabinClass)
                .build();
    }

    public static SeatMapResponse toResponse(SeatMap seatMap) {
        if(seatMap == null) return null;

        List<Seat> seats = seatMap.getSeats();
        List<SeatResponse> seatResponse = seats
                .stream()
                .map(SeatMapper::toResponse)
                .toList();
        int totalSeats = seats != null ? seats.size() : 0;
        int availableSeats = seats != null ? (int) seats.stream().filter(seat ->
                                                                         Boolean.TRUE.equals(seat.getIsAvailable()) &&
                                                                                 Boolean.TRUE.equals(seat.getIsActive()) &&
                                                                         !Boolean.TRUE.equals(seat.getIsBlocked())).count() : 0;
        int windowSeats = seats != null ? (int) seats.stream().filter(seat ->
                seat.getSeatType().name().contains("WINDOW")).count() : 0;

        int aisleSeats = seats != null ? (int) seats.stream().filter(seat ->
                seat.getSeatType().name().contains("AISLE")).count() : 0;

        int middleSeats = seats != null ? (int) seats.stream().filter(seat ->
                seat.getSeatType().name().contains("MIDDLE")).count() : 0;

        return SeatMapResponse.builder()
                .id(seatMap.getId())
                .name(seatMap.getName())
                .totalRows(seatMap.getTotalRows())
                .rightSeatsPerRow(seatMap.getRightSeatsPerRow())
                .leftSeatsPerRow(seatMap.getLeftSeatsPerRow())
                .airlineId(seatMap.getAirlineId())
                .cabinClassId(seatMap.getCabinClass() != null ?
                        seatMap.getCabinClass().getId() : null)
                .cabinClassName(seatMap.getCabinClass() != null ?
                        seatMap.getCabinClass().getName().toString() : null)
                .cabinClassCode(seatMap.getCabinClass() != null ?
                        seatMap.getCabinClass().getCode() : null)
                .totalSeats(totalSeats)
                .availableSeats(availableSeats)
                .occupiedSeats(totalSeats - availableSeats)
                .windowSeats(windowSeats)
                .aisleSeats(aisleSeats)
                .middleSeats(middleSeats)
                .seats(seatResponse)
                .build();
    }

    public static void updateEntity(
            SeatMapRequest request,
            SeatMap existing
    ) {
        if(request == null || existing == null) return;

        if(request.getName() != null) existing.setName(request.getName());
        existing.setTotalRows(request.getTotalRows());
        existing.setRightSeatsPerRow(request.getRightSeatsPerRow());
        existing.setLeftSeatsPerRow(request.getLeftSeatsPerRow());
    }

    public static SeatMapResponse toSimpleResponse(SeatMap seatMap) {
        if(seatMap == null)return null;

        return SeatMapResponse.builder()
                .totalRows(seatMap.getTotalRows())
                .leftSeatsPerRow(seatMap.getLeftSeatsPerRow())
                .rightSeatsPerRow(seatMap.getRightSeatsPerRow())
                .build();
    }
}
