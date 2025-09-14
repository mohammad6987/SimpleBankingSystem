package Banking.TransactionService.Service;

import Banking.TransactionService.Model.Account;
import Banking.TransactionService.Model.Transaction;
import Banking.TransactionService.Model.Transaction.TransactionType;
import Banking.TransactionService.Model.Transaction.TransactionStatus;
import Banking.TransactionService.Repository.AccountRepository;
import Banking.TransactionService.Repository.TransactionRepository;
import Banking.TransactionService.DTO.TransactionMessage;
import Banking.TransactionService.Config.TransferFeeConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    protected TransferFeeConfig transferFeeConfig;

    @Transactional
    public Account deposit(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Optional<Account> optionalAccount = accountRepository.findByAccountNumberWithLock(accountNumber);
        
        if (optionalAccount.isEmpty()) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }

        Account account = optionalAccount.get();
        
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new IllegalStateException("Cannot deposit to inactive or blocked account: " + accountNumber);
        }

        
        Transaction transaction = createTransaction(
            TransactionType.deposit,
            null, 
            account,
            amount,
            BigDecimal.ZERO 
        );
        
        try {
            
            BigDecimal newBalance = account.getBalance().add(amount);
            account.setBalance(newBalance);
            Account updatedAccount = accountRepository.save(account);
            
            
            transaction.setStatus(TransactionStatus.success);
            transactionRepository.save(transaction);
            
            
            sendTransactionMessage(transaction, updatedAccount);
            
            return updatedAccount;
        } catch (Exception e) {

            transaction.setStatus(TransactionStatus.failed);
            transactionRepository.save(transaction);
            throw e;
        }
    }

    @Transactional
    public Account withdraw(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        Optional<Account> optionalAccount = accountRepository.findByAccountNumberWithLock(accountNumber);
        
        if (optionalAccount.isEmpty()) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }

        Account account = optionalAccount.get();
        
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new IllegalStateException("Cannot withdraw from inactive or blocked account: " + accountNumber);
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds in account: " + accountNumber);
        }


        Transaction transaction = createTransaction(
            TransactionType.withdraw,
            account, 
            null, 
            amount,
            BigDecimal.ZERO 
        );
        
        try {
           
            BigDecimal newBalance = account.getBalance().subtract(amount);
            account.setBalance(newBalance);
            Account updatedAccount = accountRepository.save(account);
            
            
            transaction.setStatus(TransactionStatus.success);
            transactionRepository.save(transaction);
            
            
            sendTransactionMessage(transaction, updatedAccount);
            
            return updatedAccount;
        } catch (Exception e) {
            
            transaction.setStatus(TransactionStatus.failed);
            transactionRepository.save(transaction);
            throw e;
        }
    }

    @Transactional
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        // Calculate transfer fee
        BigDecimal fee = calculateTransferFee(amount);
        BigDecimal totalDebit = amount.add(fee);

        Optional<Account> optionalFromAccount = accountRepository.findByAccountNumberWithLock(fromAccountNumber);
        Optional<Account> optionalToAccount = accountRepository.findByAccountNumberWithLock(toAccountNumber);
        
        if (optionalFromAccount.isEmpty()) {
            throw new IllegalArgumentException("Source account not found: " + fromAccountNumber);
        }
        
        if (optionalToAccount.isEmpty()) {
            throw new IllegalArgumentException("Destination account not found: " + toAccountNumber);
        }

        Account fromAccount = optionalFromAccount.get();
        Account toAccount = optionalToAccount.get();
        
        if (fromAccount.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new IllegalStateException("Cannot transfer from inactive or blocked account: " + fromAccountNumber);
        }
        
        if (toAccount.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new IllegalStateException("Cannot transfer to inactive or blocked account: " + toAccountNumber);
        }

        // Check if source account has enough balance for amount + fee
        if (fromAccount.getBalance().compareTo(totalDebit) < 0) {
            throw new IllegalStateException("Insufficient funds in source account for transfer amount plus fee: " + fromAccountNumber);
        }

        // Get bank account for fee collection
        Optional<Account> optionalBankAccount = accountRepository.findByAccountNumberWithLock(transferFeeConfig.getBankAccountNumber());
        if (optionalBankAccount.isEmpty()) {
            throw new IllegalStateException("Bank account for fee collection not found: " + transferFeeConfig.getBankAccountNumber());
        }
        Account bankAccount = optionalBankAccount.get();

        // Create main transfer transaction record
        Transaction transferTransaction = createTransaction(
            TransactionType.transfer,
            fromAccount,
            toAccount,
            amount,
            fee
        );
        
        try {
            // Update account balances for the main transfer
            BigDecimal fromNewBalance = fromAccount.getBalance().subtract(amount);
            fromAccount.setBalance(fromNewBalance);
            
            BigDecimal toNewBalance = toAccount.getBalance().add(amount);
            toAccount.setBalance(toNewBalance);

            // Create fee transaction record
            Transaction feeTransaction = createTransaction(
                TransactionType.fee,
                fromAccount,
                bankAccount,
                fee,
                BigDecimal.ZERO
            );
            
            // Update account balances for the fee
            fromNewBalance = fromAccount.getBalance().subtract(fee);
            fromAccount.setBalance(fromNewBalance);
            
            BigDecimal bankNewBalance = bankAccount.getBalance().add(fee);
            bankAccount.setBalance(bankNewBalance);

            // Save all accounts
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            accountRepository.save(bankAccount);
            
            // Update transaction status to success
            transferTransaction.setStatus(TransactionStatus.success);
            feeTransaction.setStatus(TransactionStatus.success);
            transactionRepository.save(transferTransaction);
            transactionRepository.save(feeTransaction);
            
            // Send messages to RabbitMQ for both transactions
            sendTransactionMessage(transferTransaction, fromAccount);
            sendTransactionMessage(feeTransaction, fromAccount);
            
        } catch (Exception e) {
            // Update transaction status to failed
            transferTransaction.setStatus(TransactionStatus.failed);
            transactionRepository.save(transferTransaction);
            throw e;
        }
    }

    private BigDecimal calculateTransferFee(BigDecimal amount) {
        // Calculate percentage fee
        BigDecimal percentageFee = amount.multiply(transferFeeConfig.getPercentage());
        
        // Apply minimum and maximum constraints
        BigDecimal fee = percentageFee.max(transferFeeConfig.getMinFee());
        fee = fee.min(transferFeeConfig.getMaxFee());
        
        return fee;
    }

    private Transaction createTransaction(TransactionType type, Account accountFrom, 
                                        Account accountTo, BigDecimal amount, BigDecimal fee) {
        Transaction transaction = new Transaction();
        transaction.setTrackingCode(generateTrackingCode());
        transaction.setType(type);
        transaction.setAccountFrom(accountFrom);
        transaction.setAccountTo(accountTo);
        transaction.setAmount(amount);
        transaction.setFee(fee);
        transaction.setStatus(TransactionStatus.pending);
        transaction.setCreatedAt(LocalDateTime.now());
        
        return transactionRepository.save(transaction);
    }

    private String generateTrackingCode() {
        return "TRX-" + LocalDateTime.now().toLocalDate() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void sendTransactionMessage(Transaction transaction, Account account) {
        try {
            TransactionMessage message = new TransactionMessage(
                transaction.getId(),
                transaction.getTrackingCode(),
                transaction.getType().name(),
                transaction.getAccountFrom() != null ? transaction.getAccountFrom().getAccountNumber() : null,
                transaction.getAccountTo() != null ? transaction.getAccountTo().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getFee(),
                transaction.getStatus().name(),
                transaction.getCreatedAt(),
                account.getBalance()
            );
            
            rabbitTemplate.convertAndSend("transaction.queue", message);
            
            System.out.println("Sent transaction message: " + transaction.getTrackingCode());
        } catch (Exception e) {
            System.err.println("Failed to send transaction message: " + e.getMessage());
        }
    }

    public BigDecimal getBalance(String accountNumber) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        
        if (optionalAccount.isEmpty()) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        
        return optionalAccount.get().getBalance();
    }
}