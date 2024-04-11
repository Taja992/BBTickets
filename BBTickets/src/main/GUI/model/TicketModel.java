package GUI.model;

import BE.Customer;
import BE.Event;
import BE.TicketType;
import BE.Ticket;
import BLL.TicketBLL;
import BLL.UserBLL;
import Exceptions.BBExceptions;

import java.io.IOException;
import java.util.List;

public class TicketModel {

    private TicketBLL ticketBLL;

    public TicketModel() {
        ticketBLL = new TicketBLL();

    }
    public void createTicket(int typeId, int customerId, int eventId, double price, String UUID){
        ticketBLL.createTicket(typeId, customerId, eventId, price, UUID);
    }

    public void createTicket(int typeId, int customerId, int eventId, String UUID){
        ticketBLL.createTicket(typeId, customerId, eventId, UUID);
    }

    public List<TicketType> getAllTypes(){
        return ticketBLL.getAllTypes();
    }
    public void addType(String name) throws BBExceptions {
        ticketBLL.addType(name);
    }

    public void printTicketWithInfo(int width, int height, Customer cust, Event event, String type, double price,String uuid, String fileLocation) throws IOException {
        ticketBLL.printTicketWithInfo(width, height, cust, event, type, price, uuid, fileLocation);
    }

    public String generateUUID(){
        return ticketBLL.generateUUID();
    }

    public boolean doesPDFExist(String filepath){
        return ticketBLL.doesPDFExist(filepath);
    }

    public List<Ticket> getTickets(int customerId, int eventId) {
        return ticketBLL.getTickets(customerId, eventId);
    }

}
