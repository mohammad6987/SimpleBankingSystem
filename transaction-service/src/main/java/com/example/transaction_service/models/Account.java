package com.example.transaction_service.models;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;


@Entity
@Table(name = "accounts")
public class Account {
    @Id
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private BigDecimal balance;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
