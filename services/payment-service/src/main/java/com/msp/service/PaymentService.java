package com.msp.service;

import com.msp.exceptions.PaymentException;
import com.msp.payloads.requests.PaymentInitiateRequest;
import com.msp.payloads.requests.PaymentVerifyRequest;
import com.msp.payloads.responses.PaymentDTO;
import com.msp.payloads.responses.PaymentInitiateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PaymentService {

    PaymentInitiateResponse initiatePayment(PaymentInitiateRequest request) throws PaymentException;

    PaymentDTO verifyPayment(PaymentVerifyRequest request) throws PaymentException;


    Page<PaymentDTO> getAllPayments(Pageable pageable);



    Map<Long, PaymentDTO> getPaymentsByBookingIds(List<Long> bookingIds);
}
