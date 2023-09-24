package com.klimovich.notification.event;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReceiptCreatedEvent {
    private Long id;
    private BigDecimal value = BigDecimal.ZERO;
}
