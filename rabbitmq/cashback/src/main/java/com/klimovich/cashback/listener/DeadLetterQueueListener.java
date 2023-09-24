package com.klimovich.cashback.listener;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import com.klimovich.receipt.event.ReceiptCreatedEvent;

@Component
public class DeadLetterQueueListener {
    private static final String DLQ = "receipts.v1.receipt-created.dlx.generate-cashback.dlq";

    private static final String X_RETRY_HEADER = " x-dql-retry";
    private final RabbitTemplate rabbitTemplate;

    public DeadLetterQueueListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @RabbitListener(queues = DLQ)
    public void process(ReceiptCreatedEvent receiptCreatedEvent, @Headers Map<String, Object> headers) {
        Integer retryHeader = (Integer) headers.get(X_RETRY_HEADER);
        if (retryHeader == null) {
            retryHeader = 0;
        }
        System.out.println("Repeat event ID: " + receiptCreatedEvent.getId());

        if (retryHeader < 3) {
            int tryCount = retryHeader + 1;
            Map<String, Object> updatedHeaders = new HashMap<>(headers);
            updatedHeaders.put(X_RETRY_HEADER, tryCount);

            final MessagePostProcessor messagePostProcessor = message -> {
                MessageProperties messagePostProperties = message.getMessageProperties();
                updatedHeaders.forEach(messagePostProperties::setHeader);
                return message;
            };
            System.out.println("Repeat event ID: " + receiptCreatedEvent.getId() + "for DLQ");
            this.rabbitTemplate.convertAndSend(DLQ, receiptCreatedEvent, messagePostProcessor);
        } else {
            this.rabbitTemplate.convertAndSend("receipts.v1.receipt-created.dlx.generate-cashback.dlq-parking-lot",
                    receiptCreatedEvent);

        }
    }
}
