package com.msp.services.integration;

import com.msp.client.PricingClient;
import com.msp.payloads.responses.FareResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FareIntegrationService {

    private final PricingClient pricingClient;

    public Double calculateFareTotal(Long fareId) {
        FareResponse fareResponse = pricingClient.getFareById(fareId);
        Double baseFare = fareResponse.getBaseFare();
        Double taxesAndFees = fareResponse.getTaxesAndFees() != null
                ? fareResponse.getTaxesAndFees() : 0.0;
        Double airlineFees = fareResponse.getAirlineFees() != null
                ? fareResponse.getAirlineFees() : 0.0;

        return baseFare + taxesAndFees + airlineFees;
    }

}

