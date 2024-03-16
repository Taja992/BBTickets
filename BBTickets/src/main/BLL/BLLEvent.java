package BLL;

import BE.Event;
import DAL.EventDAO;
import Exceptions.BBExceptions;

public class BLLEvent {

    EventDAO DAO = new EventDAO();

    public void newEvent(Event event) throws BBExceptions {
        DAO.newEvent(event);
    }
}
