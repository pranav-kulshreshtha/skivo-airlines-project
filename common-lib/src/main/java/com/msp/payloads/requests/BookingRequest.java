package com.msp.payloads.requests;

import com.msp.embaddables.ContactInfo;
import com.msp.enums.CabinClassType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {

    @NotNull(message = "Flight ID is required!")
    private Long flightId;

    @NotNull(message = "Flight instance ID is required!")
    private Long flightInstanceId;

    @NotNull(message = "Cabin class is required!")
    private CabinClassType cabinClass;

    @NotNull(message = "Fare ID is required!")
    private Long fareId;

    @NotNull(message = "At least one passenger is required!")
    @Size(min = 1, message = "At least one passenger is required!")
    private List<PassengerRequest> passengers;

    private ContactInfo contactInfo;

    private List<Long> ancillaryIds;

    private List<Long> mealIds;

    private List<String> seatNumbers;
}
