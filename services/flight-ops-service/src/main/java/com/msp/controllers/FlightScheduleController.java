package com.msp.controllers;

import com.msp.payloads.requests.FlightScheduleRequest;
import com.msp.payloads.responses.ApiResponse;
import com.msp.payloads.responses.FlightScheduleResponse;
import com.msp.services.FlightScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/flight-schedules")
public class FlightScheduleController {

    private final FlightScheduleService flightScheduleService;

    @PostMapping
    public ResponseEntity<FlightScheduleResponse> createFlightSchedule(
            @Valid @RequestBody FlightScheduleRequest flightScheduleRequest,
            @RequestHeader("X-User-Id") Long userId
            ) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(flightScheduleService.createFlightSchedule(
                userId, flightScheduleRequest
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightScheduleResponse> getFlightScheduleById(
            @PathVariable Long id
    ) throws Exception {
        return ResponseEntity.ok(flightScheduleService.getFlightScheduleById(id));
    }

    @GetMapping
    public ResponseEntity<?> getFlightSchedules(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return ResponseEntity.ok(
                flightScheduleService.getFlightScheduleByAirline(userId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightScheduleResponse> updateFlightSchedule(
            @PathVariable Long id,
            @RequestBody FlightScheduleRequest request
    ) throws Exception {
        return ResponseEntity.ok(
                flightScheduleService.updateFlightSchedule(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FlightScheduleResponse> deleteFlightSchedule(
            @PathVariable Long id
    ) throws Exception {
        flightScheduleService.deleteFlightSchedule(id);
        ApiResponse apiResponse = new ApiResponse("Schedule deleted successfully!");
        return ResponseEntity.noContent().build();
    }
 }
