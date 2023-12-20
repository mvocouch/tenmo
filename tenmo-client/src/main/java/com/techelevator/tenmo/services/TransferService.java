package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.TransferStatusUpdateDto;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

public class TransferService extends AuthTokenService{
    public TransferService(String baseUrl) {
        this.baseUrl = baseUrl + "transfer/";
    }
    public Transfer createTransfer(TransferDto dto) {
       Transfer transfer = null;
       try {
           ResponseEntity<Transfer> response =
                   restTemplate.exchange(baseUrl, HttpMethod.POST, makeTransferDtoEntity(dto),  Transfer.class);
           transfer = response.getBody();
       } catch (RestClientResponseException e) {
           e.getMessage();
       } catch (ResourceAccessException e) {
           e.getMessage();
       } return transfer;
    }
    // need a rework of transfer controller, need 1 method to update to accept or reject
    public Transfer updatePendingTransferStatus(int transferId, int status) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(baseUrl + transferId, HttpMethod.PUT, makeTransferStatusEntity(status), Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException e) {
            e.getMessage();
        } catch (ResourceAccessException e) {
            e.getMessage();
        } return transfer;
    }
    public Transfer getTransferDetails(int transferId) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(baseUrl + transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException e) {
            e.getMessage();
        } catch (ResourceAccessException e) {
            e.getMessage();
        } return transfer;
    }

    private HttpEntity<TransferStatusUpdateDto> makeTransferStatusEntity(int transferStatusId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(new TransferStatusUpdateDto(transferStatusId), headers);
    }

    private HttpEntity<TransferDto> makeTransferDtoEntity(TransferDto transferDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transferDto, headers);
    }
}
