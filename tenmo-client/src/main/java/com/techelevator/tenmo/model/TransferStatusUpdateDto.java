package com.techelevator.tenmo.model;

public class TransferStatusUpdateDto {
    private int transferStatusId;
    public TransferStatusUpdateDto(int transferStatusId) {
        if (transferStatusId == 1 || transferStatusId == 2 || transferStatusId == 3) {
            this.transferStatusId = transferStatusId;
        } else {
            throw new IllegalArgumentException("Invalid transferStatus: " + transferStatusId);
        }
    }
}
