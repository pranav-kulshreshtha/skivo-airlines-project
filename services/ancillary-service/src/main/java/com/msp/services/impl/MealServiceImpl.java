package com.msp.services.impl;

import com.msp.client.AirlineClient;
import com.msp.mapper.MealMapper;
import com.msp.models.Meal;
import com.msp.payloads.requests.MealRequest;
import com.msp.payloads.responses.AirlineResponse;
import com.msp.payloads.responses.MealResponse;
import com.msp.repositories.MealRepository;
import com.msp.services.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final AirlineClient airlineClient;

    @Override
    public MealResponse createMeal(Long userId, MealRequest request) throws Exception {

        AirlineResponse airlineResponse = airlineClient.getAirlineByOwner(userId);

        if(mealRepository.existsByCodeAndAirlineId(request.getCode(),
                airlineResponse.getId())) {
            throw new Exception("Meal code already exists!");
        }

        Meal meal = Meal.builder()
                .code(request.getCode())
                .name(request.getName())
                .mealType(request.getMealType())
                .dietaryRestrictions(request.getDietaryRestrictions())
                .ingredients(request.getIngredients())
                .imageUrl(request.getImageUrl())
                .requiresAdvanceBooking(request.getRequiresAdvanceBooking()!=null
                    ? request.getRequiresAdvanceBooking() : false)
                .advanceBookingHours(request.getAdvanceBookingHours())
                .displayOrder(request.getDisplayOrder())
                .airlineId(airlineResponse.getId())
                .build();

        Meal saved = mealRepository.save(meal);

        return MealMapper.toResponse(saved);
    }

    @Override
    public MealResponse getMealById(Long id) throws Exception {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new Exception("Meal with given id not found!"));

        return MealMapper.toResponse(meal);
    }

    @Override
    public List<MealResponse> getMealByAirlineId(Long userId) {

        AirlineResponse airlineResponse = airlineClient.getAirlineByOwner(userId);

        return mealRepository.findByAirlineId(airlineResponse.getId())
                .stream()
                .map(MealMapper::toResponse)
                .toList();
    }

    @Override
    public MealResponse updateMeal(Long userId, Long id, MealRequest request)
            throws Exception {
        Meal existing = mealRepository.findById(id)
                .orElseThrow(() -> new Exception("Meal with given id not found!"));
        AirlineResponse airlineResponse = airlineClient.getAirlineByOwner(userId);
        if(request.getCode()!=null && mealRepository.existsByAirlineIdAndCodeAndIdNot(
                airlineResponse.getId(),
                request.getCode(),
                existing.getId()
        )) {
            throw new Exception("Meal code already exists!");
        }

        if(request.getCode()!=null)existing.setCode(request.getCode());
        if(request.getName()!=null) existing.setName(request.getName());
        if(request.getMealType()!=null) existing.setMealType(request.getMealType());
        if(request.getDietaryRestrictions()!=null)
            existing.setDietaryRestrictions(request.getDietaryRestrictions());
        if(request.getIngredients()!=null)
            existing.setIngredients(request.getIngredients());
        if(request.getImageUrl()!=null) existing.setImageUrl(request.getImageUrl());
        if(request.getRequiresAdvanceBooking()!=null)
            existing.setRequiresAdvanceBooking(request.getRequiresAdvanceBooking());
        if(request.getAdvanceBookingHours()!=null)
            existing.setAdvanceBookingHours(request.getAdvanceBookingHours());
        if(request.getDisplayOrder()!=null)
            existing.setDisplayOrder(request.getDisplayOrder());

        Meal updated = mealRepository.save(existing);

        return MealMapper.toResponse(updated);
    }

    @Override
    public MealResponse updateAvailability(Long id, boolean availability) throws Exception {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new Exception("Meal with given id not found!"));
        meal.setIsAvailable(availability);
        Meal updated = mealRepository.save(meal);

        return MealMapper.toResponse(updated);
    }

    @Override
    public void deleteMeal(Long id) throws Exception {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new Exception("Meal with given id not found!"));
        mealRepository.delete(meal);
    }
}
