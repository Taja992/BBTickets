package GUI.controller;

import BE.Event;
import BE.User;
import Exceptions.BBExceptions;
import GUI.model.EventModel;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;

public class DragAndDrop {

    private ListView<User> userListLv;
    private ListView<Event> eventListLv;
    private HBox userWindowHbox;
    private EventModel eventModel;
    private EventHelper eventHelper;

    public DragAndDrop(ListView<User> userListLv, ListView<Event> eventListLv, HBox userWindowHbox, EventHelper eventHelper) {
        this.userListLv = userListLv;
        this.eventListLv = eventListLv;
        this.userWindowHbox = userWindowHbox;
        this.eventHelper = eventHelper;
        setupUserListDrag();
        setupUserWindowDragOver();
        setupUserWindowDragDropped();
        eventModel = new EventModel();
    }

    private void setupUserListDrag() {
        userListLv.setOnDragDetected(event -> {
            User selectedUser = userListLv.getSelectionModel().getSelectedItem();

            if (selectedUser != null) {
                Dragboard db = userListLv.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(String.valueOf(selectedUser));
                db.setContent(content);
                event.consume();
            }
        });
    }

    private void setupUserWindowDragOver() {
        userWindowHbox.setOnDragOver(event -> {
            if (event.getGestureSource() != userWindowHbox && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    }

    private void setupUserWindowDragDropped() {
        userWindowHbox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                Event selectedEvent = eventListLv.getSelectionModel().getSelectedItem();
                User selectedUser = userListLv.getSelectionModel().getSelectedItem();
                int userId = selectedUser.getUserId();
                int eventId = selectedEvent.getEventId();

                try {
                    eventModel.assignUserToEvent(userId, eventId);
                    eventHelper.refreshUserWindowHbox(selectedEvent);
                } catch (BBExceptions e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("A user can only be assigned to an event once.");
                    alert.showAndWait();
                    throw new RuntimeException(e);
                }

                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }
}
