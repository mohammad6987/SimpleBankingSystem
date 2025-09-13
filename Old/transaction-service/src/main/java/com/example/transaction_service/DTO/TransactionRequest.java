package com.example.transaction_service.DTO;

import java.math.BigDecimal;

public class TransactionRequest {
    private String type; 
    private String sourceAccount;
    private String destinationAccount;
    private BigDecimal amount;

    public TransactionRequest(String type , String source , String dest , BigDecimal amount){
    this.type =type;
    this.sourceAccount = source;
    this.destinationAccount = dest;
    this.amount = amount;
    }
    public String getType() {
        return type;
    }
    public String getSource(){
        return sourceAccount;
    }
    public String getDest(){
        return destinationAccount;
    }
    public BigDecimal getAmount(){
        return amount;
    }



}