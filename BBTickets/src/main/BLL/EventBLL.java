package BLL;

import BE.Event;
import DAL.EventDAO;
import Exceptions.BBExceptions;

import java.util.List;

public class EventBLL {

    EventDAO DAO = new EventDAO();

    public int newEvent(Event event) throws BBExceptions {
        return DAO.newEvent(event);
    }

    public void deleteEvent(int Id) throws BBExceptions {
        DAO.deleteEvent(Id);
    }

    public void manageEvent(Event event) throws BBExceptions {
        DAO.manageEvent(event);
    }

    public List<Event> getAllEvents() throws BBExceptions {
        return DAO.getAllEvents();
    }

    public void assignUserToEvent(int userId, int eventId) throws BBExceptions {
        DAO.assignUserToEvent(userId, eventId);
    }

    public void removeUserFromEvent(int userId, int eventId) throws BBExceptions {
        DAO.removeUserFromEvent(userId, eventId);
    }
}
