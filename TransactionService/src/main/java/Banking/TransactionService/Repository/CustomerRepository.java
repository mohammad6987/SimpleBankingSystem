package Banking.TransactionService.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import Banking.TransactionService.Model.Customer;

import java.math.BigDecimal;
import java.util.List;


public interface CustomerRepository extends JpaRepository<Customer , String> {

    public Customer findBynationalId(String nationalid);
    public Page<Customer> findByname(String name , Pageable pageable);
    public Customer findByaccountNumber(String account_number);

}
