package Banking.CustomerService.Controller;


import Banking.CustomerService.DTO.CustomerUpdateRequest;
import Banking.CustomerService.DTO.CustomerRequest;
import Banking.CustomerService.Model.Customer;

import Banking.CustomerService.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

   
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerRequest customerRequest) {
        try {
            
            Customer customer = customerService.createCustomer(
                customerRequest
            );
            return new ResponseEntity<>(customer, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{nationalId}")
    public ResponseEntity<?> getCustomer(@PathVariable String nationalId) {
        Optional<Customer> customer = customerService.getCustomerByNationalId(nationalId);
        if (customer.isPresent()) {
            return new ResponseEntity<>(customer.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
        }
    }

    
    @PutMapping("/{nationalId}")
    public ResponseEntity<?> updateCustomer(@PathVariable String nationalId, @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        try {
            Customer customer = customerService.updateCustomerField(
                nationalId,customerUpdateRequest.getFieldName() , customerUpdateRequest.getNewValue() , customerUpdateRequest.getAccount_Id()
            );
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    


}