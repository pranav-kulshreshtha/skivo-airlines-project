package com.msp.services.impl;

import com.msp.mappers.CityMapper;
import com.msp.models.City;
import com.msp.payloads.requests.CityRequest;
import com.msp.payloads.responses.CityResponse;
import com.msp.repositories.CityRepository;
import com.msp.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Override
    public CityResponse createCity(CityRequest request) throws Exception {
        if(cityRepository.existsByCityCode(request.getCityCode())) {
            throw new Exception("City with given code already exists!");
        }

        City city = CityMapper.toEntity(request);
        City result = cityRepository.save(city);
        return CityMapper.toResponse(result);
    }

    @Override
    @Cacheable(cacheNames = "cities", key = "#id")
    public CityResponse getCityById(Long id) throws Exception {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new Exception("City with the given id does not exist!"));
        return CityMapper.toResponse(city);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "cities", key = "#id"),
            @CacheEvict(cacheNames = "citiesByCode", allEntries = true)
    })
    public CityResponse updateCity(Long id, CityRequest request) throws Exception {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new Exception("City with the given id does not exist!"));

        if(cityRepository.existsByCityCodeAndIdNot(request.getCityCode(), id)){
            throw new Exception("City with the given code already exists");
        }

        City updatedCity = cityRepository.save(CityMapper.updateEntity(city, request));
        return CityMapper.toResponse(updatedCity);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "cities", key = "#id"),
            @CacheEvict(cacheNames = "citiesByCode", allEntries = true)
    })
    public void deleteCity(Long id) throws Exception {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new Exception("City with the given id does not exist!"));

        cityRepository.delete(city);
    }

    @Override
    public Page<CityResponse> getAllCities(Pageable pageable) {
        return cityRepository.findAll(pageable).map(CityMapper::toResponse);
    }

    @Override
    public Page<CityResponse> searchCities(String keyword, Pageable pageable) {

        return cityRepository.searchByKeyword(keyword, pageable).map(CityMapper::toResponse);
    }

    @Override
    public Page<CityResponse> getCitiesByCountryCode(String countryCode, Pageable pageable) {
        return cityRepository.findByCountryCodeIgnoreCase(countryCode, pageable)
                .map(CityMapper::toResponse);
    }

    @Override
    public boolean cityExists(String cityCode) {
        return cityRepository.existsByCityCode(cityCode);
    }
}
