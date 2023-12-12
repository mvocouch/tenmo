package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.model.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class TransferServiceImpl implements TransferService{
    private final AccountDao accountDao;
    private final TransferDao transferDao;
    private final AccountService accountService;

    public TransferServiceImpl(AccountDao accountDao, TransferDao transferDao, AccountService accountService){
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.accountService = accountService;
    }

    private Transfer createTransferFromDto(User loggedInUser, TransferDto transferDto) {
        Transfer transfer = new Transfer();
        Account senderAccount = accountDao.getAccountByUserId(loggedInUser.getId());
        Account account = accountDao.getAccountByUserId(transferDto.getUserId());
        transfer.setTransferStatus(TransferStatus.PENDING);
        transfer.setTransferType(TransferType.SEND_MONEY);
        transfer.setAccountTo(account.getAccount_id());
        transfer.setAccountFrom(senderAccount.getAccount_id());
        transfer.setAmount(transferDto.getAmount());

        return transfer;
    }
    public Transfer initializeTransfer(User loggedInUser, TransferDto transferDto) {
        Transfer transfer = createTransferFromDto(loggedInUser, transferDto);
        //transferDao.addTransfer(transfer);
        accountService.transferFunds(transfer);

        return transfer;
    }
}
