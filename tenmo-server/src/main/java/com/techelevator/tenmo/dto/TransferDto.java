package com.techelevator.tenmo.dto;

import java.math.BigDecimal;

public class TransferDto {
    // user Id depends on who is receiving learned from type
    private final int userId;
    private final BigDecimal amount;
    private final String type;

    public TransferDto(int userId, BigDecimal amount, String type) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }
}
