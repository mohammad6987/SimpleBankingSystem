package com.example.transaction_service.DTO;

import java.time.LocalDateTime;

public class TransactionResponse {
    private Long transactionId;
    private String status;
    private LocalDateTime createdAt;

    public TransactionResponse(Long transactionId, String status, LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.status = status;
        this.createdAt = createdAt;
    }

}