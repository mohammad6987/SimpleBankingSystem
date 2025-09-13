package com.example.account_service.services;

import com.example.account_service.models.AccountHistory;
import com.example.account_service.repositories.AccountHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountHistoryService {

    private final AccountHistoryRepository historyRepository;

    public AccountHistoryService(AccountHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public AccountHistory logChange(String accountNumber, String fieldName, String oldValue, String newValue, String changedBy) {
        AccountHistory history = new AccountHistory(accountNumber, fieldName, oldValue, newValue, changedBy);
        return historyRepository.save(history);
    }


    public List<AccountHistory> getHistoryForAccount(String accountNumber) {
        return historyRepository.findByAccountNumberOrderByChangedAtDesc(accountNumber);
    }
}
