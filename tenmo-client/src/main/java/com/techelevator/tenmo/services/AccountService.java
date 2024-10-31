package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;

public class AccountService {

    private final String API_BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken;

    public AccountService(String apiUrl) {
        this.API_BASE_URL = apiUrl;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * Retrieves the current balance for the authenticated user.
     *
     * @return the user's balance as a BigDecimal.
     */
    public BigDecimal getBalance() {
        String url = API_BASE_URL + "/account/balance";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, entity, BigDecimal.class).getBody();
    }
    public int getAccountIdByUserId(int userId) {
        String url = API_BASE_URL + "/account/user/" + userId;
        return restTemplate.getForObject(url, Integer.class);
    }
}
