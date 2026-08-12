package com.msp.services.impl;

import com.msp.mappers.AirportMapper;
import com.msp.models.Airport;
import com.msp.models.City;
import com.msp.payloads.requests.AirportRequest;
import com.msp.payloads.responses.AirportResponse;
import com.msp.repositories.AirportRepository;
import com.msp.repositories.CityRepository;
import com.msp.services.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;

    @Override
    public AirportResponse createAirport(AirportRequest request) throws Exception {
        if(airportRepository.findByIataCode(request.getIataCode()).isPresent()) {
            throw new Exception("Airport with IATA code already exists!");
        }

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new Exception("City does not exist!"));
        Airport airport = AirportMapper.toEntity(request);
        airport.setCity(city);

        Airport savedAirport = airportRepository.save(airport);

        return AirportMapper.toResponse(savedAirport);
    }

    @Override
    @Cacheable(cacheNames = "airports", key = "#id")
    public AirportResponse getAirportById(Long id) throws Exception {

        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new Exception("Airport does not exist with the given id!"));

        return AirportMapper.toResponse(airport);
    }

    @Override
    @Cacheable(cacheNames = "allAirports")
    public List<AirportResponse> getAllAirports() {

        return airportRepository.findAll()
                .stream().map(AirportMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "airports", key = "#id"),
            @CacheEvict(cacheNames = "allAirports", allEntries = true),
            @CacheEvict(cacheNames = "airportsByCity", allEntries = true)
    })
    public AirportResponse updateAirport(Long id, AirportRequest request) throws Exception {

        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new Exception("Airport does not exist with the given id!"));

        if(request.getIataCode() != null
                && !airport.getIataCode().equals(request.getIataCode())
                && airportRepository.findByIataCode(request.getIataCode()).isPresent()) {
            throw new Exception("Airport with the given IATA code already exists!");
        }

        Airport updatedAirport = AirportMapper.updateEntity(airport, request);
        Airport savedAirport = airportRepository.save(updatedAirport);

        return AirportMapper.toResponse(savedAirport);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "airports", key = "#id"),
            @CacheEvict(cacheNames = "allAirports", allEntries = true),
            @CacheEvict(cacheNames = "airportsByCity", allEntries = true)
    })
    public void deleteAirport(Long id) throws Exception {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new Exception("No airport exists with the given id!"));

        airportRepository.delete(airport);
    }

    @Override
    @Cacheable(cacheNames = "airportsByCity", key = "#cityId")
    public List<AirportResponse> getAirportByCityId(Long cityId) {

        return airportRepository.findByCityId(cityId)
                .stream().map(AirportMapper::toResponse)
                .collect(Collectors.toList());    }
}
