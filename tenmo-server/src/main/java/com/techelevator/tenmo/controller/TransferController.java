package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {
    private final AccountDao accountDao;
    private final TransferService transferService;

    public TransferController(AccountDao accountDao, TransferService transferService) {
        this.accountDao = accountDao;
        this.transferService = transferService;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer", method = RequestMethod.PUT)
    public Transfer createTransfer(Principal principal, @RequestBody Transfer transfer){
        try{
            transferService.createTransfer(transfer);
        }  catch (DaoException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return transfer;
    }

}
