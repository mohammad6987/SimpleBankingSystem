package com.example.transaction_service.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.transaction_service.models.Account;
public interface AccountRepo extends JpaRepository<Account, String> {
    List<Account> findByCustomerId(Long customerId);
}