package com.msp.services.impl;

import com.msp.client.AirlineClient;
import com.msp.mappers.SeatMapMapper;
import com.msp.models.CabinClass;
import com.msp.models.SeatMap;
import com.msp.payloads.requests.SeatMapRequest;
import com.msp.payloads.responses.AirlineResponse;
import com.msp.payloads.responses.SeatMapResponse;
import com.msp.repositories.CabinClassRepository;
import com.msp.repositories.SeatMapRepository;
import com.msp.services.SeatMapService;
import com.msp.services.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SeatMapServiceImpl implements SeatMapService {

    private final CabinClassRepository cabinClassRepository;
    private final SeatMapRepository seatMapRepository;
    private final SeatService seatService;
    private final AirlineClient airlineClient;

    @Override
    public SeatMapResponse createSeatMap(Long userId, SeatMapRequest request) throws Exception {
        CabinClass cabinClass = cabinClassRepository.findById(request.getCabinClassId())
                .orElseThrow(() -> new Exception("Cabin class with given id not found!"));

        AirlineResponse airlineResponse = airlineClient.getAirlineByOwner(userId);

        if(seatMapRepository.existsByAirlineIdAndCabinClassIdAndName(
                airlineResponse.getId(), request.getCabinClassId(), request.getName()
        )) {
            throw new Exception("Cabin class already exists with given name!");
        }

        SeatMap seatMap = SeatMapMapper.toEntity(request, cabinClass);
        seatMap.setAirlineId(airlineResponse.getId());
        SeatMap saved = seatMapRepository.save(seatMap);

        // generate seats for seat map
        seatService.generateSeats(saved.getId());

        return SeatMapMapper.toResponse(saved);
    }

    @Override
    public SeatMapResponse getSeatMapById(Long id) throws Exception {
        SeatMap seatMap = seatMapRepository.findById(id)
                .orElseThrow(() -> new Exception("Seat map with given id not found!"));

        return SeatMapMapper.toResponse(seatMap);
    }

    @Override
    public SeatMapResponse getSeatMapByCabinClass(Long cabinClassId) {
        SeatMap seatMap = seatMapRepository.findByCabinClassId(cabinClassId);
        return SeatMapMapper.toResponse(seatMap);
    }

    @Override
    public SeatMapResponse updateSeatMap(Long id, SeatMapRequest request) throws Exception {
        SeatMap existing = seatMapRepository.findById(id)
                .orElseThrow(() -> new Exception("Seat map with given id not found!"));

        SeatMapMapper.updateEntity(request, existing);
        SeatMap updated = seatMapRepository.save(existing);

        return SeatMapMapper.toResponse(updated);
    }

    @Override
    public void deleteSeatMap(Long id) throws Exception {
        SeatMap seatMap = seatMapRepository.findById(id)
                .orElseThrow(() -> new Exception("Seat map with given id not found!"));
        seatMapRepository.delete(seatMap);
    }
}
