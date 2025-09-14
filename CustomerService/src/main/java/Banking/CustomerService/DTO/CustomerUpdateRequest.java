package Banking.CustomerService.DTO;

import java.time.LocalDate;

public class CustomerUpdateRequest {
    private String nationalId;
    private String account_Id;
    private String fieldName;
   public String getAccount_Id() {
    return account_Id;
}

public void setAccount_Id(String account_Id) {
    this.account_Id = account_Id;
}
 private Object newValue;


    public CustomerUpdateRequest() {}
    
    public CustomerUpdateRequest(String nationalId, String fieldName, Object newValue, String account_Id) {
        this.nationalId = nationalId;
        this.fieldName = fieldName;
        this.newValue = newValue;
        this.account_Id = account_Id;
    }
    

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }
    
    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }
    
    public Object getNewValue() { return newValue; }
    public void setNewValue(Object newValue) { this.newValue = newValue; }
}