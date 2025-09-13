package Banking.TransactionService.Service;

import Banking.TransactionService.Model.Customer;
import Banking.TransactionService.Model.AccountHistory;
import Banking.TransactionService.Model.Account;
import Banking.TransactionService.Repository.CustomerRepository;
import Banking.TransactionService.Repository.AccountHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private AccountHistoryRepository accountHistoryRepository;

    @Transactional
    public Customer createCustomer(Customer customer, String changedBy) {
  
        if (customerRepository.existsByNationalId(customer.getNationalId())) {
            throw new IllegalArgumentException("Customer with this national ID already exists");
        }
        

        String accountNumber = generateAccountNumber();
        while (customerRepository.existsByAccountNumber(accountNumber)) {
            accountNumber = generateAccountNumber();
        }
        
        customer.setAccountNumber(accountNumber);
        Customer savedCustomer = customerRepository.save(customer);
        
      
        recordAccountHistory(savedCustomer, "ACCOUNT_CREATION", null, null, changedBy);
        
        return savedCustomer;
    }

  
    @Transactional
    public Customer updateCustomer(String accountNumber, Customer updatedCustomer, String changedBy) {
        Optional<Customer> existingCustomerOpt = customerRepository.findByAccountNumber(accountNumber);
        
        if (!existingCustomerOpt.isPresent()) {
            throw new IllegalArgumentException("Customer account not found");
        }
        
        Customer existingCustomer = existingCustomerOpt.get();
        
    
        if (!existingCustomer.getNationalId().equals(updatedCustomer.getNationalId())) {
            throw new IllegalArgumentException("National ID cannot be changed");
        }
        
        
        if (!existingCustomer.getName().equals(updatedCustomer.getName())) {
            recordAccountHistory(existingCustomer, "NAME", existingCustomer.getName(), updatedCustomer.getName(), changedBy);
            existingCustomer.setName(updatedCustomer.getName());
        }
        
        if (!existingCustomer.getDob().equals(updatedCustomer.getDob())) {
            recordAccountHistory(existingCustomer, "DOB", existingCustomer.getDob().toString(), updatedCustomer.getDob().toString(), changedBy);
            existingCustomer.setDob(updatedCustomer.getDob());
        }
        
        if (!existingCustomer.getCustomerType().equals(updatedCustomer.getCustomerType())) {
            recordAccountHistory(existingCustomer, "CUSTOMER_TYPE", existingCustomer.getCustomerType().toString(), updatedCustomer.getCustomerType().toString(), changedBy);
            existingCustomer.setCustomerType(updatedCustomer.getCustomerType());
        }
        
        if (!existingCustomer.getNationality().equals(updatedCustomer.getNationality())) {
            recordAccountHistory(existingCustomer, "NATIONALITY", existingCustomer.getNationality(), updatedCustomer.getNationality(), changedBy);
            existingCustomer.setNationality(updatedCustomer.getNationality());
        }
        
        if (!existingCustomer.getPhone().equals(updatedCustomer.getPhone())) {
            recordAccountHistory(existingCustomer, "PHONE", existingCustomer.getPhone(), updatedCustomer.getPhone(), changedBy);
            existingCustomer.setPhone(updatedCustomer.getPhone());
        }
        
        if (!existingCustomer.getAddress().equals(updatedCustomer.getAddress())) {
            recordAccountHistory(existingCustomer, "ADDRESS", existingCustomer.getAddress(), updatedCustomer.getAddress(), changedBy);
            existingCustomer.setAddress(updatedCustomer.getAddress());
        }
        
        if (!existingCustomer.getPostalCode().equals(updatedCustomer.getPostalCode())) {
            recordAccountHistory(existingCustomer, "POSTAL_CODE", existingCustomer.getPostalCode(), updatedCustomer.getPostalCode(), changedBy);
            existingCustomer.setPostalCode(updatedCustomer.getPostalCode());
        }
        
        if (!existingCustomer.getAccountStatus().equals(updatedCustomer.getAccountStatus())) {
            recordAccountHistory(existingCustomer, "ACCOUNT_STATUS", existingCustomer.getAccountStatus().toString(), updatedCustomer.getAccountStatus().toString(), changedBy);
            existingCustomer.setAccountStatus(updatedCustomer.getAccountStatus());
        }
        
        return customerRepository.save(existingCustomer);
    }

    public Customer getCustomerByAccountNumber(String accountNumber) {
        Optional<Customer> customer = customerRepository.findByAccountNumber(accountNumber);
        return customer.orElseThrow(() -> new IllegalArgumentException("Customer account not found"));
    }


    public Customer getCustomerByNationalId(String nationalId) {
        Optional<Customer> customer = customerRepository.findByNationalId(nationalId);
        return customer.orElseThrow(() -> new IllegalArgumentException("Customer account not found"));
    }

    
    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();
        
        for (int i = 0; i < 14; i++) {
            accountNumber.append(random.nextInt(10));
        }
        
        return accountNumber.toString();
    }

  
    private void recordAccountHistory(Customer customer, String fieldName, String oldValue, String newValue, String changedBy) {
        AccountHistory history = new AccountHistory();
        history.setAccount(customer);
        history.setFieldName(fieldName);
        history.setOldValue(oldValue);
        history.setNewValue(newValue);
        history.setChangedBy(changedBy);
        
        accountHistoryRepository.save(history);
    }
    

    
}