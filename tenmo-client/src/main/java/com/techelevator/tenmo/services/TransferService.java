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
import java.util.stream.Collectors;

@Service
public class TransferService {

    private final String API_BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken;

    private static final int REJECTED_STATUS_ID = 3;

    // Sets the authorization token for authenticated requests.
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    // Constructor initializes the API base URL for the service.
    public TransferService(String apiUrl) {
        this.API_BASE_URL = apiUrl;
    }

    // Sends a request to create a new transfer based on provided data.
    public TransferDTO createTransfer(CreateTransferDTO createTransferDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<CreateTransferDTO> entity = new HttpEntity<>(createTransferDTO, headers);

        return restTemplate.postForObject(API_BASE_URL + "/transfer", entity, TransferDTO.class);
    }

    // Retrieves all transfers associated with a specific user ID.
    public List<TransferDTO> getTransfersByUserId(int userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        TransferDTO[] transferArray = restTemplate.exchange(
                API_BASE_URL + "/transfer/user/" + userId, HttpMethod.GET, entity, TransferDTO[].class).getBody();

        return Arrays.asList(transferArray);
    }

    // Retrieves details of a specific transfer by transfer ID.
    public TransferDTO getTransferDetails(int transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                API_BASE_URL + "/transfer/details/" + transferId, HttpMethod.GET, entity, TransferDTO.class).getBody();
    }

    // Retrieves all users except the current user, based on user ID.
    public List<UserDTO> getAllUsers(int currentUserId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        UserDTO[] userArray = restTemplate.exchange(
                API_BASE_URL + "/account/users", HttpMethod.GET, entity, UserDTO[].class).getBody();

        return Arrays.stream(userArray)
                .filter(user -> user.getUserId() != currentUserId)  // Filter out the current user
                .collect(Collectors.toList());
    }

    // Retrieves all pending transfers for a specific user.
    public List<TransferDTO> getPendingTransfersByUserId(int userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        TransferDTO[] transferArray = restTemplate.exchange(
                API_BASE_URL + "/transfer/pending/" + userId, HttpMethod.GET, entity, TransferDTO[].class).getBody();
        return Arrays.asList(transferArray);
    }

    // Provides a default TransferDTO object when a transfer ID is not found.
    private TransferDTO getTransferByTransferId(int transferId){
        TransferDTO transfer = new TransferDTO();
        transfer.setTransferId(transferId);
        transfer.setTransferStatusId(1);  // Default status set to 'Pending'
        transfer.setAmount(BigDecimal.valueOf(0.00));
        transfer.setAccountFrom(1001);    // Default sender account
        transfer.setAccountTo(1002);      // Default receiver account
        return  transfer;
    }

    // Approves a specific transfer by its transfer ID.
    public TransferDTO approveTransfer(int transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                API_BASE_URL + "/transfer/" + transferId + "/approve",
                HttpMethod.PUT, entity, TransferDTO.class).getBody();
    }

    // Rejects a specific transfer by its transfer ID.
    public TransferDTO rejectTransfer(int transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                API_BASE_URL + "/transfer/" + transferId + "/reject",
                HttpMethod.PUT, entity, TransferDTO.class).getBody();
    }

}
