package com.msp.services;

import com.msp.payloads.requests.MealRequest;
import com.msp.payloads.responses.MealResponse;
import java.util.List;

public interface MealService {

    MealResponse createMeal(Long userId, MealRequest request) throws Exception;

    MealResponse getMealById(Long id) throws Exception;

    List<MealResponse> getMealByAirlineId(Long userId);

    MealResponse updateMeal(Long userId, Long id, MealRequest request)
            throws Exception;

    MealResponse updateAvailability(Long id, boolean availability) throws Exception;

    void deleteMeal(Long id) throws Exception;

}
