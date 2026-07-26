package com.msp.models;

import com.msp.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ticketNumber;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private LocalDateTime issuedAt;

    @ManyToOne
    private Booking booking;

    @ManyToOne
    private Passenger passenger;
}
