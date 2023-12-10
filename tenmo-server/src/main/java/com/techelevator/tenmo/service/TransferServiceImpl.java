package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Component;

@Component
public class TransferServiceImpl implements TransferService{
    private final AccountDao accountDao;
    private final TransferDao transferDao;

    public TransferServiceImpl(AccountDao accountDao, TransferDao transferDao){
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    @Override
    public Transfer createTransferFromDto(User loggedInUser, TransferDto transferDto) {
        Transfer transfer = new Transfer();
        int defaultStatusPending = 1;

        Account senderAccount = accountDao.getAccountByUserId(loggedInUser.getId());
        Account account = accountDao.getAccountByUserId(transferDto.getUserId());
        transfer.setTransferStatus(defaultStatusPending);
        transfer.setAccountTo(account.getAccount_id());
        transfer.setAccountFrom(senderAccount.getAccount_id());
        transfer.setAmount(transferDto.getAmount());

        transferDao.addTransfer(transfer);

        return transfer;
    }
}
