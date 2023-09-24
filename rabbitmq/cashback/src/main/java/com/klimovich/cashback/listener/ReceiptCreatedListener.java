package com.klimovich.cashback.listener;

import java.math.BigDecimal;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.klimovich.receipt.event.ReceiptCreatedEvent;

@Component
public class ReceiptCreatedListener {
    @RabbitListener(queues = "receipts.v1.receipt-created.generate-cashback")
    public void onReceiptCreated(ReceiptCreatedEvent receiptEvent) {
        System.out.println("Id: " + receiptEvent.getId());
        System.out.println("Value: " + receiptEvent.getValue());

        if (receiptEvent.getValue().compareTo(new BigDecimal(10000)) >= 0) {
            throw new RuntimeException("Sending fail for receipt: " + receiptEvent.getId());
        }
    }
}
