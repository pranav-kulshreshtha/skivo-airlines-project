package com.msp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "seat-price")
public interface SeatClient {

    @PostMapping("/api/seat-instances/price/total")
    Double calculatedSeatPrice(@RequestBody List<Long> seatInstanceIds);

}
