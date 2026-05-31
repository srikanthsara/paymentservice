package com.payment.service;

import com.common.event.OrderCreatedEvent;
import com.common.event.PaymentFailedEvent;
import com.common.event.PaymentSuccessEvent;
import com.payment.entity.PaymentTransaction;
import com.payment.kafka.PaymentEventProducer;
import com.payment.repository.PaymentTransactionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentProcessingService {

    private final PaymentTransactionRepository repository;

    private final PaymentEventProducer producer;

    public void processPayment(
            OrderCreatedEvent event) {

        boolean paymentSuccess =
                mockPaymentGateway(event);

        if (paymentSuccess) {

            String txnId =
                    UUID.randomUUID().toString();

            PaymentTransaction payment =

                    PaymentTransaction.builder()

                            .orderId(event.getOrderId())

                            .customerId(event.getCustomerId())

                            .amount(event.getTotalAmount())

                            .paymentType("UPI")

                            .paymentProvider("GooglePay")

                            .paymentStatus("SUCCESS")

                            .transactionId(txnId)

                            .createdAt(LocalDateTime.now())

                            .build();

            repository.save(payment);

            producer.publishSuccess(

                    PaymentSuccessEvent.builder()

                            .orderId(event.getOrderId())

                            .customerId(event.getCustomerId())

                            .amount(event.getTotalAmount())

                            .paymentStatus("SUCCESS")

                            .transactionId(txnId)

                            .build());

            System.out.println(
                    "Payment processed successfully");

        } else {

            PaymentTransaction payment =

                    PaymentTransaction.builder()

                            .orderId(event.getOrderId())

                            .customerId(event.getCustomerId())

                            .amount(event.getTotalAmount())

                            .paymentStatus("FAILED")

                            .failureReason(
                                    "Payment Gateway Failed")

                            .createdAt(LocalDateTime.now())

                            .build();

            repository.save(payment);

            producer.publishFailure(

                    PaymentFailedEvent.builder()

                            .orderId(event.getOrderId())

                            .customerId(event.getCustomerId())

                            .amount(event.getTotalAmount())

                            .paymentStatus("FAILED")

                            .reason("Payment Gateway Failed")

                            .build());
        }
    }

    // MOCK GATEWAY
    private boolean mockPaymentGateway(
            OrderCreatedEvent event) {

        return event.getTotalAmount()
                .compareTo(BigDecimal.valueOf(10000)) < 0;
    }
}