package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

public interface TransferService {
    Transfer createTransferFromDto(User loggedInUser, TransferDto transferDto);
}
