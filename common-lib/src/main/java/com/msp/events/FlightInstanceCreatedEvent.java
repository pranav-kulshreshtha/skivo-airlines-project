package com.msp.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightInstanceCreatedEvent {
    private Long flightInstanceId;
    private Long aircraftId;
    private Long flightId;
}
