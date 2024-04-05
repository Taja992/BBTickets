package GUI.model;

import BE.Customer;
import BE.Event;
import BLL.TicketBLL;
import BLL.UserBLL;

import java.io.IOException;

public class TicketModel {

    private TicketBLL ticketBLL;

    public TicketModel() {
        ticketBLL = new TicketBLL();

    }
    public void createTicket(String type, int customerId, int eventId, double price, String UUID){
        ticketBLL.createTicket(type, customerId, eventId, price, UUID);
    }

    public void createTicket(String type, int customerId, int eventId, String UUID){
        ticketBLL.createTicket(type, customerId, eventId, UUID);
    }

    public void printTicketWithInfo(int width, int height, Customer cust, Event event, String type, double price, String fileLocation) throws IOException {
        ticketBLL.printTicketWithInfo(width, height, cust, event, type, price, fileLocation);
    }

    public String generateUUID(){
        return ticketBLL.generateUUID();
    }



}
