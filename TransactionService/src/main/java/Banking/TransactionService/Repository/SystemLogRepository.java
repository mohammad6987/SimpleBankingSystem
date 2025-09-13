package Banking.TransactionService.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import Banking.TransactionService.Model.SystemLog;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {
    
    List<SystemLog> findByEventType(String eventType);

    Page<SystemLog> findByEventType(String eventType, Pageable pageable);
    
    List<SystemLog> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    Page<SystemLog> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    List<SystemLog> findByEventTypeAndCreatedAtBetween(String eventType, LocalDateTime startDate, LocalDateTime endDate);
    
    List<SystemLog> findByDescriptionContainingIgnoreCase(String searchTerm);
    
    List<SystemLog> findByEventTypeAndDescriptionContainingIgnoreCase(String eventType, String searchTerm);
    
    List<SystemLog> findTop10ByOrderByCreatedAtDesc();
    
    Long countByEventType(String eventType);
    
    Long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}