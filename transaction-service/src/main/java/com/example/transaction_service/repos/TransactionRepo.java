package com.example.transaction_service.repos;

import com.example.transaction_service.models.Transaction;
import com.example.transaction_service.models.Account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountFrom(Account account);
    List<Transaction> findByAccountTo(Account account);
    Transaction findByTrackingCode(String trackingCode);
    Page<Transaction> findByAccountFromOrAccountTo(Account accountFrom, Account accountTo, Pageable pageable);


}