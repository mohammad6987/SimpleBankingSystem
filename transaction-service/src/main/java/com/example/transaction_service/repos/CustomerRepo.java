package com.example.transaction_service.repos;

import java.util.Optional;

import com.example.transaction_service.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo  extends JpaRepository<Customer, Long>{
    Optional<Customer> findByNationalId(String nationalId);
}
