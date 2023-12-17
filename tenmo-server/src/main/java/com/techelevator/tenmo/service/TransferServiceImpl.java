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
        Account senderAccount = accountDao.getAccountByUserId(loggedInUser.getId());
        Account account = accountDao.getAccountByUserId(transferDto.getUserId());
        transfer.setTransferStatus(TransferStatus.PENDING);
        transfer.setTransferType(transferDto.getType());
        transfer.setAccountTo(account.getAccount_id());
        transfer.setAccountFrom(senderAccount.getAccount_id());
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

    public Transfer acceptTransfer(User loggedInUser, Long transferId){
        try {
            Transfer transfer = transferDao.getTransferById(transferId);

            if (transfer != null && transfer.getTransferStatus() == (TransferStatus.PENDING)) {
                Account loggedInUserAccount = accountDao.getAccountByUserId(loggedInUser.getId());
                if (loggedInUserAccount.getAccount_id() == transfer.getAccountTo()) {
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

}
