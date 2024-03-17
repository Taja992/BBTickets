package BLL;

import BE.Event;
import DAL.EventDAO;
import Exceptions.BBExceptions;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.util.List;

public class BLLEvent {

    EventDAO DAO = new EventDAO();

    public void newEvent(Event event) throws BBExceptions {
        DAO.newEvent(event);
    }

    public void DeleteEvent(int Id) throws BBExceptions {
        DAO.deleteEvent(Id);
    }

    public void manageEvent(Event event) {
        DAO.manageEvent(event);
    }
    public List<Event> getAllEvents() throws BBExceptions {
        return DAO.getAllEvents();
    }
}
