package com.msp.services;

import com.msp.enums.SeatAvailabilityStatus;
import com.msp.payloads.responses.SeatInstanceResponse;

import java.util.List;

public interface SeatInstanceService {

    Double calculateSeatPrice(List<Long> seatInstanceIds);

    SeatInstanceResponse updateSeatInstanceStatus(
            Long seatInstanceId, SeatAvailabilityStatus status);

    List<SeatInstanceResponse> getAllByIds(List<Long> ids);
}
