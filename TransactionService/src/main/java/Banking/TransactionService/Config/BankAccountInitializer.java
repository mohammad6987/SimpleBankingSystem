package Banking.TransactionService.Config;

import Banking.TransactionService.Model.Account;
import Banking.TransactionService.Repository.AccountRepository;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BankAccountInitializer implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private TransferFeeConfig transferFeeConfig;

    @Override
    public void run(String... args) throws Exception {
        String bankAccountNumber = transferFeeConfig.getBankAccountNumber();
        
        if (!accountRepository.findByAccountNumber(bankAccountNumber).isPresent()) {
  
            Account bankAccount = new Account();
            bankAccount.setAccountNumber(bankAccountNumber);
            bankAccount.setBalance(BigDecimal.ZERO);
            bankAccount.setStatus(Account.AccountStatus.ACTIVE);
            
            accountRepository.save(bankAccount);
            System.out.println("Bank account created: " + bankAccountNumber);
        } else {
            System.out.println("Bank account already exists: " + bankAccountNumber);
        }
    }
}