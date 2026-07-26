package com.msp.models;

import com.msp.embaddables.ContactInfo;
import com.msp.enums.BookingStatus;
import com.msp.enums.CabinClassType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String bookingReference;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long flightId;

    @Column(nullable = false)
    private Long flightInstanceId;

    @Column(nullable = false)
    private Long airlineId;

    @Enumerated(EnumType.STRING)
    private CabinClassType cabinClassType = CabinClassType.ECONOMY;

    @Column(nullable = false)
    private Long fareId;

    private Boolean flexibleTicket;

    private LocalDateTime ticketTimeLimit;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Passenger> passengers = new HashSet<>();

    @ElementCollection
    private List<Long> seatInstanceIds;

    @ElementCollection
    private List<Long> ancillaryIds;

    @ElementCollection
    private List<Long> mealIds;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ticket> tickets = new HashSet<>();

    private Long paymentId;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @CreatedDate
    private Instant bookingDate;

    @UpdateTimestamp
    private Instant lastModified;

    private Boolean ticketIssued;

    private ContactInfo contactInfo;
}
