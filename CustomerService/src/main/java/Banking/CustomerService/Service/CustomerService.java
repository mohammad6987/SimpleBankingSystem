package Banking.CustomerService.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Banking.CustomerService.DTO.CustomerRequest;
import Banking.CustomerService.Model.ChangeLog;
import Banking.CustomerService.Model.Customer;
import Banking.CustomerService.Model.Customer.CustomerType;
import Banking.CustomerService.Repository.ChangeLogRepository;
import Banking.CustomerService.Repository.CustomerRepository;
import jakarta.transaction.Transactional;

@Service
public class CustomerService {
    

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ChangeLogRepository changeLogRepository;


    public Optional<Customer> getCustomerByNationalId(String nationalId) {
        return customerRepository.findByNationalId(nationalId);
    }
    
    @Transactional
    public Customer createCustomer(CustomerRequest cr){


        if (customerRepository.existsByNationalId(cr.getNationalId())){
            throw new IllegalArgumentException("Customer with national ID " + cr.getNationalId() + " already exists");
        }
        if (customerRepository.existsByAccountNumber(cr.getAccount_number())){
            throw new IllegalArgumentException("Account with account_number " + cr.getAccount_number() + " already exists");
        }
        Customer customer = new Customer();
        customer.setName(cr.getName());
        customer.setNationalId(cr.getNationalId());
        customer.setCustomerType(CustomerType.valueOf(cr.getCustomerType()));
        customer.setDob(cr.getDob());
        customer.setNationality(cr.getNationality());
        customer.setPhone(cr.getPhone());
        customer.setAddress(cr.getAddress());
        customer.setPostalCode(cr.getPostalCode());



        // adding accont in transaction service




        return customerRepository.save(customer);
    }



     @Transactional
    public Customer updateCustomerField(String nationalId, String fieldName, Object newValue , String accountID) {
        Optional<Customer> optionalCustomer = customerRepository.findByNationalId(nationalId);
        
        if (optionalCustomer.isEmpty()) {
            throw new IllegalArgumentException("Customer with national ID " + nationalId + " not found");
        }
        
        Customer customer = optionalCustomer.get();

        if( !customer.getAccountNumber().equals(accountID)){
            throw new IllegalArgumentException("Mismatch between National ID and Account ID");
        }
        String oldValue = null;
        
        // Update the specific field and get the old value
        switch (fieldName.toLowerCase()) {
            case "name":
                oldValue = customer.getName();
                customer.setName((String) newValue);
                break;
            case "customertype":
                oldValue = customer.getCustomerType().name();
                customer.setCustomerType(CustomerType.valueOf((String) newValue));
                break;
            case "dob":
                oldValue = customer.getDob().toString();
                customer.setDob((java.time.LocalDate) newValue);
                break;
            case "nationality":
                oldValue = customer.getNationality();
                customer.setNationality((String) newValue);
                break;
            case "phone":
                oldValue = customer.getPhone();
                customer.setPhone((String) newValue);
                break;
            case "address":
                oldValue = customer.getAddress();
                customer.setAddress((String) newValue);
                break;
            case "postalcode":
                oldValue = customer.getPostalCode();
                customer.setPostalCode((String) newValue);
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
        

        Customer updatedCustomer = customerRepository.save(customer);
        
        ChangeLog changeLog = new ChangeLog();
        changeLog.setAccount(updatedCustomer);
        changeLog.setOldValue(oldValue);
        changeLog.setNewValue(newValue.toString());
        changeLog.setChangedAt(LocalDateTime.now());
        
        changeLogRepository.save(changeLog);
        
        return updatedCustomer;
    }


    
}
