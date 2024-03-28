package GUI.controller;

import BE.Event;
import BE.User;
import Exceptions.BBExceptions;
import GUI.model.EventModel;
import GUI.model.UserModel;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class EventHelper {
    private ListView<Event> eventListLv;
    private HBox userWindowHbox;
    private UserModel userModel;
    private EventModel eventModel;
    private Label eventTypeLbl;
    private Label eventLocationLbl;
    private Label eventStartLbl;
    private Label eventEndLbl;
    private Label eventNotesLbl;
    private Label eventDirLbl;

    public EventHelper(ListView<BE.Event> eventListLv, HBox userWindowHbox, UserModel userModel, EventModel eventModel, Label eventTypeLbl, Label eventLocationLbl, Label eventStartLbl, Label eventEndLbl, Label eventNotesLbl, Label eventDirLbl) {
        this.eventListLv = eventListLv;
        this.userWindowHbox = userWindowHbox;
        this.userModel = userModel;
        this.eventModel = eventModel;
        this.eventTypeLbl = eventTypeLbl;
        this.eventLocationLbl = eventLocationLbl;
        this.eventStartLbl = eventStartLbl;
        this.eventEndLbl = eventEndLbl;
        this.eventNotesLbl = eventNotesLbl;
        this.eventDirLbl = eventDirLbl;
    }


    //This uses the selected event and shows the info on the main event window
    private void eventInfo(Event event){
        Event selected = eventListLv.getSelectionModel().getSelectedItem();
        eventTypeLbl.setText(selected.getEventType());
        eventLocationLbl.setText(selected.getEventLocation());
        eventStartLbl.setText(formatDateTime(selected.getEventStartTime()));
        eventEndLbl.setText(formatDateTime(selected.getEventEndingTime()));
        eventNotesLbl.setText(selected.getEventNotes());
        eventDirLbl.setText(selected.getLocationGuidance());
    }

    //This watches the event list for what has been selected
    public void eventListObserver() {
        eventListLv.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleEventSelection(newValue);
            }
        });
    }

    //This Applies the event to the eventInfo as well as populates the userbox showing which
    //users are responsible for the event
    private void handleEventSelection(Event newValue) {
        eventInfo(newValue);
        userWindowHbox.getChildren().clear();

        try {
            ObservableList<User> users = userModel.getUsersForEvent(newValue.getEventId());
            for (User user : users) {
                addUserToHbox(user);
            }
        } catch (BBExceptions | URISyntaxException e) {
            showErrorDialog("Failed to handle event selection.");
        }
    }

    //This is just styling for the top box it creates a Vbox for each user, adds a circle and label containing their name
    private void addUserToHbox(User user) throws URISyntaxException {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Circle circle = createCircleForUser();
        Label label = createLabelForUser(user);
        vbox.getChildren().addAll(circle, label);
        rightClickMenu(circle, user);
        userWindowHbox.getChildren().add(vbox);
    }

    private void rightClickMenu(Circle circle, User user) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem removeUser = new MenuItem("Remove User");
        removeUser.setOnAction(e -> {
            try {
                Event selectedEvent = eventListLv.getSelectionModel().getSelectedItem();
                eventModel.removeUserFromEvent(user.getUserId(), selectedEvent.getEventId());
                refreshUserWindowHbox(selectedEvent);
            } catch (BBExceptions ex) {
                showErrorDialog("Failed to remove user from event.");
            }
        });
        contextMenu.getItems().add(removeUser);

        circle.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(circle, e.getScreenX(), e.getScreenY());
            }
        });
    }

    //These adds the profile pic(random image atm) to the user
    private Circle createCircleForUser() throws URISyntaxException {
        Circle circle = new Circle(40);
        String imagePath = getRandomImagePath();
        if (imagePath != null) {
            Image image = new Image(imagePath, 80, 80, false, true);
            ImagePattern imagePattern = new ImagePattern(image);
            circle.setFill(imagePattern);
        } else {
            circle.setFill(Color.TEAL);
        }
        return circle;
    }

    //This is the formatting for the username label
    private Label createLabelForUser(User user) {
        Label label = new Label(user.getUsername());
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        return label;
    }


    //This gets the contents of the userImages folder inside the resources folder
    //Can be later swapped with a way to link profile pic
    private String getRandomImagePath() throws URISyntaxException {
        URL url = getClass().getClassLoader().getResource("/userImages");
        if (url == null) {
            return null;
        }

        File folder = new File(url.toURI());
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null || listOfFiles.length == 0) {
            return null; // Return null if no files are found
        }

        //returns a random photo file string to apply
        int randomIndex = new Random().nextInt(listOfFiles.length);
        return listOfFiles[randomIndex].toURI().toString();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy  '⏰'HH:mm");
        return formatter.format(dateTime);
    }

    public void refreshUserWindowHbox(Event event) {
        userWindowHbox.getChildren().clear();

        try {
            ObservableList<User> users = userModel.getUsersForEvent(event.getEventId());
            for (User user : users) {
                addUserToHbox(user);
            }
        } catch (BBExceptions | URISyntaxException e) {
            showErrorDialog("Failed to refresh user window.");
        }
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
