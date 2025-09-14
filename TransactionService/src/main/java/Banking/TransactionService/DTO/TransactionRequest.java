package Banking.TransactionService.DTO;

import java.math.BigDecimal;

public class TransactionRequest {
    private String accountNumber;
    private BigDecimal amount;
    

    public TransactionRequest() {}
    
    public TransactionRequest(String accountNumber, BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }
    

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
