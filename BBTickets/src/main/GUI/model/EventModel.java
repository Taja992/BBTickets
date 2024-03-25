package GUI.model;

import BLL.EventBLL;
import BLL.UserBLL;
import Exceptions.BBExceptions;
import BE.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;
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
        if (allEvents.isEmpty()) {
            allEvents.setAll(eventBLL.getAllEvents());
        }
        return allEvents;
    }

    public ObservableList<Event> getEventsForUser(int userId) throws BBExceptions {
        if (!eventsForUser.containsKey(userId)) {
            eventsForUser.put(userId, FXCollections.observableArrayList(userBLL.getEventsForUser(userId)));
        }
        return eventsForUser.get(userId);
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

}
