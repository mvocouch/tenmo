package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;

public class TransferService extends AuthTokenService{
    public TransferService(String baseUrl) {
        this.baseUrl = baseUrl + "transfers/";
    }
    public Transfer createTransfer(Transfer transfer) {
        return transfer;
    }
}
