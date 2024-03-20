package GUI.model;

import BLL.EventBLL;
import BLL.UserBLL;
import Exceptions.BBExceptions;
import BE.Event;
import java.util.List;

public class EventModel {
    private EventBLL eventBLL;
    private UserBLL userBLL;

    public EventModel() {
        eventBLL = new EventBLL();
        userBLL = new UserBLL();
    }

    public List<Event> getAllEvents() throws BBExceptions {
        return eventBLL.getAllEvents();
    }

    public List<Event> getEventsForUser(int userId) throws BBExceptions {
        return userBLL.getEventsForUser(userId);
    }

    public void deleteEvent(int eventId) throws BBExceptions {
        eventBLL.deleteEvent(eventId);
    }

    public void manageEvent(Event event) throws BBExceptions {
        eventBLL.manageEvent(event);
    }

    public void newEvent(Event event) throws BBExceptions {
        eventBLL.newEvent(event);
    }

}
