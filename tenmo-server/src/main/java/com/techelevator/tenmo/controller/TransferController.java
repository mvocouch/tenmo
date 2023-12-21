package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.dto.TransferStatusUpdateDto;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.service.AccountService;
import com.techelevator.tenmo.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {
    private final TransferService transferService;
    private final AccountService accountService;
    private final UserDao userDao;
    private final TransferDao transferDao;

    public TransferController(TransferService transferService, AccountService accountService, UserDao userDao, TransferDao transferDao) {
        this.transferService = transferService;
        this.accountService = accountService;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "/transfer/pending", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(Principal principal){
        User loggedInUser = userDao.getUserByUsername(principal.getName());
        //Ask Srdan if we should try/catch here
        return transferDao.getPendingTransfersForUser(loggedInUser.getId());
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

    //We should change this method to an updateTransfer method that can change it
    //to Approved or Rejected based on the TransferStatusUpdateDto made in client
    //the Dto just contains the ID of the transfer status
    @RequestMapping(path = "/transfer/{transferId}", method = RequestMethod.PUT)
    public Transfer updateTransferStatus(Principal principal, @PathVariable Long transferId, @RequestBody TransferStatusUpdateDto dto) {
        User loggedInUser = userDao.getUserByUsername(principal.getName());
        Transfer updatedTransfer = null;
        try {
            int newTransferStatus = dto.getTransferStatus();
            if (newTransferStatus == TransferStatus.APPROVED) {
                updatedTransfer = transferService.acceptTransfer(loggedInUser, transferId);
            } else if (newTransferStatus == TransferStatus.REJECTED) {
                updatedTransfer = transferService.rejectTransfer(loggedInUser, transferId);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return updatedTransfer;
    }
}