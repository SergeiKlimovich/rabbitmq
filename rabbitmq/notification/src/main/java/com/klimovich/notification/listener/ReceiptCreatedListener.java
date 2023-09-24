package com.klimovich.notification.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.klimovich.receipt.event.ReceiptCreatedEvent;


@Component
public class ReceiptCreatedListener {
    @RabbitListener(queues = "receipts.v1.receipt-created.send-notification")
    public void onReceiptCreated(ReceiptCreatedEvent event) {
        System.out.println("Id: " + event.getId());
        System.out.println("Value: " + event.getValue());
    }
}
