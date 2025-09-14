package Banking.TransactionService.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "transfer.fee")
public class TransferFeeConfig {
    private BigDecimal percentage; 
    private BigDecimal minFee;    
    private BigDecimal maxFee;    
    private String bankAccountNumber; 

    public BigDecimal getPercentage() { return percentage; }
    public void setPercentage(BigDecimal percentage) { this.percentage = percentage; }
    
    public BigDecimal getMinFee() { return minFee; }
    public void setMinFee(BigDecimal minFee) { this.minFee = minFee; }
    
    public BigDecimal getMaxFee() { return maxFee; }
    public void setMaxFee(BigDecimal maxFee) { this.maxFee = maxFee; }
    
    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }
}