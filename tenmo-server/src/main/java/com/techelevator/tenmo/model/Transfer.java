package com.techelevator.tenmo.model;


import org.springframework.data.annotation.Id;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Transfer {
    @NotNull
    @Id
    private long id;
    @NotNull
    private int transferStatus;
    @NotNull
    private int transferType;
    @NotNull
    private int accountFrom;
    @NotNull
    private int accountTo;
    @NotNull
    @DecimalMin(value = ".01")
    @DecimalMax(value = "9999999999999.99")
    private BigDecimal amount;

    public Transfer(long id, int transfer_status, int accountFrom, int accountTo, BigDecimal amount) {
        this.id = id;
        this.transferStatus = transfer_status;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Transfer(int transfer_status, int accountFrom, int accountTo, BigDecimal amount) {
        this.transferStatus = transfer_status;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Transfer() {
    }

    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(int transferStatus) {
        this.transferStatus = transferStatus;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_DOWN);
    }


}
