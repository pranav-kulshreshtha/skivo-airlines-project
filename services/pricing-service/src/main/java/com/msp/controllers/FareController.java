package com.msp.controllers;

import com.msp.payloads.requests.FareRequest;
import com.msp.payloads.responses.FareResponse;
import com.msp.services.FareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/fares")
public class FareController {

    private final FareService fareService;

    @PostMapping
    public ResponseEntity<FareResponse> createFare(
            @Valid @RequestBody FareRequest fareRequest
            ) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                fareService.createFare(fareRequest));
    }

    @GetMapping
    public ResponseEntity<?> getAllFares() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(fareService.getAllFares());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FareResponse> getFareById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(fareService.getFareById(id));
    }

    @PostMapping("/batch-by-ids")
    public ResponseEntity<Map<Long, FareResponse>> getFaresByIds(
            @RequestBody List<Long> ids
    ) {
        return ResponseEntity.ok(fareService.getFaresByIds(ids));
    }

    @GetMapping("/lowest/flight/{flightId}/cabin-class/{cabinClassId}")
    ResponseEntity<FareResponse> getLowestFareForFlightAndCabinClass(
            @PathVariable Long flightId,
            @PathVariable Long cabinClassId
    ) {
        return ResponseEntity.ok(fareService.getLowestFareForFlightAndCabin(
                flightId, cabinClassId));
    }

    @GetMapping("/flight/{flightId}/cabin-class/{cabinClassId}")
    public ResponseEntity<List<FareResponse>> getFaresByFlightAndCabinClass(
            @PathVariable Long flightId,
            @PathVariable Long cabinClassId
    ) {
        return ResponseEntity.ok(
                fareService.getFaresByFlightIdAndCabinClassId(flightId, cabinClassId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<FareResponse> updateFare(
            @PathVariable Long id,
            @RequestBody FareRequest fareRequest
    ) throws Exception {
        return ResponseEntity.ok(fareService.updateFare(id, fareRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FareResponse> deleteFare(@PathVariable Long id) throws Exception {
        fareService.deleteFare(id);
        return ResponseEntity.noContent().build();
    }
}
