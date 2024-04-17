package BE;

public class Ticket {
    private int ticketId;
    private String type;
    private int customerId;
    private int eventId;
    private double price;
    private String uuid;
    private String customerName;

    // Getters
    public int getTicketId() {
        return ticketId;
    }

    public String getType() {
        return type;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getEventId() {
        return eventId;
    }

    public double getPrice() {
        return price;
    }

    public String getUuid() {
        return uuid;
    }

    // Setters
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }
}