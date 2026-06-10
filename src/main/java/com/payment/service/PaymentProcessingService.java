package com.payment.service;

import com.common.event.OrderCreatedEvent;
import com.common.event.PaymentFailedEvent;
import com.common.event.PaymentSuccessEvent;
import com.payment.entity.PaymentTransaction;
import com.payment.kafka.PaymentEventProducer;
import com.payment.repository.PaymentTransactionRepository;
import com.payment.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PaymentProcessingService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PaymentProcessingService.class);
    private final PaymentTransactionRepository repository;
    private final PaymentEventProducer producer;

    public void processPayment(OrderCreatedEvent event) {

        if (Constants.PAYMENT_SUCCESS) {

            String txnId = UUID.randomUUID().toString();

            PaymentTransaction payment =
                    PaymentTransaction.builder()
                            .orderId(event.getOrderId())
                            .customerId(event.getCustomerId())
                            .amount(event.getTotalAmount())
                            .paymentType(event.getPaymentType())
                            .paymentProvider(event.getPaymentProvider())
                            .paymentStatus(Constants.PAYMENT_SUCCESS_STATUS)
                            .transactionId(txnId)
                            .createdAt(LocalDateTime.now())
                            .build();

            repository.save(payment);

            producer.publishSuccess(

                    PaymentSuccessEvent.builder()
                            .orderId(event.getOrderId())
                            .customerId(event.getCustomerId())
                            .amount(event.getTotalAmount())
                            .paymentStatus(Constants.PAYMENT_SUCCESS_STATUS)
                            .transactionId(txnId)
                            .build());

            LOGGER.info("Payment processed successfully");

        } else {

            PaymentTransaction payment =
                    PaymentTransaction.builder()
                            .orderId(event.getOrderId())
                            .customerId(event.getCustomerId())
                            .amount(event.getTotalAmount())
                            .paymentStatus(Constants.PAYMENT_FAILED_STATUS)
                            .failureReason(Constants.PAYMENT_GATEWAY_FAILED_REASON)
                            .createdAt(LocalDateTime.now())
                            .build();

            repository.save(payment);

            producer.publishFailure(
                    PaymentFailedEvent.builder()
                            .orderId(event.getOrderId())
                            .customerId(event.getCustomerId())
                            .amount(event.getTotalAmount())
                            .paymentStatus(Constants.PAYMENT_FAILED_STATUS)
                            .reason(Constants.PAYMENT_GATEWAY_FAILED_REASON)
                            .build());
        }
    }

}

