package com.example.transaction_service.repos;
import com.example.transaction_service.models.FeeConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeRepo extends JpaRepository<FeeConfig, Long>{

}
