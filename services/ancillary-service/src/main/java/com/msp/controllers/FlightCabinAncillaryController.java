package com.msp.controllers;

import com.msp.enums.AncillaryType;
import com.msp.payloads.requests.FlightCabinAncillaryRequest;
import com.msp.payloads.responses.FlightCabinAncillaryResponse;
import com.msp.services.FlightCabinAncillaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/flight-cabin-ancillaries")
@RequiredArgsConstructor
public class FlightCabinAncillaryController {

    private final FlightCabinAncillaryService flightCabinAncillaryService;

    @PostMapping
    public ResponseEntity<FlightCabinAncillaryResponse> createFlightCabinAncillary(
            @Valid @RequestBody FlightCabinAncillaryRequest request) throws Exception {
        return new ResponseEntity<>(
                flightCabinAncillaryService.createFlightCabinAncillary(request),
                HttpStatus.CREATED);
    }

    @PostMapping("/ids")
    public ResponseEntity<List<FlightCabinAncillaryResponse>> getAllByIds(
            @RequestBody List<Long> ids) {
        return ResponseEntity.ok(
                flightCabinAncillaryService.getAllByIds(ids));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightCabinAncillaryResponse> getById(
            @PathVariable Long id) throws Exception {
        return ResponseEntity.ok(
                flightCabinAncillaryService.getById(id));
    }

    @GetMapping("/flight/{flightId}/cabin/{cabinClassId}")
    public ResponseEntity<List<FlightCabinAncillaryResponse>> getByFlightAndCabinClass(
            @PathVariable Long flightId,
            @PathVariable Long cabinClassId) {
        return ResponseEntity.ok(
                flightCabinAncillaryService.getByFlightAndCabinClass(
                        flightId, cabinClassId));
    }

    @GetMapping("/flight/{flightId}/cabin/{cabinClassId}/type/{type}")
    public ResponseEntity<FlightCabinAncillaryResponse> getByFlightIdAndCabinClassIdAndType(
            @PathVariable Long flightId,
            @PathVariable Long cabinClassId,
            @PathVariable AncillaryType type) {
        return ResponseEntity.ok(
                flightCabinAncillaryService.getByFlightIdAndCabinClassIdAndType(
                        flightId, cabinClassId, type));
    }

    @GetMapping("/flight/{flightId}/cabin/{cabinClassId}/type/{type}/all")
    public ResponseEntity<List<FlightCabinAncillaryResponse>> getAllByFlightIdAndCabinClassIdAndType(
            @PathVariable Long flightId,
            @PathVariable Long cabinClassId,
            @PathVariable AncillaryType type) {
        return ResponseEntity.ok(
                flightCabinAncillaryService.getAllByFlightIdAndCabinClassIdAndType(
                        flightId, cabinClassId, type));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightCabinAncillaryResponse> updateFlightCabinAncillary(
            @PathVariable Long id,
            @Valid @RequestBody FlightCabinAncillaryRequest request) throws Exception {
        return ResponseEntity.ok(
                flightCabinAncillaryService.updateFlightCabinAncillary(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlightCabinAncillary(
            @PathVariable Long id) throws Exception {
        flightCabinAncillaryService.deleteFlightCabinAncillary(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/price/total")
    public ResponseEntity<Double> calculateAncillaryPrice(
            @RequestBody List<Long> ancillaryIds) {
        return ResponseEntity.ok(
                flightCabinAncillaryService.calculateAncillaryPrice(ancillaryIds));
    }
}