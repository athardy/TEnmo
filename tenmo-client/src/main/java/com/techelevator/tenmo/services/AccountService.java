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
    private final AccountDao accountDao;

    @Autowired
    public AccountService (AccountDao accountDao){
        this.accountDao=accountDao;
        this.API_BASE_URL = null;
    }

    public AccountService(String apiUrl) {
        this.API_BASE_URL = apiUrl;
        this.accountDao = null;
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

    public int getUserIdByAccountId(int accountId){
        return accountDao.getUserIdByAccountId(accountId);
    }


}
