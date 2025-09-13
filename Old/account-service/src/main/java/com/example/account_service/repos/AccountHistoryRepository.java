package com.example.account_service.repositories;

import com.example.account_service.models.AccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Long> {
    List<AccountHistory> findByAccountNumberOrderByChangedAtDesc(Long accountNumber);
}
