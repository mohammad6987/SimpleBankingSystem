package com.example.transaction_service.repos;

import com.example.transaction_service.models.Transaction;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountFrom(String account);
    List<Transaction> findByAccountTo(String account);
    Transaction findByTrackingCode(String trackingCode);
    Page<Transaction> findByAccountFromOrAccountTo(String accountFrom, String accountTo, Pageable pageable);


}