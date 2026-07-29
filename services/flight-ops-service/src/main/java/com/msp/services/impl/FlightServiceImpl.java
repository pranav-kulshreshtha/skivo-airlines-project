package com.msp.services.impl;

import com.msp.client.AirlineClient;
import com.msp.client.LocationClient;
import com.msp.enums.FlightStatus;
import com.msp.mappers.FlightMapper;
import com.msp.models.Flight;
import com.msp.payloads.requests.FlightRequest;
import com.msp.payloads.responses.AircraftResponse;
import com.msp.payloads.responses.AirlineResponse;
import com.msp.payloads.responses.AirportResponse;
import com.msp.payloads.responses.FlightResponse;
import com.msp.repositories.FlightRepository;
import com.msp.services.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final AirlineClient airlineClient;
    private final LocationClient locationClient;

    @Override
    public FlightResponse createFlight(Long userId, FlightRequest request) throws Exception {
        if(flightRepository.existsByFlightNumber(request.getFlightNumber())) {
            throw new Exception("Flight with given number already exists!");
        }

        //fetching airline by owner id
        AirlineResponse airlineResponse = airlineClient.getAirlineByOwner(userId);
        request.setAirlineId(airlineResponse.getId());
        Flight flight = FlightMapper.toEntity(request);
        Flight savedFlight = flightRepository.save(flight);
        return convertToFlightResponse(savedFlight);
    }

    @Override
    public Page<FlightResponse> getFlightsByAirline(Long userId, Long departureAirportId, Long arrivalAirportId, Pageable pageable) {

        AirlineResponse airlineResponse = airlineClient.getAirlineByOwner(userId);

        return flightRepository.findByAirlineId(
                airlineResponse.getId(),
                departureAirportId,
                arrivalAirportId,
                pageable).map(this::convertToFlightResponse
        );
    }

    @Override
    public FlightResponse getFlightById(Long id) throws Exception {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new Exception("Flight with given id does not exist!"));
        return convertToFlightResponse(flight);
    }

    @Override
    public FlightResponse updateFlight(Long id, FlightRequest flightRequest) throws Exception {
        Flight existing = flightRepository.findById(id)
                .orElseThrow(() -> new Exception("Flight with given id does not exist!"));

        if(flightRequest.getFlightNumber()!=null
            && flightRepository.existsByFlightNumberAndIdNot(
                flightRequest.getFlightNumber(), id
        )) {
            throw new Exception("Flight with given number already exists!");
        }

        FlightMapper.updateEntity(flightRequest, existing);
        Flight updated = FlightMapper.toEntity(flightRequest);
        return convertToFlightResponse(updated);
    }

    @Override
    public FlightResponse changeStatus(Long id, FlightStatus status) throws Exception {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new Exception("Flight with given id does not exist!"));
        flight.setStatus(status);
        Flight updated = flightRepository.save(flight);
        return convertToFlightResponse(updated);
    }

    @Override
    public void deleteFlight(Long userId, Long id) throws Exception {
        AirlineResponse airlineResponse = airlineClient.getAirlineByOwner(userId);
        Flight flight = flightRepository.findByAirlineIdAndId(
                airlineResponse.getId(), id)
                .orElseThrow(() -> new Exception("Flight with given id does not exist!"));
        flightRepository.delete(flight);
    }

    public FlightResponse convertToFlightResponse(Flight flight) {
        AircraftResponse aircraft = airlineClient.getAircraftById(
                flight.getAircraftId());
        AirlineResponse airline = airlineClient.getAirlineById(
                flight.getAirlineId());
        AirportResponse arrivalAirport = locationClient.getAirportById(
                flight.getArrivalAirportId());
        AirportResponse departureAirport = locationClient.getAirportById(
                flight.getDepartureAirportId());

        return FlightMapper.toResponse(flight,
                aircraft,
                airline,
                departureAirport,
                arrivalAirport);
    }
}
