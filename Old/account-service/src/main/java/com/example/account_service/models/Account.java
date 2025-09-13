package com.example.account_service.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "account_number", length = 14)
    @Pattern(regexp = "\\d{14}", message = "Account number must be exactly 14 digits")
    private String accountNumber; 



    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;


    @Column(nullable = false)
    private String status;





    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}
