package com.msp.services.impl;

import com.msp.mappers.FareMapper;
import com.msp.models.Fare;
import com.msp.payloads.requests.FareRequest;
import com.msp.payloads.responses.FareResponse;
import com.msp.repositories.FareRepository;
import com.msp.services.FareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FareServiceImpl implements FareService {
    private final FareRepository fareRepository;

    @Override
    public FareResponse createFare(FareRequest fareRequest) throws Exception {
        if(fareRepository.existsByFlightIdAndCabinClassIdAndName(
                fareRequest.getFlightId(),
                fareRequest.getCabinClassId(),
                fareRequest.getName()
        )) {
            throw new Exception("Fare with provided name already exists!");
        }

        Fare fare = FareMapper.toEntity(fareRequest);
        Fare saved = fareRepository.save(fare);

        return FareMapper.toResponse(saved);
    }

    @Override
    public FareResponse getFareById(Long id) throws Exception {
        Fare fare = fareRepository.findById(id)
                .orElseThrow(() -> new Exception("Fare not found with given id!"));
        return FareMapper.toResponse(fare);
    }

    @Override
    public List<FareResponse> getFaresByFlightIdAndCabinClassId(Long flightId, Long cabinClassId) {

        return fareRepository.findByFlightIdAndCabinClassId(flightId, cabinClassId)
                .stream()
                .map(FareMapper::toResponse)
                .toList();
    }

    @Override
    public FareResponse updateFare(Long id, FareRequest fareRequest) throws Exception {
        Fare existing = fareRepository.findById(id)
                .orElseThrow(() -> new Exception("Fare not found with given id!"));

        if(fareRepository.existsByFlightIdAndCabinClassIdAndNameAndIdNot(
                fareRequest.getFlightId(), fareRequest.getCabinClassId(),
                fareRequest.getName(), existing.getId()
        ));

        FareMapper.updateEntity(fareRequest, existing);
        Fare updated = fareRepository.save(existing);
        return FareMapper.toResponse(updated);
    }

    @Override
    public List<Fare> getAllFares() {
        return fareRepository.findAll();
    }

    @Override
    public void deleteFare(Long id) throws Exception {
        Fare fare = fareRepository.findById(id)
                .orElseThrow(() -> new Exception("Fare not found with given id!"));

        fareRepository.delete(fare);
    }

    @Override
    public Map<Long, FareResponse> getLowestFarePerFlight(List<Long> flightIds, Long cabinClassId) {
        if(flightIds == null || flightIds.isEmpty())return Map.of();

        List<Fare> fares = fareRepository.findByFlightIdInAndCabinClassId(
                flightIds, cabinClassId
        );

        Map<Long, FareResponse> result = fares.stream()
                .collect(Collectors.toMap(
                        Fare::getFlightId,
                        fare -> fare,
                        (existing, candidate) -> candidate.totalPrice()
                            < existing.totalPrice() ? candidate : existing
                        )).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> FareMapper.toResponse(e.getValue())
                ));

        return result;
    }

    @Override
    public FareResponse getLowestFareForFlightAndCabin(Long flightId, Long cabinClassId) {
        List<Fare> fares = fareRepository.findByFlightIdAndCabinClassId(
                flightId, cabinClassId);
        Fare lowestFare = fares
                .stream()
                .min(Comparator.comparingDouble(Fare::totalPrice))
                .orElse(null);
        return FareMapper.toResponse(lowestFare);
    }

    @Override
    public Map<Long, FareResponse> getFaresByIds(List<Long> ids) {
        List<Fare> fares = fareRepository.findAllById(ids);
        return fares.stream().collect(Collectors.toMap(Fare::getId, FareMapper::toResponse));
    }
}
