package BE;

public class Customer {
    private String customerName;
    private String emailAddress;

    // Constructor
    public Customer(String customerName, String emailAddress) {
        this.customerName = customerName;
        this.emailAddress = emailAddress;
    }

    // Getter and setter methods for customerName
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // Getter and setter methods for emailAddress
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}