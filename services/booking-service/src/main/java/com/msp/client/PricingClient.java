package com.msp.client;

import com.msp.payloads.responses.FareResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pricing-service")
public interface PricingClient {

    @GetMapping("/api/fares/{id}")
    FareResponse getFareById(@PathVariable Long id);

}
