package Banking.TransactionService.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import Banking.TransactionService.Model.Transaction;
import Banking.TransactionService.Model.Account;
import Banking.TransactionService.Model.AccountHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory , Long>  {

    public List<AccountHistory> findByAccount(Account account);
    
    Page<AccountHistory> findByAccountAndChangedAtBetween(
        Account account, 
        LocalDateTime startTime, 
        LocalDateTime endTime, 
        Pageable pageable
    );
    
}
