package com.msp.services.impl;

import com.msp.client.AirlineClient;
import com.msp.client.LocationClient;
import com.msp.enums.FlightStatus;
import com.msp.mappers.FlightScheduleMapper;
import com.msp.models.Flight;
import com.msp.models.FlightSchedule;
import com.msp.payloads.requests.FlightInstanceRequest;
import com.msp.payloads.requests.FlightScheduleRequest;
import com.msp.payloads.responses.AirlineResponse;
import com.msp.payloads.responses.AirportResponse;
import com.msp.payloads.responses.FlightScheduleResponse;
import com.msp.repositories.FlightRepository;
import com.msp.repositories.FlightScheduleRepository;
import com.msp.services.FlightInstanceService;
import com.msp.services.FlightScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FlightScheduleServiceImpl implements FlightScheduleService {

    private final FlightRepository flightRepository;
    private final FlightScheduleRepository flightScheduleRepository;
    private final FlightInstanceService flightInstanceService;
    private final LocationClient locationClient;
    private final AirlineClient airlineClient;

    @Override
    public FlightScheduleResponse createFlightSchedule(Long userId, FlightScheduleRequest request) throws Exception {

        AirlineResponse airlineResponse = airlineClient.getAirlineByOwner(userId);
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new Exception("Flight not found with given id!"));

        if(request.getEndDate().isBefore(request.getStartDate())) {
            throw new Exception("End date is before start date!");
        }

        FlightSchedule flightSchedule = FlightScheduleMapper.toEntity(request, flight);
        FlightSchedule savedSchedule = flightScheduleRepository.save(flightSchedule);

        //create flight instances for saved schedule

        List<DayOfWeek> operatingDays = savedSchedule.getOperatingDays();
        LocalDate startDate = savedSchedule.getStartDate();
        LocalDate endDate = savedSchedule.getEndDate();

        FlightInstanceRequest flightInstanceRequest = FlightInstanceRequest.builder()
                .scheduleId(savedSchedule.getId())
                .flightId(flight.getId())
                .arrivalAirportId(flight.getArrivalAirportId())
                .departureAirportId(flight.getDepartureAirportId())
                .status(FlightStatus.SCHEDULED)
                .build();

        for(LocalDate date = startDate;!date.isAfter(endDate);date = date.plusDays(1)) {
            if(operatingDays.contains(date.getDayOfWeek())){
                flightInstanceRequest.setDepartureDateTime(
                        LocalDateTime.of(date, savedSchedule.getDepartureTime())
                );
                flightInstanceRequest.setArrivalDateTime(
                        LocalDateTime.of(date, savedSchedule.getArrivalTime())
                );
                flightInstanceService.createFlightInstance(airlineResponse.getId(),
                        flightInstanceRequest);
            }
        }

        return convertToFlightScheduleResponse(savedSchedule);
    }

    @Override
    public FlightScheduleResponse getFlightScheduleById(Long id) throws Exception {
        FlightSchedule schedule = flightScheduleRepository.findById(id)
                .orElseThrow(() -> new Exception("Schedule with given id not found!"));
        return convertToFlightScheduleResponse(schedule);
    }

    @Override
    public List<FlightScheduleResponse> getFlightScheduleByAirline(Long userId) {
        AirlineResponse airlineResponse = airlineClient.getAirlineByOwner(userId);
        List<FlightSchedule> schedules = flightScheduleRepository.findByFlightAirlineId(
                airlineResponse.getId());
        return schedules.stream()
                .map(this::convertToFlightScheduleResponse)
                .toList();
    }

    @Override
    public FlightScheduleResponse updateFlightSchedule(Long id, FlightScheduleRequest request) throws Exception {
        FlightSchedule existing = flightScheduleRepository.findById(id)
                .orElseThrow(() -> new Exception("Schedule with given id not found!"));
        FlightScheduleMapper.updateEntity(request, existing);
        FlightSchedule updated = flightScheduleRepository.save(existing);
        return convertToFlightScheduleResponse(updated);
    }

    @Override
    public void deleteFlightSchedule(Long id) throws Exception {
        FlightSchedule schedule = flightScheduleRepository.findById(id)
                .orElseThrow(() -> new Exception("Schedule with given id not found!"));
        flightScheduleRepository.delete(schedule);
    }

    private FlightScheduleResponse convertToFlightScheduleResponse(
            FlightSchedule flightSchedule
    ) {
        //service to service communication
        AirportResponse arrivalAirport = locationClient.getAirportById(
                flightSchedule.getArrivalAirportId());
        AirportResponse departureAirport = locationClient.getAirportById(
                flightSchedule.getDepartureAirportId());

        return FlightScheduleMapper.toResponse(
                flightSchedule, arrivalAirport, departureAirport
        );
    }
}
