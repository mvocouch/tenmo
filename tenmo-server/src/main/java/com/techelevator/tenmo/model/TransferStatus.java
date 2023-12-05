package com.techelevator.tenmo.model;

public class TransferStatus {
    private int transfer_Status_id;

    public TransferStatus(int transfer_type_id) {
        this.transfer_Status_id = transfer_type_id;
    }

    public int getTransfer_type_id() {
        return transfer_Status_id;
    }

    public void setTransfer_type_id(int transfer_type_id) {
        this.transfer_Status_id = transfer_type_id;
    }
}
