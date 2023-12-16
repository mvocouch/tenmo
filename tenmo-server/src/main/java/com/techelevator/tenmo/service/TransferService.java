package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

public interface TransferService {
    Transfer initializeTransfer(User loggedInUser, TransferDto transferDto);
    Transfer acceptTransfer(User loggedInUser, Long transferId);
}

