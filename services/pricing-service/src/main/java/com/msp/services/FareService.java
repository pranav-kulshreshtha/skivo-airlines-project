package com.msp.services;

import com.msp.models.Fare;
import com.msp.payloads.requests.FareRequest;
import com.msp.payloads.responses.FareResponse;

import java.util.List;
import java.util.Map;

public interface FareService {

    FareResponse createFare(FareRequest fareRequest) throws Exception;
    FareResponse getFareById(Long id) throws Exception;
    List<FareResponse> getFaresByFlightIdAndCabinClassId(
            Long flightId, Long cabinClassId
    );
    FareResponse updateFare(Long id, FareRequest fareRequest) throws Exception;
    List<Fare> getAllFares();
    void deleteFare(Long id) throws Exception;

    Map<Long, FareResponse> getLowestFarePerFlight(
            List<Long> flightIds, Long cabinClassId);
    FareResponse getLowestFareForFlightAndCabin(Long flightId, Long cabinClassId);
    Map<Long, FareResponse> getFaresByIds(List<Long> ids);
}
