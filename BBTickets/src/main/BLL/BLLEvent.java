package BLL;

import BE.Event;
import DAL.EventDAO;
import Exceptions.BBExceptions;

import java.util.List;

public class BLLEvent {

    EventDAO DAO = new EventDAO();

    public void newEvent(Event event) throws BBExceptions {
        DAO.newEvent(event);
    }

    public List<Event> getAllEvents() throws BBExceptions {
        return DAO.getAllEvents();
    }
}
