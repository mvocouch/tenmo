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

import javax.validation.Valid;
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
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public Transfer createTransfer(Principal principal, @Valid @RequestBody TransferDto transferDto){
        User loggedInUser = userDao.getUserByUsername(principal.getName());

        Transfer createdTransfer = null;
        try{
            createdTransfer =  transferService.initializeTransfer(loggedInUser, transferDto);
        }  catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return createdTransfer;
    }

    @RequestMapping(path = "/transfer/{transferId}", method = RequestMethod.PUT)
    public Transfer acceptTransfer(Principal principal, @PathVariable Long transferId) {
        User loggedInUser = userDao.getUserByUsername(principal.getName());
        Transfer acceptedTransfer = null;
        try {
            transferService.acceptTransfer(loggedInUser, transferId);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return acceptedTransfer;
    }
}