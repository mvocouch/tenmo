package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.exception.TransferExceptions;
import com.techelevator.tenmo.model.*;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        int transferType = transferDto.getType();
        Account accountSending;
        Account accountReceiving;


        if (transferType == TransferType.SEND_MONEY){
            accountSending = accountDao.getAccountByUserId(loggedInUser.getId());
            accountReceiving = accountDao.getAccountByUserId(transferDto.getUserId());
        } else if (transferType == TransferType.REQUEST_MONEY) {
            accountSending = accountDao.getAccountByUserId(transferDto.getUserId());
            accountReceiving = accountDao.getAccountByUserId(loggedInUser.getId());
        } else {
            throw new RuntimeException("Invalid transfer type.");
        }

        transfer.setTransferStatus(TransferStatus.PENDING);
        transfer.setTransferType(transferDto.getType());
        transfer.setAccountTo(accountReceiving.getAccount_id());
        transfer.setAccountFrom(accountSending.getAccount_id());
        transfer.setAmount(transferDto.getAmount());

        return transfer;
    }
    public Transfer initializeTransfer(User loggedInUser, TransferDto transferDto) {
        Transfer transfer = createTransferFromDto(loggedInUser, transferDto);
        transfer = transferDao.addTransfer(transfer);
        if (transfer.getTransferType() == TransferType.SEND_MONEY){
            accountService.transferFunds(transfer);
        }
        return transfer;
    }

    public Transfer rejectTransfer(User loggedInUser, Long transferId){
        try {
            Transfer transfer = transferDao.getTransferById(transferId);

            if (isRequestValid(transfer)){
                if (isUserAuthorized(loggedInUser, transfer)){
                    transfer.setTransferStatus(TransferStatus.REJECTED);
                    transferDao.updateTransferStatus(transferId, TransferStatus.REJECTED);
                    return transfer;
                } else {
                    throw new TransferExceptions.TransferUnauthorizedException("Unauthorized to accept this transfer.");
                }
            } else {
                throw new TransferExceptions.TransferNotFoundException("Transfer not found or not pending.");
            }
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error accessing database", e);
        } catch (TransferExceptions.TransferUnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (TransferExceptions.TransferNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An error occurred while processing the transfer", e);
        }
    }

    public Transfer acceptTransfer(User loggedInUser, Long transferId){
        try {
            Transfer transfer = transferDao.getTransferById(transferId);

            if (isRequestValid(transfer)){
                if (isUserAuthorized(loggedInUser, transfer)){
                    transfer.setTransferStatus(TransferStatus.APPROVED);
                    accountService.transferFunds(transfer);
                    transferDao.updateTransferStatus(transferId, TransferStatus.APPROVED);
                    return transfer;
                } else {
                    throw new TransferExceptions.TransferUnauthorizedException("Unauthorized to accept this transfer.");
                }
            } else {
                throw new TransferExceptions.TransferNotFoundException("Transfer not found or not pending.");
            }
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error accessing database", e);
        } catch (TransferExceptions.TransferUnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (TransferExceptions.TransferNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An error occurred while processing the transfer", e);
        }
    }
//    public Transfer rejectTransfer(User loggedInUser, Long transferId){
//        Transfer transfer = transferDao.getTransferById(transferId);
//        if (transfer != null && transfer.getTransferStatus() == TransferStatus.PENDING) {
//            Account loggedInUserAccount = accountDao.getAccountByUserId(loggedInUser.getId());
//            if(loggedInUserAccount.getAccount_id() == transfer.getAccountTo()) {
//                transfer.setTransferType(TransferStatus.REJECTED);
//                transferDao.updateTransferStatus(transferId, TransferStatus.REJECTED);
//                return transferDao.getTransferById(transferId);
//            } else {
//                throw new TransferExceptions.TransferUnauthorizedException("Unauthorized to reject this transfer.");
//            }
//
//        } else {
//            throw new TransferExceptions.TransferNotFoundException("Transfer not found or not pending.");
//        }
//    }

    public boolean isUserAuthorized(User loggedInUser, Transfer transfer) {
        Account loggedInUserAccount = accountDao.getAccountByUserId(loggedInUser.getId());
        return loggedInUserAccount.getAccount_id() == transfer.getAccountFrom();
    }

    public boolean isRequestValid(Transfer transfer) {
        return transfer != null && transfer.getTransferStatus() == (TransferStatus.PENDING);
    }
}
