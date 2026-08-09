package com.msp.services;

import com.msp.payloads.requests.FlightSearchRequest;
import com.msp.payloads.responses.FlightInstanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FlightSearchService {
    Page<FlightInstanceResponse> searchFlights(FlightSearchRequest request, Pageable pageable);
}
