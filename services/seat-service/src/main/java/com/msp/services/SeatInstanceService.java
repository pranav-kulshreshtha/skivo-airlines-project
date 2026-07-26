package com.msp.services;

import java.util.List;

public interface SeatInstanceService {
    Double calculateSeatPrice(List<Long> seatInstanceIds);
}
