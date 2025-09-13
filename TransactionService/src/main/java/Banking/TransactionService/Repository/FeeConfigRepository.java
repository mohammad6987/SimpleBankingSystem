package Banking.TransactionService.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import Banking.TransactionService.Model.FeeConfig;
import java.math.BigDecimal;
import java.util.List;


public interface FeeConfigRepository extends JpaRepository<FeeConfig , Long> {

    public FeeConfig findByid(Long id);

}
