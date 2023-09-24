package com.klimovich.receipt.event;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptCreatedEvent {
    private Long id;
    private BigDecimal value = BigDecimal.ZERO;

}
