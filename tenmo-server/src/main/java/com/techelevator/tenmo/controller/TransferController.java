package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.service.AccountService;
import com.techelevator.tenmo.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {
    private final TransferService transferService;
    private final AccountService accountService;
    private final UserDao userDao;

    public TransferController(TransferService transferService, AccountService accountService, UserDao userDao) {
        this.transferService = transferService;
        this.accountService = accountService;
        this.userDao = userDao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer", method = RequestMethod.PUT)
    public Transfer createTransfer(Principal principal, @RequestBody TransferDto transferDto){
        User loggedInUser = userDao.getUserByUsername(principal.getName());

        Transfer createdTransfer;
        try{
            createdTransfer = transferService.createTransferFromDto(loggedInUser, transferDto);
            if (createdTransfer == null){
                //Create custom exception
                throw new RuntimeException();
            } else {
                accountService.transferFunds(createdTransfer);
            }
        }  catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return createdTransfer;
    }
}