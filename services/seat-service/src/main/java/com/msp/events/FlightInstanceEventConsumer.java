package com.msp.events;

import com.msp.enums.SeatAvailabilityStatus;
import com.msp.enums.SeatType;
import com.msp.models.CabinClass;
import com.msp.models.FlightInstanceCabin;
import com.msp.models.Seat;
import com.msp.models.SeatInstance;
import com.msp.repositories.CabinClassRepository;
import com.msp.repositories.FlightInstanceCabinRepository;
import com.msp.repositories.SeatInstanceRepository;
import com.msp.repositories.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FlightInstanceEventConsumer {

    private final CabinClassRepository cabinClassRepository;
    private final SeatRepository seatRepository;
    private final SeatInstanceRepository seatInstanceRepository;
    private final FlightInstanceCabinRepository flightInstanceCabinRepository;

    @Transactional
    @KafkaListener(topics = "flight-instance-created", groupId = "seat-service-group")
    public void handleFlightInstanceCreated(FlightInstanceCreatedEvent event) {

        List<CabinClass> cabinClasses = cabinClassRepository.findByAircraftId(event.getAircraftId());

        int totalSeatInstances = 0;

        for(CabinClass cabinClass : cabinClasses) {
            List<Seat> seats = cabinClass.getSeatMap() != null
                    ? seatRepository.findBySeatMapId(cabinClass.getSeatMap().getId())
                    : List.of();
            FlightInstanceCabin fic = FlightInstanceCabin.builder()
                    .flightInstanceId(event.getFlightInstanceId())
                    .cabinClass(cabinClass)
                    .totalSeats(seats.size())
                    .bookedSeats(0)
                    .build();
            FlightInstanceCabin saved = flightInstanceCabinRepository.save(fic);

            List<SeatInstance> seatInstances = seats.stream().map(
                    seat ->
                        SeatInstance.builder()
                                .flightId(event.getFlightId())
                                .flightInstanceId(event.getFlightInstanceId())
                                .flightInstanceCabin(saved)
                                .seat(seat)
                                .seatAvailabilityStatus(SeatAvailabilityStatus.AVAILABLE)
                                .isBooked(false)
                                .isAvailable(true)
                                .premiumSupercharge(getPremiumSupercharge(seat.getSeatType(),
                                        1000.0, 500.0))
                                .build()
                    ).toList();
            seatInstanceRepository.saveAll(seatInstances);
            totalSeatInstances += seatInstances.size();
        }


    }

    private Double getPremiumSupercharge(
            SeatType seatType,
            Double windowSupercharge,
            Double aisleSupercharge
    ) {
        if(seatType==null)return 0.0;

        return switch(seatType) {
            case AISLE -> aisleSupercharge;
            case WINDOW -> windowSupercharge;
            default -> 0.0;
        };
    }

}
