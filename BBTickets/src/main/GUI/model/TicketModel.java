package GUI.model;

import BLL.TicketBLL;
import BLL.UserBLL;

public class TicketModel {

    private TicketBLL ticketBLL;

    public TicketModel() {
        ticketBLL = new TicketBLL();

    }
    public void createTicket(String type, int customerId, int eventId, double price){
        ticketBLL.createTicket(type, customerId, eventId, price);
    }



}
