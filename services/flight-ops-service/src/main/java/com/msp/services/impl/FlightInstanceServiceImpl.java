package com.msp.services.impl;

import com.msp.client.AirlineClient;
import com.msp.client.LocationClient;
import com.msp.mappers.FlightInstanceMapper;
import com.msp.models.Flight;
import com.msp.models.FlightInstance;
import com.msp.payloads.requests.FlightInstanceRequest;
import com.msp.payloads.responses.AircraftResponse;
import com.msp.payloads.responses.AirlineResponse;
import com.msp.payloads.responses.AirportResponse;
import com.msp.payloads.responses.FlightInstanceResponse;
import com.msp.repositories.FlightInstanceRepository;
import com.msp.repositories.FlightRepository;
import com.msp.services.FlightInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class FlightInstanceServiceImpl implements FlightInstanceService {

    private final FlightRepository flightRepository;
    private final FlightInstanceRepository flightInstanceRepository;
    private final AirlineClient airlineClient;
    private final LocationClient locationClient;

    @Override
    public FlightInstanceResponse createFlightInstance(Long userId, FlightInstanceRequest request) throws Exception {

        //fetch airline by ownerId
        AirlineResponse airlineResponse = airlineClient.getAirlineByOwner(userId);
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new Exception("Flight not found!"));

        AircraftResponse aircraft = airlineClient.getAircraftById(flight.getAircraftId());

        FlightInstance flightInstance = FlightInstanceMapper.toEntity(request, flight);
        flightInstance.setTotalSeats(aircraft.getTotalSeats());
        flightInstance.setAvailableSeats(aircraft.getTotalSeats());

        FlightInstance saved = flightInstanceRepository.save(flightInstance);

        //todo : create seat instances
         // publish kafka event. seat service consume that and creates seat instance

        return convertToFlightInstanceResponse(saved);
    }

    @Override
    public FlightInstanceResponse getFlightInstanceById(Long id) throws Exception {
        FlightInstance flightInstance = flightInstanceRepository.findById(id)
                .orElseThrow(() -> new Exception("Flight instance not found with id!"));
        return convertToFlightInstanceResponse(flightInstance);
    }

    @Override
    public Page<FlightInstanceResponse> getByAirlineId(Long userId, Long departureAirportId, Long arrivalAirportId, Long flightId, LocalDate onDate, Pageable pageable) {

        AirlineResponse airlineResponse = airlineClient.getAirlineByOwner(userId);
        LocalDateTime start = onDate!=null ? onDate.atStartOfDay() : null;
        LocalDateTime end = onDate!=null ? onDate.plusDays(1).atStartOfDay()
                : null;

        return flightInstanceRepository.findByAirlineId(
                airlineResponse.getId(),
                departureAirportId, arrivalAirportId, flightId, start,
                end, pageable
        ).map(this::convertToFlightInstanceResponse);
    }

    @Override
    public FlightInstanceResponse updateFlightResponse(Long id, FlightInstanceRequest request) throws Exception {
        FlightInstance existing = flightInstanceRepository.findById(id)
                .orElseThrow(() -> new Exception("Flight instance not found with id!"));

        FlightInstanceMapper.updateEntity(request, existing);
        FlightInstance updated = flightInstanceRepository.save(existing);
        return convertToFlightInstanceResponse(updated);
    }

    @Override
    public void deleteFlightInstance(Long id) throws Exception {
        FlightInstance flight = flightInstanceRepository.findById(id)
                .orElseThrow(() -> new Exception("Flight instance not found with id!"));

        flightInstanceRepository.delete(flight);
    }

    private FlightInstanceResponse convertToFlightInstanceResponse(FlightInstance flightInstance) {
        // service to service communication
        AirlineResponse airline = airlineClient.getAirlineById(
                flightInstance.getAirlineId());
        AirportResponse arrivalAirport = locationClient.getAirportById(
                flightInstance.getArrivalAirportId());
        AirportResponse departureAirport = locationClient.getAirportById(
                flightInstance.getDepartureAirportId());
        AircraftResponse aircraft = airlineClient.getAircraftById(
                flightInstance.getFlight().getAircraftId());

        return FlightInstanceMapper.toResponse(flightInstance,
                aircraft,
                airline,
                departureAirport,
                arrivalAirport);
    }
}
