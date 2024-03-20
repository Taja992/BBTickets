package BLL;

import DAL.TicketDAO;

public class TicketBLL {

    private TicketDAO DAO = new TicketDAO();

    public void createTicket(String type, int customerId, int eventId, double price){
        DAO.createTicket(type, customerId, eventId, price);
    }

}
