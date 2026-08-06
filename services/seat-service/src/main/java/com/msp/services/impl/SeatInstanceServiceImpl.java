package com.msp.services.impl;

import com.msp.enums.SeatAvailabilityStatus;
import com.msp.mappers.SeatInstanceMapper;
import com.msp.models.SeatInstance;
import com.msp.payloads.responses.SeatInstanceResponse;
import com.msp.repositories.SeatInstanceRepository;
import com.msp.services.SeatInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SeatInstanceServiceImpl implements SeatInstanceService {

    private final SeatInstanceRepository seatInstanceRepository;

    @Override
    public Double calculateSeatPrice(List<Long> seatInstanceIds) {
        List<SeatInstance> seatInstances = seatInstanceRepository
                .findAllById(seatInstanceIds);
        double price = 0.0;
        for(SeatInstance si : seatInstances) {
            double seatPremium = si.getPremiumSupercharge()!=null
                    ? si.getPremiumSupercharge() : 0.0;
            price += seatPremium;
        }

        return price;
    }

    @Override
    public SeatInstanceResponse updateSeatInstanceStatus(Long seatInstanceId, SeatAvailabilityStatus status) {
        SeatInstance seatInstance = seatInstanceRepository.findById(seatInstanceId)
                .orElse(null);
        if(seatInstance == null)return null;

        seatInstance.setSeatAvailabilityStatus(status);
        seatInstanceRepository.save(seatInstance);

        return SeatInstanceMapper.toResponse(seatInstance);
    }

    @Override
    public List<SeatInstanceResponse> getAllByIds(List<Long> ids) {
        return seatInstanceRepository.findAllById(ids)
                .stream()
                .map(SeatInstanceMapper::toResponse)
                .toList();
    }
}
