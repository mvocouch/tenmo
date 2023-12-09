package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransferServiceImpl implements TransferService{
    private final AccountDao accountDao;

    public TransferServiceImpl(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        BigDecimal transferAmount = transfer.getAmount();
        int accountIdFrom = transfer.getAccountFrom();
        int accountIdTo = transfer.getAccountTo();

        Account accountFrom = accountDao.getAccountById(accountIdFrom);
        Account accountTo = accountDao.getAccountById(accountIdTo);

        if (accountFrom.equals(accountTo)){
            throw new DaoException("Funds cannot be transferred from and received by the same account.");
        }

        boolean fundsWithdrawn = accountFrom.subtractFromBalance(transferAmount);
        if (!fundsWithdrawn){
            throw new DaoException("The account #" + accountTo.getAccount_id() +
                    " has an insufficient balance to perform the requested withdrawal.");
        }

        boolean fundsReceived = accountTo.addToBalance(transferAmount);
        if (!fundsReceived){
            accountFrom.addToBalance(transferAmount);
            throw new DaoException("The account #" + accountFrom.getAccount_id() +
                    " has maxed out it's balance and cannot receive the allotted funds");
        }

        return transfer;

    }
}
