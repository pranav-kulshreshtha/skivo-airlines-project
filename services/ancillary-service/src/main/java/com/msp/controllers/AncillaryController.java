package com.msp.controllers;

import com.msp.payloads.requests.AncillaryRequest;
import com.msp.payloads.responses.AncillaryResponse;
import com.msp.payloads.responses.ApiResponse;
import com.msp.services.AncillaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ancillaries")
public class AncillaryController {

    private final AncillaryService ancillaryService;

    @PostMapping
    public ResponseEntity<AncillaryResponse> createAncillaryResponse(
            @Valid @RequestBody AncillaryRequest ancillaryRequest,
            @RequestHeader("X-User-Id") Long userId
        ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ancillaryService.createAncillary(userId,ancillaryRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AncillaryResponse> getAncillaryById(
            @PathVariable Long id
    ) throws Exception {
        return ResponseEntity.ok(ancillaryService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AncillaryResponse>> getAllByAirlineId(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return ResponseEntity.ok(ancillaryService.getByAirlineId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AncillaryResponse> updateAncillary(
            @PathVariable Long id,
            @RequestBody AncillaryRequest request
    ) throws Exception {
        return ResponseEntity.ok(ancillaryService.updateAncillary(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAncillary(@PathVariable Long id)
            throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        ancillaryService.deleteAncillary(id);

        apiResponse.setMessage("Ancillary deleted successfully!");
        return ResponseEntity.ok(apiResponse);
    }
}
