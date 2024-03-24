package BE;

public class Customer {

    private int custId;
    private String customerName;
    private String customerEmail;

    // Constructors
    public Customer(String customerName, String customerEmail) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }
    public Customer(int custId, String customerName, String customerEmail) {
        this.custId = custId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }

    public int getCustId() {
        return custId;
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

    //@Override
    public String toString(){
        return customerName + ", " + customerEmail;
    }
}