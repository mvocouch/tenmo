package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransferServiceImpl implements TransferService{
    private final AccountDao accountDao;

    public TransferServiceImpl(AccountDao accountDao){
        this.accountDao = accountDao;
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

        return transfer;
    }
}
