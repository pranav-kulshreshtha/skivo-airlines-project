package com.msp.client;

import com.msp.payloads.responses.FlightResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "flight-ops-service")
public interface FlightClient {

    @GetMapping("/api/flights/{id}")
    FlightResponse getFlightsById(@PathVariable Long id);

}

