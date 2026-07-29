package com.msp.services;

import com.msp.payloads.requests.FlightInstanceRequest;
import com.msp.payloads.responses.FlightInstanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface FlightInstanceService {
    FlightInstanceResponse createFlightInstance(Long userId, FlightInstanceRequest request) throws Exception;
    FlightInstanceResponse getFlightInstanceById(Long id) throws Exception;
    Page<FlightInstanceResponse> getByAirlineId(Long userId,
                                                Long departureAirportId,
                                                Long arrivalAirportId,
                                                Long flightId,
                                                LocalDate onDate,
                                                Pageable pageable);
    FlightInstanceResponse updateFlightResponse(Long id, FlightInstanceRequest request) throws Exception;
    void deleteFlightInstance(Long id) throws Exception;
}
