package com.payment.kafka;

import com.common.event.OrderCreatedEvent;
import com.payment.service.PaymentProcessingService;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderCreatedConsumer {

    private final PaymentProcessingService   paymentService;

    @KafkaListener(
            topics = "order-created-topic",
            groupId = "payment-group"
    )
    public void consume(OrderCreatedEvent event) {
        System.out.println(
                "Consumed ORDER_CREATED event: "
                        + event);
        paymentService.processPayment(event);
    }
}
