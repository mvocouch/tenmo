package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountServiceImpl implements AccountService{
    private final AccountDao accountDao;
    private final TransferDao transferDao;

    public AccountServiceImpl(AccountDao accountDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    @Override
    public void transferFunds(Transfer transfer) {
        BigDecimal transferAmount = transfer.getAmount();
        int accountIdFrom = transfer.getAccountFrom();
        int accountIdTo = transfer.getAccountTo();
        int approvedStatusId = 2;
        int rejectedStatusId = 3;

        Account accountFrom = accountDao.getAccountById(accountIdFrom);
        Account accountTo = accountDao.getAccountById(accountIdTo);

        if (accountFrom.equals(accountTo)){
            throw new DaoException("Funds cannot be transferred from and received by the same account.");
        }

        //We want to change this to use AccountDao instead of Account
        boolean fundsWithdrawn = accountFrom.subtractFromBalance(transferAmount);
        if (!fundsWithdrawn){
            //transferDao.updateTransferStatus(transfer.getId(), rejectedStatusId);
            throw new DaoException("The account #" + accountTo.getAccount_id() +
                    " has an insufficient balance to perform the requested withdrawal.");
        }

        //We want to change this to use AccountDao instead of Account
        boolean fundsReceived = accountTo.addToBalance(transferAmount);
        if (!fundsReceived){
            accountFrom.addToBalance(transferAmount);
            //transferDao.updateTransferStatus(transfer.getId(), rejectedStatusId);
            throw new DaoException("The account #" + accountFrom.getAccount_id() +
                    " has maxed out it's balance and cannot receive the allotted funds");
        } else {
            transferDao.updateTransferStatus(transfer.getId(), approvedStatusId);
        }
        //accountDao.updateAccount(accountFrom);
        //accountDao.updateAccount(accountTo);
    }
}
