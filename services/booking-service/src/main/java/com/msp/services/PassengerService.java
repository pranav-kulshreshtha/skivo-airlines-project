package com.msp.services;

import com.msp.models.Passenger;
import com.msp.payloads.requests.PassengerRequest;
import com.msp.payloads.responses.PassengerResponse;

public interface PassengerService {
    Passenger createPassenger(PassengerRequest request, Long userId);
}
