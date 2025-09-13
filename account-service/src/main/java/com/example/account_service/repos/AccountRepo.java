package com.example.account_service.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.account_service.models.Account;
public interface AccountRepo extends JpaRepository<Account, String> {
    List<Account> findByCustomerId(String customerId);
}