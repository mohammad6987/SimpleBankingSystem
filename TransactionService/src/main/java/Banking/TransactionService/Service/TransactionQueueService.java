package Banking.TransactionService.Service;

import java.math.BigDecimal;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Banking.TransactionService.Config.RabbitMQConfig;
import Banking.TransactionService.DTO.AccountCreationMessage;
import Banking.TransactionService.DTO.AccountStatus;
import Banking.TransactionService.Model.Account;
import Banking.TransactionService.Repository.AccountRepository;

@Service
public class TransactionQueueService {


    @Autowired
    private AccountRepository accountRepository;

    @RabbitListener(queues = RabbitMQConfig.ACCOUNT_CREATION_QUEUE)
    public void receiveMessage(AccountCreationMessage message) {
        System.out.println("Received message: " + message.toString());
        Account account = new Account();
        account.setAccountNumber(message.getAccountNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(Account.AccountStatus.ACTIVE);
        try {
            accountRepository.save(account);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("saved an account successfully!");
        
    }


    
}
