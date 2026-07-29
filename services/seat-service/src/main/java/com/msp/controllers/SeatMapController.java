package com.msp.controllers;

import com.msp.payloads.requests.SeatMapRequest;
import com.msp.payloads.responses.ApiResponse;
import com.msp.payloads.responses.SeatMapResponse;
import com.msp.services.SeatMapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/seat-maps")
public class SeatMapController {

    private final SeatMapService seatMapService;

    @PostMapping
    public ResponseEntity<SeatMapResponse> createSeatMap(
            @Valid @RequestBody SeatMapRequest seatMapRequest,
            @RequestHeader("X-User-Id") Long userId
            ) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(seatMapService.createSeatMap(userId, seatMapRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatMapResponse> getSeatMapById(
            @PathVariable Long id
    ) throws Exception {
        return ResponseEntity.ok(seatMapService.getSeatMapById(id));
    }

    @GetMapping("/cabin-class/{cabinClassId}")
    public ResponseEntity<SeatMapResponse> getSeatMapByCabinClass(
            @PathVariable Long cabinClassId
    ) {
        return ResponseEntity.ok(seatMapService.getSeatMapByCabinClass(cabinClassId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatMapResponse> updateSeatMap(
            @PathVariable Long id,
            @RequestBody SeatMapRequest seatMapRequest
    ) throws Exception {
        return ResponseEntity.ok(seatMapService.updateSeatMap(id, seatMapRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSeatMap(@PathVariable Long id)
            throws Exception {

        ApiResponse apiResponse = new ApiResponse();
        seatMapService.deleteSeatMap(id);

        apiResponse.setMessage("Seat Map deleted successfully!");
        return ResponseEntity.ok(apiResponse);
    }
}
