package com.example.account_service.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;


public enum AccountStatus {
    ACTIVE,
    INACTIVE,
    BLOCKED
}


@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private Long accountNumber;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    private BigDecimal balance;

    private AccountStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Long getAccountNumber() { return accountNumber; }
    public void setAccountNumber(Long accountNumber) { this.accountNumber = accountNumber; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getStatus() { return status.toString(); }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
