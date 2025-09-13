package Banking.TransactionService.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import Banking.TransactionService.Model.Transaction;
import Banking.TransactionService.Model.Account;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountFrom(Account account);
    List<Transaction> findByAccountTo(Account account);
    Transaction findByTrackingCode(String trackingCode);
    Page<Transaction> findByAccountFromOrAccountTo(Account accountFrom, Account accountTo, Pageable pageable);


}