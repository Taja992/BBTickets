package BE;

public class Customer {
    private String customerName;
    private String customerEmail;

    // Constructor
    public Customer(String customerName, String customerEmail) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setEmailAddress(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}