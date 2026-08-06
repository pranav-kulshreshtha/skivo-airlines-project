package com.msp.services.impl;

import com.msp.mappers.PassengerMapper;
import com.msp.models.Passenger;
import com.msp.payloads.requests.PassengerRequest;
import com.msp.repositories.PassengerRepository;
import com.msp.services.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Override
    @Transactional
    public Passenger findOrCreatePassengerEntity(
            PassengerRequest request, Long userId) {
        Optional<Passenger> existing = findExistingPassengerOptional(request);
        if (existing.isPresent()) {
            Passenger passenger = existing.get();
            PassengerMapper.updateEntityFromRequest(request, passenger);
            return passengerRepository.save(passenger);
        }

        Passenger newPassenger = PassengerMapper.toEntity(request);
        newPassenger.setPrimaryUserId(userId);
        return passengerRepository.save(newPassenger);
    }

    @Override
    public Passenger findExistingPassenger(PassengerRequest request) {
        return findExistingPassengerOptional(request).orElse(null);
    }

    @Override
    public boolean existsById(Long id) {
        return passengerRepository.existsById(id);
    }

    @Override
    public long count() {
        return passengerRepository.count();
    }

    private Optional<Passenger> findExistingPassengerOptional(PassengerRequest request) {
        if (request.getPassportNumber() != null && !request.getPassportNumber().isEmpty()) {
            Optional<Passenger> byPassport = passengerRepository.findByPassportNumber(
                    request.getPassportNumber());
            if (byPassport.isPresent()) {
                return byPassport;
            }
        }

        return passengerRepository.findByEmailAndPhoneAndDateOfBirth(
                request.getEmail(), request.getPhone(), request.getDateOfBirth());
    }

}
