package GUI.controller;

import BE.Event;
import BE.User;
import Exceptions.BBExceptions;
import GUI.model.EventModel;
import GUI.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class RenameAdminDashboardController {
    public BorderPane mainBp;
    public VBox eventListVbox;
    public ListView<BE.Event> eventListLv;
    public ListView<String> userListLv;
    public BorderPane nestedBp;
    public HBox userWindowHbox;
    public VBox eventWindowVbox;
    public HBox bottomHbox;
    public Button createEventBtn;
    public Button logoutBtn;
    private EventModel eventModel;
    private UserModel userModel;
    private int userId;

    public RenameAdminDashboardController() {
        eventModel = new EventModel();
        userModel = new UserModel();
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void initialize() {
        setupEventListView();
        loadUsers();
    }

    public void logoutBtn(ActionEvent actionEvent) {
        try {
            // Load login.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/GUI/view/login.fxml"));

            // Create a new stage for the login screen
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.TRANSPARENT);

            // Create a new scene with the loaded parent and set it on the stage
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            loginStage.setTitle("Ticket Interface");
            loginStage.setScene(scene);

            // Get the current stage and close it
            Stage currentStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

            // Show the login stage
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void assignCoordinator(ActionEvent actionEvent) {
    }

    public void createTicket(ActionEvent actionEvent) {
    }

    public void deleteEvent(ActionEvent actionEvent)  throws BBExceptions {
        //gets the selected item from the table and deletes it (does nothing if nothing is selected)
        BE.Event selected = eventListLv.getSelectionModel().getSelectedItem();
        if (selected != null) {
            eventModel.deleteEvent(selected.getEventId());
            loadEventsToListView(); //reloads the table so it updates with the item deleted
        }
    }

    public void editEvent(ActionEvent actionEvent) {
    }

    public void createEventBtn(ActionEvent actionEvent) {
    }

    private void setupEventListView() {
        // Set the cell factory of the ListView
        eventListLv.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    // Set the text of the cell to the eventType of the Event
                    setText(String.valueOf(item.getEventType()));
                }
            }
        });

        // Load the events into the ListView
        loadEventsToListView();
    }

    private void loadEventsToListView() {
        try {
            // Call getAllEvents from eventModel and set the result as the items of eventListLstV
            eventListLv.getItems().setAll(eventModel.getAllEvents());
        } catch (BBExceptions e) {
            e.printStackTrace();
        }
    }
    private void loadUsers() {
        try {
            List<User> users = userModel.getAllUsers();
            for (User user : users) {
                userListLv.getItems().add(user.getUsername());
            }
        } catch (BBExceptions e) {
            e.printStackTrace();
        }
    }
}
