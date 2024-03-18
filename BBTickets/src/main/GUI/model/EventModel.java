package GUI.model;

import BLL.BLLEvent;
import BLL.BLLUser;
import Exceptions.BBExceptions;
import BE.Event;
import java.util.List;

public class EventModel {
    private BLLEvent bllEvent;
    private BLLUser bllUser;

    public EventModel() {
        bllEvent = new BLLEvent();
        bllUser = new BLLUser();
    }

    public List<Event> getAllEvents() throws BBExceptions {
        return bllEvent.getAllEvents();
    }

    public List<Event> getEventsForUser(int userId) throws BBExceptions {
        return bllUser.getEventsForUser(userId);
    }

    public void deleteEvent(int eventId) throws BBExceptions {
        bllEvent.deleteEvent(eventId);
    }

    public void manageEvent(Event event) throws BBExceptions {
        bllEvent.manageEvent(event);
    }

}
