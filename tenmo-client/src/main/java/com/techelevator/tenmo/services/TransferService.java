package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.CreateTransferDTO;
import com.techelevator.tenmo.model.UserDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class TransferService {


    public static final int REQUEST_TYPE_ID = 2;

    private final String API_BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken;

    public TransferService(String apiUrl) {
        this.API_BASE_URL = apiUrl;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
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

        TransferDTO[] transferArray = restTemplate.exchange(API_BASE_URL + "/transfer/pending/" + userId, HttpMethod.GET, entity, TransferDTO[].class).getBody();
        return Arrays.asList(transferArray);
    }

}
