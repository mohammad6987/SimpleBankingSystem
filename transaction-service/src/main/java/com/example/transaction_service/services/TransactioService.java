package com.example.transaction_service.services;

import java.security.Timestamp;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.transaction_service.DTO.TransactionRequest;
import com.example.transaction_service.DTO.TransactionResponse;
import com.example.transaction_service.repos.*;
import com.example.transaction_service.models.*;

import jakarta.transaction.Transactional;

import com.example.transaction_service.DTO.*;
@Service
public class TransactioService {

    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private FeeRepo feeRepo;
    @Autowired
    private AccountRepo accountRepo;

    @Transactional
public TransactionResponse createTransaction(TransactionRequest req) {
    // Fetch source account or throw exception if not found
    Account accountFrom = accountRepo.findById(req.getSource())
        .orElseThrow(() -> new IllegalArgumentException("Source account not found with number: " + req.getSource()));
    
    // Fetch destination account or throw exception if not found
    Account accountTo = accountRepo.findById(req.getDest())
        .orElseThrow(() -> new IllegalArgumentException("Destination account not found with number: " + req.getDest()));
    
    // Validate that source and destination accounts are different
    if (accountFrom.getAccountNumber().equals(accountTo.getAccountNumber())) {
        throw new IllegalArgumentException("Source and destination accounts cannot be the same");
    }
    
    // Create and save the transaction
    Transaction tx = new Transaction();
    tx.setType(req.getType());
    tx.setAccountFrom(accountFrom);
    tx.setAccountTo(accountTo);
    tx.setAmount(req.getAmount());
    tx.setCreatedAt(LocalDateTime.now());
    tx.setStatus("PENDING");
    
    
    transactionRepo.save(tx);
    

    return new TransactionResponse(tx.getId(), "Transaction created successfully" ,LocalDateTime.now() );
}

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepo.findById(id);
    }



    

}
