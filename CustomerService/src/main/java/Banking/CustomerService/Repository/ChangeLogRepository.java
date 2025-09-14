package Banking.CustomerService.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import Banking.CustomerService.Model.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ChangeLogRepository extends JpaRepository<ChangeLog , Long>  {

    public List<ChangeLog> findByAccount(Customer customer);
    
    Page<ChangeLog> findByAccountAndChangedAtBetween(
        Customer customer, 
        LocalDateTime startTime, 
        LocalDateTime endTime, 
        Pageable pageable
    );
    
}