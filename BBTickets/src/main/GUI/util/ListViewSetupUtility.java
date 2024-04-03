package GUI.util;

import BE.Event;
import BE.User;
import Exceptions.BBExceptions;
import GUI.model.EventModel;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class ListViewSetupUtility {


    public static void setupEventListView(ListView<Event> eventListLv, EventModel eventModel) {
        eventListLv.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(item.getEventType()));
                }
            }
        });
        loadEventsToListView(eventListLv, eventModel);
    }


    private static void loadEventsToListView(ListView<Event> eventListLv, EventModel eventModel) {
        try {
            eventListLv.getItems().setAll(eventModel.getAllEvents());
        } catch (BBExceptions e) {
            e.printStackTrace();
            // Handle error
        }
    }

    public static void setupUserListView(ListView<User> userListLv) {
        userListLv.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getUsername());
                }
            }
        });
    }
}
