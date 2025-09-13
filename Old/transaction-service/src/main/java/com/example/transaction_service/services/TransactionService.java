package com.example.transaction_service.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.transaction_service.DTO.TransactionRequest;
import com.example.transaction_service.DTO.TransactionResponse;
import com.example.transaction_service.DTO.AccountDTO;
import com.example.transaction_service.clients.AccountClient;
import com.example.transaction_service.repos.*;
import com.example.transaction_service.models.*;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private FeeRepo feeRepo;

    @Autowired
    private AccountClient accountClient;  // RabbitMQ client

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest req) {
        
        AccountDTO accountFrom = accountClient.getAccountByNumber(req.getSource());
        if (accountFrom == null) {
            throw new IllegalArgumentException("Source account not found: " + req.getSource());
        }

        AccountDTO accountTo = accountClient.getAccountByNumber(req.getDest());
        if (accountTo == null) {
            throw new IllegalArgumentException("Destination account not found: " + req.getDest());
        }

        if (accountFrom.getAccountNumber().equals(accountTo.getAccountNumber())) {
            throw new IllegalArgumentException("Source and destination accounts cannot be the same");
        }

        if (!"ACTIVE".equals(accountFrom.getStatus()) || !"ACTIVE".equals(accountTo.getStatus())) {
            throw new IllegalStateException("One of the accounts is not active");
        }

        if (accountFrom.getBalance().compareTo(req.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds in source account");
        }

  
        Transaction tx = new Transaction();
        tx.setType(req.getType());
        tx.setAccountFrom(req.getSource()); 
        tx.setAccountTo(req.getDest());
        tx.setAmount(req.getAmount());
        tx.setCreatedAt(LocalDateTime.now());
        tx.setStatus("PENDING");

        transactionRepo.save(tx);

        return new TransactionResponse(tx.getId(), "Transaction created successfully", LocalDateTime.now());
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepo.findById(id);
    }
}
