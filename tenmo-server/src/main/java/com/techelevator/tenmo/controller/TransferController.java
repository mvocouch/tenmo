package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
public class TransferController {
    private final AccountDao accountDao;
    private final TransferDao transferDao;

    public TransferController(AccountDao accountDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.PUT)
    public Transfer createTransfer(@RequestBody Transfer transfer){
        BigDecimal transferAmount = transfer.getAmount();
        int accountIdFrom = transfer.getAccountFrom();
        int accountIdTo = transfer.getAccountTo();

        try{
            Account accountFrom = accountDao.getAccountById(accountIdFrom);
            Account accountTo = accountDao.getAccountById(accountIdTo);

            if (accountFrom.equals(accountTo)){
                throw new DaoException("Funds cannot be transferred from and receive by the same account.");
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
        }  catch (DaoException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return transfer;
    }

}
