package com.example.transaction_service.clients;

import com.example.transaction_service.DTO.AccountDTO;
import com.example.transaction_service.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class AccountClient {

    private final RabbitTemplate rabbitTemplate;

    public AccountClient(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public AccountDTO getAccountByNumber(String accountNumber) {
        Object response = rabbitTemplate.convertSendAndReceive(
                RabbitConfig.ACCOUNT_REQUEST_QUEUE,
                Map.of("action", "getAccount", "accountNumber", accountNumber)
        );

        if (response == null) {
            throw new RuntimeException("No response from Account Service for account: " + accountNumber);
        }

        return (AccountDTO) response;
    }

    public void creditAccount(String accountNumber, BigDecimal amount) {
        Map<String, Object> request = new HashMap<>();
        request.put("action", "credit");
        request.put("accountNumber", accountNumber);
        request.put("amount", amount);

        Object response = rabbitTemplate.convertSendAndReceive(RabbitConfig.ACCOUNT_REQUEST_QUEUE, request);
        if (response == null) {
            throw new RuntimeException("Credit request failed for account: " + accountNumber);
        }
    }

    public void debitAccount(String accountNumber, BigDecimal amount) {
        Map<String, Object> request = new HashMap<>();
        request.put("action", "debit");
        request.put("accountNumber", accountNumber);
        request.put("amount", amount);

        Object response = rabbitTemplate.convertSendAndReceive(RabbitConfig.ACCOUNT_REQUEST_QUEUE, request);
        if (response == null) {
            throw new RuntimeException("Debit request failed for account: " + accountNumber);
        }
    }

    public boolean hasSufficientBalance(String accountNumber, BigDecimal amount) {
        Map<String, Object> request = new HashMap<>();
        request.put("action", "hasBalance");
        request.put("accountNumber", accountNumber);
        request.put("amount", amount);

        Object response = rabbitTemplate.convertSendAndReceive(RabbitConfig.ACCOUNT_REQUEST_QUEUE, request);
        if (response == null) {
            throw new RuntimeException("Balance check failed for account: " + accountNumber);
        }

        return (Boolean) response;
    }
}
