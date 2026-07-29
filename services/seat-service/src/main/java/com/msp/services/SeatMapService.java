package com.msp.services;

import com.msp.payloads.requests.SeatMapRequest;
import com.msp.payloads.responses.SeatMapResponse;

public interface SeatMapService {
    SeatMapResponse createSeatMap(Long userId, SeatMapRequest request) throws Exception;
    SeatMapResponse getSeatMapById(Long id) throws Exception;
    SeatMapResponse getSeatMapByCabinClass(Long cabinClassId);
    SeatMapResponse updateSeatMap(Long id, SeatMapRequest request) throws Exception;
    void deleteSeatMap(Long id) throws Exception;
}
