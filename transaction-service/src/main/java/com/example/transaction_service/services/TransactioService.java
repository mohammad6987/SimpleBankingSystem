package com.example.transaction_service.services;

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

    @Transactional
    public TransactionResponse createTransaciont(TransactionRequest req){
        Transaction tx = new Transaction();
        tx.setType(req.getType());



        return null;

    }

    public Transaction getTransactionById(Long id) {
        return transactionRepo.findById(id);
    }



    

}
