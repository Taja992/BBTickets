package GUI.model;

import BLL.EventBLL;
import BLL.UserBLL;
import Exceptions.BBExceptions;
import BE.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.HashMap;
import java.util.Map;

public class EventModel {
    private EventBLL eventBLL;
    private UserBLL userBLL;

    private ObservableList<Event> allEvents = FXCollections.observableArrayList();
    private Map<Integer, ObservableList<Event>> eventsForUser = new HashMap<>();

    public EventModel() {
        eventBLL = new EventBLL();
        userBLL = new UserBLL();
    }

    public ObservableList<Event> getAllEvents() throws BBExceptions {
        allEvents.setAll(eventBLL.getAllEvents());
        return allEvents;
    }

    public ObservableList<Event> getEventsForUser(int userId) throws BBExceptions {
        ObservableList<Event> userEvents = eventsForUser.computeIfAbsent(userId, k -> FXCollections.observableArrayList());
        userEvents.setAll(userBLL.getEventsForUser(userId));
        return userEvents;
    }


    public void deleteEvent(int eventId) throws BBExceptions {
        eventBLL.deleteEvent(eventId);
    }

    public void manageEvent(Event event) throws BBExceptions {
        eventBLL.manageEvent(event);
    }

    public int newEvent(Event event) throws BBExceptions {
        return eventBLL.newEvent(event);
    }

    public void assignUserToEvent(int userId, int eventId) throws BBExceptions {
        eventBLL.assignUserToEvent(userId, eventId);
    }

    public void removeUserFromEvent(int userId, int eventId) throws BBExceptions {
        eventBLL.removeUserFromEvent(userId, eventId);
    }

}
