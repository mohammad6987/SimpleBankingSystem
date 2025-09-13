package Banking.TransactionService.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import Banking.TransactionService.Model.Account;

import java.math.BigDecimal;
import java.util.List;


public interface AccountRepository extends JpaRepository<Account , String> {

    public Account findByAccountNumber(String accountNumber);

}
