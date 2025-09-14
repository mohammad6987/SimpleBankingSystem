package Banking.CustomerService.DTO;

public class AccountCreationMessage {
    private String nationalId;
    private String accountNumber;
    private String customerName;
    private String customerType;
    

    public AccountCreationMessage() {}
    
    public AccountCreationMessage(String nationalId, String accountNumber, 
                                 String customerName, String customerType) {
        this.nationalId = nationalId;
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.customerType = customerType;
    }
    

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getCustomerType() { return customerType; }
    public void setCustomerType(String customerType) { this.customerType = customerType; }
}