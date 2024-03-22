package GUI.controller;

import BE.Event;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class DragAndDrop {

    private ListView<String> userListLv;
    private ListView<Event> eventListLv;

    public DragAndDrop(ListView<String> userListLv, ListView<Event> eventListLv) {
        this.userListLv = userListLv;
        this.eventListLv = eventListLv;
        setupUserListDrag();
        setupEventListDragOver();
        setupEventListDragDropped();
    }

    private void setupUserListDrag() {
        userListLv.setOnDragDetected(event -> {
            String selectedUser = userListLv.getSelectionModel().getSelectedItem();

            if (selectedUser != null) {
                Dragboard db = userListLv.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(selectedUser);
                db.setContent(content);
                event.consume();
            }
        });
    }

    private void setupEventListDragOver() {
        eventListLv.setOnDragOver(event -> {
            if (event.getGestureSource() != eventListLv && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    }

    private void setupEventListDragDropped() {
        eventListLv.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                Event selectedEvent = eventListLv.getSelectionModel().getSelectedItem();
                String user = db.getString();

                // TODO: Assign the user to the event

                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }
}
