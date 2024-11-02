package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;

public class AccountService {

    private final String API_BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken;

    // Constructor initializes the AccountService with the base API URL for account-related requests.
    @Autowired
    public AccountService(String url) {
        this.API_BASE_URL = url;
    }

    // Sets the authorization token for making authenticated requests.
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    // Retrieves the account balance for the currently authenticated user.
    public BigDecimal getBalance() {
        String url = API_BASE_URL + "/account/balance";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken); // Adds the auth token to request headers
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, entity, BigDecimal.class).getBody();
    }

    // Retrieves the account ID associated with a specific user ID.
    public int getAccountIdByUserId(int userId) {
        String url = API_BASE_URL + "/account/user/" + userId;
        return restTemplate.getForObject(url, Integer.class);
    }

    // Retrieves the user ID associated with a specific account ID.
    public int getUserIdByAccountId(int accountId) {
        String url = API_BASE_URL + "/account/account/" + accountId;
        return restTemplate.getForObject(url, Integer.class);
    }
}

