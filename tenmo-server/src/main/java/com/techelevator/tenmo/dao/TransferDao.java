package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {


    List<Transfer> getTransfers(int accountId);

    Transfer getTransferById(int transferId);

    List<Transfer> getTransfersReceivedById(int accountTo);

    List<Transfer> getTransfersSentById(int accountFrom);
}
