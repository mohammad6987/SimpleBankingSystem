package com.example.transaction_service.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id @GeneratedValue
    private Long id;
    private String trackingCode;
    private String type; // deposit, withdraw, transfer
    private String accountFrom;
    private String accountTo;
    private BigDecimal amount;
    private BigDecimal fee;
    private String status;
    private LocalDateTime createdAt;
}