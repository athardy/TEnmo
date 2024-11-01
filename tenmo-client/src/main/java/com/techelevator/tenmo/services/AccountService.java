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


    @Autowired
    public AccountService (String url){
        this.API_BASE_URL = url;
    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

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



    public int getUserIdByAccountId(int accountId) {
        String url = API_BASE_URL + "/account/account/" + accountId;
        return restTemplate.getForObject(url, Integer.class);
    }

}
