package com.msp.repositories;

import com.msp.models.FlightInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

public interface FlightInstanceRepository extends JpaRepository<FlightInstance, Long>,
        JpaSpecificationExecutor<FlightInstance> {
    @Query("""
select fi from FlightInstance fi
where fi.airlineId = :airlineId
and (:depAirportId is null or fi.departureAirportId = :depAirportId)
and (:arrAirportId is null or fi.departureAirportId = :arrAirportId)
and (:flightId is null or fi.flight.id = :flightId)
and (:dayStart is null or fi.departureDateTime >= :dayStart)
and (:dayEnd is null or fi.arrivalDateTime >= :dayEnd)

""")
    Page<FlightInstance> findByAirlineId(@Param("airlineId") Long airlineId,
                                         @Param("depAirportId") Long departureAirportId,
                                         @Param("arrAirportId") Long arrivalAirportId,
                                         @Param("flightId") Long flightId,
                                         @Param("dayStart") LocalDateTime dayStart,
                                         @Param("dayEnd") LocalDateTime dayEnd,
                                         Pageable pageable);
}
