package Banking.TransactionService.Controller;

import Banking.TransactionService.DTO.TransactionRequest;
import Banking.TransactionService.DTO.TransferRequest;
import Banking.TransactionService.DTO.TransactionResponse;
import Banking.TransactionService.Model.Account;
import Banking.TransactionService.Model.Transaction;
import Banking.TransactionService.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody TransactionRequest request) {
        try {
            Transaction transaction = transactionService.deposit(request.getAccountNumber(), request.getAmount());
            
   
            BigDecimal balance = transactionService.getBalance(request.getAccountNumber());
            
            TransactionResponse response = new TransactionResponse(
                transaction.getId(),
                transaction.getTrackingCode(),
                transaction.getType().name(),
                transaction.getAccountFrom() != null ? transaction.getAccountFrom().getAccountNumber() : null,
                transaction.getAccountTo() != null ? transaction.getAccountTo().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getFee(),
                transaction.getStatus().name(),
                transaction.getCreatedAt(),
                balance
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody TransactionRequest request) {
        try {
            Transaction transaction = transactionService.withdraw(request.getAccountNumber(), request.getAmount());
            
       
            BigDecimal balance = transactionService.getBalance(request.getAccountNumber());
            
            TransactionResponse response = new TransactionResponse(
                transaction.getId(),
                transaction.getTrackingCode(),
                transaction.getType().name(),
                transaction.getAccountFrom() != null ? transaction.getAccountFrom().getAccountNumber() : null,
                transaction.getAccountTo() != null ? transaction.getAccountTo().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getFee(),
                transaction.getStatus().name(),
                transaction.getCreatedAt(),
                balance
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        try {
            Transaction transaction = transactionService.transfer(
                request.getFromAccountNumber(),
                request.getToAccountNumber(),
                request.getAmount()
            );
    
            BigDecimal balance = transactionService.getBalance(request.getFromAccountNumber());
            
            TransactionResponse response = new TransactionResponse(
                transaction.getId(),
                transaction.getTrackingCode(),
                transaction.getType().name(),
                transaction.getAccountFrom() != null ? transaction.getAccountFrom().getAccountNumber() : null,
                transaction.getAccountTo() != null ? transaction.getAccountTo().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getFee(),
                transaction.getStatus().name(),
                transaction.getCreatedAt(),
                balance
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<?> getBalance(@PathVariable String accountNumber) {
        try {
            BigDecimal balance = transactionService.getBalance(accountNumber);
            return new ResponseEntity<>(balance, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<?> getTransactionHistory(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        try {
            Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
            
            Page<Transaction> transactions = transactionService.getTransactionHistory(accountNumber, pageable);
            
            Page<TransactionResponse> response = transactions.map(transaction -> 
                new TransactionResponse(
                    transaction.getId(),
                    transaction.getTrackingCode(),
                    transaction.getType().name(),
                    transaction.getAccountFrom() != null ? transaction.getAccountFrom().getAccountNumber() : null,
                    transaction.getAccountTo() != null ? transaction.getAccountTo().getAccountNumber() : null,
                    transaction.getAmount(),
                    transaction.getFee(),
                    transaction.getStatus().name(),
                    transaction.getCreatedAt(),
                    null
                )
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{trackingCode}")
    public ResponseEntity<?> getTransactionByTrackingCode(@PathVariable String trackingCode) {
        try {
            Transaction transaction = transactionService.getTransactionByTrackingCode(trackingCode);
            
            if (transaction == null) {
                return new ResponseEntity<>("Transaction not found", HttpStatus.NOT_FOUND);
            }
            
            TransactionResponse response = new TransactionResponse(
                transaction.getId(),
                transaction.getTrackingCode(),
                transaction.getType().name(),
                transaction.getAccountFrom() != null ? transaction.getAccountFrom().getAccountNumber() : null,
                transaction.getAccountTo() != null ? transaction.getAccountTo().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getFee(),
                transaction.getStatus().name(),
                transaction.getCreatedAt(),
                null
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}