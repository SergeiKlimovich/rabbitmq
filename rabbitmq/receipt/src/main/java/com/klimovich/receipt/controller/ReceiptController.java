package com.klimovich.receipt.controller;

import java.util.Collection;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.klimovich.receipt.model.Receipt;
import com.klimovich.receipt.event.ReceiptCreatedEvent;
import com.klimovich.receipt.repository.ReceiptRepository;

@RestController
@RequestMapping(value = "/v1/receipts")
public class ReceiptController {


    @Autowired
    private ReceiptRepository receipts;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    public Receipt create(@RequestBody Receipt receipt) {
        receipts.save(receipt);

        ReceiptCreatedEvent event = new ReceiptCreatedEvent(receipt.getId(), receipt.getValue());
        rabbitTemplate.convertAndSend("receipts.v1.receipt-created", "", event);

        return receipt;
    }

    @GetMapping
    public Collection<Receipt> list() {
        return receipts.findAll();
    }

    @GetMapping("{id}")
    public Receipt findById(@PathVariable Long id) {
        return receipts.findById(id).orElseThrow();
    }

    @PutMapping("{id}/pay")
    public Receipt pay(@PathVariable Long id) {
        Receipt receipt = receipts.findById(id).orElseThrow();
        receipt.markAsPaid();
        return receipts.save(receipt);
    }
}
