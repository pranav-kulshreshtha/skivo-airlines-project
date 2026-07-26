package com.msp.services.impl;

import com.msp.mappers.PassengerMapper;
import com.msp.models.Passenger;
import com.msp.payloads.requests.PassengerRequest;
import com.msp.repositories.PassengerRepository;
import com.msp.services.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;

    @Override
    public Passenger createPassenger(PassengerRequest request, Long userId) {
        Passenger passenger = PassengerMapper.toEntity(request);
        passenger.setPrimaryUserId(userId);
        Passenger saved = passengerRepository.save(passenger);
        return saved;
    }

}
