package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.CreateTransferDTO;
import com.techelevator.tenmo.model.UserDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class TransferService {

    private final String API_BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken;


    private static final int REJECTED_STATUS_ID = 3;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public TransferService(String apiUrl) {
        this.API_BASE_URL = apiUrl;
    }

    public TransferDTO createTransfer(CreateTransferDTO createTransferDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<CreateTransferDTO> entity = new HttpEntity<>(createTransferDTO, headers);

        return restTemplate.postForObject(API_BASE_URL + "/transfer", entity, TransferDTO.class);
    }

    public List<TransferDTO> getTransfersByUserId(int userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        TransferDTO[] transferArray = restTemplate.exchange(
                API_BASE_URL + "/transfer/user/" + userId, HttpMethod.GET, entity, TransferDTO[].class).getBody();

        return Arrays.asList(transferArray);
    }

    public TransferDTO getTransferDetails(int transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                API_BASE_URL + "/transfer/details/" + transferId, HttpMethod.GET, entity, TransferDTO.class).getBody();
    }

    public List<UserDTO> getAllUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        UserDTO[] userArray = restTemplate.exchange(
                API_BASE_URL + "/account/users", HttpMethod.GET, entity, UserDTO[].class).getBody();

        return Arrays.asList(userArray);
    }

    public List<TransferDTO> getPendingTransfersByUserId(int userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        TransferDTO[] transferArray = restTemplate.exchange(
                API_BASE_URL + "/transfer/pending/" + userId, HttpMethod.GET, entity, TransferDTO[].class).getBody();
        return Arrays.asList(transferArray);
    }

//    public TransferDTO updateTransferStatus(int transferId, String action) {
//        TransferDTO transfer = transferDao.getTransferById(transferId);
//        if (!transfer.getTransferStatus().equals("Pending")) {
//            throw new IllegalArgumentException("Only pending transfers can be updated.");
//        }
//        if (action.equalsIgnoreCase("approve")) {
//            BigDecimal accountBalance = accountDao.getBalanceByUserId(transfer.getAccountFrom());//updated and fixed
//            if (accountBalance.compareTo(transfer.getAmount()) < 0) {
//                throw new IllegalArgumentException("Insufficient funds");
//            }
//            transfer.setTransferStatusId(APPROVED_STATUS_ID);
//            transferDao.updateTransferStatus(transferId, APPROVED_STATUS_ID);
//            accountDao.updateBalances(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
//        } else if (action.equalsIgnoreCase("reject")) {
//            transfer.setTransferStatusId(REJECTED_STATUS_ID);
//            transferDao.updateTransferStatus(transferId, REJECTED_STATUS_ID);
//        } else {
//            throw new IllegalArgumentException("Invalid input.");
//        }
//        return transfer;
//    }

    private TransferDTO getTransferByTransferId(int transferId){
        TransferDTO transfer = new TransferDTO();
        transfer.setTransferId(transferId);
        transfer.setTransferStatusId(1);
        transfer.setAmount(BigDecimal.valueOf(0.00));
        transfer.setAccountFrom(1001);
        transfer.setAccountTo(1002);
        return  transfer;
    }


//    public TransferDTO approveTransfer(int transferId){
//    TransferDTO transfer = getTransferByTransferId(transferId);
//    if (transfer != null) {
//        transfer.setTransferStatusId(2);
//        System.out.println("Transfer approved with ID: " + transferId);
//    } else {
//        System.out.println("Transfer ID not found: " + transferId);
//    }
//    return transfer;
//    }
//
//    public TransferDTO rejectTransfer (int transferId) {
//        TransferDTO transfer = getTransferByTransferId(transferId);
//        if (transfer != null) {
//            transfer.setTransferStatusId(3);
//            System.out.println("Transfer rejected with ID: " + transferId);
//        } else {
//            System.out.println("Transfer ID not found: " + transferId);
//        }
//        return transfer;
//    }

    public TransferDTO approveTransfer(int transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                API_BASE_URL + "/transfer/" + transferId + "/approve",
                HttpMethod.PUT, entity, TransferDTO.class).getBody();
    }

    public TransferDTO rejectTransfer(int transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                API_BASE_URL + "/transfer/" + transferId + "/reject",
                HttpMethod.PUT, entity, TransferDTO.class).getBody();
    }

}
