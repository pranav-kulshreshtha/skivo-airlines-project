package com.msp.controllers;

import com.msp.payloads.responses.SeatInstanceResponse;
import com.msp.services.SeatInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/seat-instances/")
public class SeatInstanceController {

    private final SeatInstanceService seatInstanceService;

    @PostMapping("/price/total")
    public Double calculatedSeatPrice(@RequestBody List<Long> seatInstanceIds) {
        return seatInstanceService.calculateSeatPrice(seatInstanceIds);
    }

    @GetMapping("/all")
    ResponseEntity<List<SeatInstanceResponse>> getAllByIds(@RequestParam List<Long> Ids) {
        return ResponseEntity.ok(seatInstanceService.getAllByIds(Ids));
    }
}
