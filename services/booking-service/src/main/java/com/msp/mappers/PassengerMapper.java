package com.msp.mappers;

import com.msp.models.Passenger;
import com.msp.payloads.requests.PassengerRequest;
import com.msp.payloads.responses.PassengerResponse;

public class PassengerMapper {

    public static Passenger toEntity(PassengerRequest request) {
        if(request == null) return null;

        return Passenger.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .nationality(request.getNationality())
                .build();
    }

    public static PassengerResponse toResponse(Passenger passenger) {
        if(passenger == null) return null;

        return PassengerResponse.builder()
                .id(passenger.getId())
                .firstName(passenger.getFirstName())
                .lastName(passenger.getLastName())
                .email(passenger.getEmail())
                .phone(passenger.getPhone())
                .dateOfBirth(passenger.getDateOfBirth())
                .gender(passenger.getGender())
                .nationality(passenger.getNationality())
                .primaryUserId(passenger.getPrimaryUserId())
                .isActive(passenger.getIsActive())
                .age(passenger.getAge())
                .isAdult(passenger.isAdult())
                .fullName(passenger.getFullName())
                .createdAt(passenger.getCreatedAt())
                .updatedAt(passenger.getUpdatedAt())
                .build();
    }

    public static void updateEntity(Passenger passenger, PassengerRequest request) {
        if(passenger == null || request == null) return;

        if(request.getFirstName() != null) passenger.setFirstName(request.getFirstName());
        if(request.getLastName() != null) passenger.setLastName(request.getLastName());
        if(request.getEmail() != null) passenger.setEmail(request.getEmail());
        if(request.getPhone() != null) passenger.setPhone(request.getPhone());
        if(request.getDateOfBirth() != null) passenger.setDateOfBirth(request.getDateOfBirth());
        if(request.getGender() != null) passenger.setGender(request.getGender());
        if(request.getNationality() != null) passenger.setNationality(request.getNationality());
    }

    public static void updateEntityFromRequest(PassengerRequest request, Passenger passenger) {
        passenger.setFirstName(request.getFirstName());
        passenger.setLastName(request.getLastName());
        passenger.setEmail(request.getEmail());
        passenger.setPhone(request.getPhone());
        passenger.setDateOfBirth(request.getDateOfBirth());
        passenger.setGender(request.getGender());
        passenger.setPassportNumber(request.getPassportNumber());
        passenger.setNationality(request.getNationality());
        passenger.setFrequentFlyerNumber(request.getFrequentFlyerNumber());
        passenger.setRequiresWheelchairAssistance(request.getRequiresWheelchairAssistance());
        passenger.setDietaryPreferences(request.getDietaryPreferences());
        passenger.setMedicalConditions(request.getMedicalConditions());
    }

}
