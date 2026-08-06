package com.msp.controllers;

import com.msp.payloads.requests.FlightMealRequest;
import com.msp.payloads.responses.ApiResponse;
import com.msp.payloads.responses.FlightMealResponse;
import com.msp.services.FlightMealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/flight-meals")
public class FlightMealController {

    private final FlightMealService flightMealService;

    @PostMapping
    public ResponseEntity<FlightMealResponse> createFlightMeal(
            @Valid @RequestBody FlightMealRequest request
    ) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(flightMealService.createFlightMeal(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightMealResponse> getFlightMealById(
            @PathVariable Long id
    ) throws Exception {
        return ResponseEntity.ok(flightMealService.getFlightMealById(id));
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<List<FlightMealResponse>> getByFlightId(
            @PathVariable Long flightId
    ) {
        return ResponseEntity.ok(flightMealService.getByFlightId(flightId));
    }

    @PostMapping("/all")
    public ResponseEntity<List<FlightMealResponse>> getMealsByIds(
            @RequestBody List<Long> ids
    ) {
        return ResponseEntity.ok(flightMealService.getAllByIds(ids));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightMealResponse> updateFlightMeal(
            @PathVariable Long id,
            @RequestBody FlightMealRequest request
    ) throws Exception {
        return ResponseEntity.ok(flightMealService.updateFlightMeal(id, request));
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<FlightMealResponse> updateFlightMealAvailability(
            @PathVariable Long id,
            @RequestParam Boolean availability
    ) throws Exception {
        return ResponseEntity.ok(
                flightMealService.updateFlightMealAvailability(id, availability)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteFlightMeal(
            @PathVariable Long id
    ) throws Exception {
        flightMealService.deleteFlightMeal(id);

        ApiResponse response = new ApiResponse();
        response.setMessage("Flight meal deleted successfully!");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/price/total")
    public ResponseEntity<Double> calculateMealPrice(
            @RequestBody List<Long> mealIds
    ) {
        return ResponseEntity.ok(
                flightMealService.calculateMealPrice(mealIds)
        );
    }
}