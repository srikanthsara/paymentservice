package com.payment.kafka;

import com.common.event.OrderCreatedEvent;
import com.payment.service.PaymentProcessingService;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderCreatedConsumer {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(OrderCreatedConsumer.class);

    private final PaymentProcessingService   paymentService;

    @KafkaListener(
            topics = "order-created-topic",
            groupId = "payment-group"
    )
    public void consume(OrderCreatedEvent event) {
        LOGGER.info(
                "Consumed ORDER_CREATED event: "
                        + event);
        paymentService.processPayment(event);
    }
}
