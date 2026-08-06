package com.msp.repositories;

import com.msp.models.SeatInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatInstanceRepository extends JpaRepository<SeatInstance, Long> {
}
