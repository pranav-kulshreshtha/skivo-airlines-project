package com.msp.controllers;

import com.msp.services.SeatInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
