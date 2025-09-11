package com.example.transaction_service.DTO;

import java.math.BigDecimal;

public class TransactionRequest {
    private String type; // DEPOSIT, WITHDRAW, TRANSFER
    private String sourceAccount;
    private String destinationAccount;
    private BigDecimal amount;

    public TransactionRequest(String type , String source , String dest , String amount){
        self.type =type;
        self.sourceAccount = source;
        self.destinationAccount = dest;
        self.amount = amount;
    }
    public String getType() {
        return type;
    }
    public String getSource(){
        return sourceAccount;
    }



}