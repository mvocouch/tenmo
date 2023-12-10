package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> findAll();
    List<Transfer> getTransfersForUser(int userId);
    Transfer addTransfer(Transfer newtransfer);


    Transfer getTransferById(long transferId);
}
