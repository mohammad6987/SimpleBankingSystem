package com.example.transaction_service.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8081/accounts";

    public void creditAccount(String accountNumber, BigDecimal amount) {
        restTemplate.postForObject(BASE_URL + "/" + accountNumber + "/credit?amount=" + amount, null, Void.class);
    }

    public void debitAccount(String accountNumber, BigDecimal amount) {
        restTemplate.postForObject(BASE_URL + "/" + accountNumber + "/debit?amount=" + amount, null, Void.class);
    }

    public boolean hasSufficientBalance(String accountNumber, BigDecimal amount) {
        return restTemplate.getForObject(BASE_URL + "/" + accountNumber + "/has-balance?amount=" + amount, Boolean.class);
    }
}