package com.payment.kafka;

import com.common.event.PaymentFailedEvent;
import com.common.event.PaymentSuccessEvent;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object>
            kafkaTemplate;

    public void publishSuccess(
            PaymentSuccessEvent event) {

        kafkaTemplate.send(
                "payment-success-topic",
                event);

        System.out.println(
                "PAYMENT_SUCCESS published");
    }

    public void publishFailure(
            PaymentFailedEvent event) {

        kafkaTemplate.send(
                "payment-failed-topic",
                event);

        System.out.println(
                "PAYMENT_FAILED published");
    }
}
