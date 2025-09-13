package com.example.account_service.services;

import com.example.account_service.models.Account;
import com.example.account_service.models.AccountStatus;
import com.example.account_service.repos.AccountRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepo accountRepo;
    private final AccountHistoryService historyService;

    public AccountService(AccountRepo accountRepo, AccountHistoryService historyService) {
        this.accountRepo = accountRepo;
        this.historyService = historyService;
    }

    public Account createAccount(Long accountNumber,
                                 String customerId,
                                 AccountStatus status,
                                 String createdBy) {
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setCustomerId(customerId);
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(status != null ? status : AccountStatus.ACTIVE);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        Account saved = accountRepo.save(account);

        historyService.logChange(
                saved.getAccountNumber().toString(),
                "CREATE_ACCOUNT",
                null,
                "Created with status: " + saved.getStatus(),
                createdBy
        );

        return saved;
    }

    @Transactional
    public Account deposit(Long accountNumber, BigDecimal amount, String changedBy) {
        Account account = findAccount(accountNumber);
        checkIfActive(account);

        BigDecimal oldBalance = account.getBalance();
        account.setBalance(oldBalance.add(amount));
        account.setUpdatedAt(LocalDateTime.now());

        Account updated = accountRepo.save(account);


        historyService.logChange(
                accountNumber.toString(),
                "balance",
                oldBalance.toPlainString(),
                updated.getBalance().toPlainString(),
                changedBy
        );

        return updated;
    }


    @Transactional
    public Account withdraw(Long accountNumber, BigDecimal amount, String changedBy) {
        Account account = findAccount(accountNumber);
        checkIfActive(account);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        BigDecimal oldBalance = account.getBalance();
        account.setBalance(oldBalance.subtract(amount));
        account.setUpdatedAt(LocalDateTime.now());

        Account updated = accountRepo.save(account);

        historyService.logChange(
                accountNumber.toString(),
                "balance",
                oldBalance.toPlainString(),
                updated.getBalance().toPlainString(),
                changedBy
        );

        return updated;
    }

    @Transactional
    public Account changeStatus(Long accountNumber, AccountStatus newStatus, String changedBy) {
        Account account = findAccount(accountNumber);

        AccountStatus oldStatus = account.getStatus();
        account.setStatus(newStatus);
        account.setUpdatedAt(LocalDateTime.now());

        Account updated = accountRepo.save(account);

  
        historyService.logChange(
                accountNumber.toString(),
                "status",
                oldStatus != null ? oldStatus.name() : null,
                newStatus.name(),
                changedBy
        );

        return updated;
    }


    public List<Account> getAccountsByCustomer(String customerId) {
        return accountRepo.findByCustomerId(customerId);
    }


    public Account getAccount(Long accountNumber) {
        return findAccount(accountNumber);
    }


    private Account findAccount(Long accountNumber) {
        Optional<Account> account = accountRepo.findById(accountNumber);
        if (account.isEmpty()) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        return account.get();
    }

    private void checkIfActive(Account account) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active: " + account.getStatus());
        }
    }
}
