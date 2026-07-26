package com.msp.services.impl;

import com.msp.models.SeatInstance;
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
}
