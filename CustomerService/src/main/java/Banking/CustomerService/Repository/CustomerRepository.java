package Banking.CustomerService.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import Banking.CustomerService.Model.Customer;
import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;


public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByAccountNumber(String accountNumber);
    Optional<Customer> findByNationalId(String nationalId);
    boolean existsByNationalId(String nationalId);
    boolean existsByAccountNumber(String accountNumber);
}
