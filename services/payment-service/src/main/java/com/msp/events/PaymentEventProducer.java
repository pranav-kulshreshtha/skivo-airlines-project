package com.msp.events;

import com.msp.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentCompleted(Payment payment) {
        PaymentCompletedEvent event = PaymentCompletedEvent.builder()
                .paymentId(payment.getId())
                .bookingId(payment.getBookingId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .providerPaymentId(payment.getProviderPaymentId())
                .paidAt(payment.getPaidAt())
                .build();

        kafkaTemplate.send("payment-completed", event);
    }

    public void sendPaymentFailed(Payment payment) {
        PaymentFailedEvent event = PaymentFailedEvent.builder()
                .paymentId(payment.getId())
                .bookingId(payment.getBookingId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .failureReason(payment.getFailureReason())
                .failedAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send("payment-failed", event);
    }

}
