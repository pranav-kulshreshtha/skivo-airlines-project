package com.msp.services;

import com.msp.payloads.requests.FlightScheduleRequest;
import com.msp.payloads.responses.FlightScheduleResponse;

import java.util.List;

public interface FlightScheduleService {
    FlightScheduleResponse createFlightSchedule(Long userId,
                                                FlightScheduleRequest request) throws Exception;

    FlightScheduleResponse getFlightScheduleById(Long id) throws Exception;

    List<FlightScheduleResponse> getFlightScheduleByAirline(Long userId);

    FlightScheduleResponse updateFlightSchedule(Long id,
                                                FlightScheduleRequest request) throws Exception;

    void deleteFlightSchedule(Long id) throws Exception;

}
