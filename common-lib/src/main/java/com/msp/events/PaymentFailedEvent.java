package com.msp.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentFailedEvent {
    private Long paymentId;
    private Long bookingId;
    private Long userId;
    private Double amount;
    private String transactionId;
    private String failureReason;
    private LocalDateTime failedAt;
}
