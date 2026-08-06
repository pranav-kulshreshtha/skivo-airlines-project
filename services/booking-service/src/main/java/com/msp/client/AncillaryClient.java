package com.msp.client;

import com.msp.payloads.responses.FlightCabinAncillaryResponse;
import com.msp.payloads.responses.FlightMealResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ancillary-service")
public interface AncillaryClient {

    @PostMapping("/api/flight-cabin-ancillaries/price/total")
    Double calculateAncillaryPrice(@RequestBody List<Long> ancillaryIds);

    @PostMapping("/api/flight-meals/price/total")
     Double calculateMealPrice(@RequestBody List<Long> mealIds);

    @PostMapping("/api/flight-cabin-ancillaries/ids")
    List<FlightCabinAncillaryResponse> getAllByIds(@RequestBody List<Long> Ids);

    @PostMapping("/api/flight-meals/all")
    List<FlightMealResponse> getMealsByIds(@RequestBody List<Long> Ids);

}
