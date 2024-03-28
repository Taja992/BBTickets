package GUI.controller;

import BE.Event;
import Exceptions.BBExceptions;
import GUI.model.EventModel;
import GUI.model.UserModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.time.LocalDateTime;

public class EcDashboardController {

    public VBox eventWindowVbox;
    public BorderPane mainBp;
    public VBox eventListVbox;
    public BorderPane nestedBp;
    public HBox userWindowHbox;
    public HBox bottomHbox;
    public Button createEventBtn;
    public Button closeBtn;
    public Label eventTypeLbl;
    public Label eventLocationLbl;
    public Label eventStartLbl;
    public Label eventEndLbl;
    public Label eventNotesLbl;
    public Label eventDirLbl;
    public ListView<BE.Event> eventListLv;
    @FXML
    private Button logoutBtn;

    private int userId;
    private EventModel eventModel;
    private UserModel userModel;
    private EventHelper eventHelper;

    public void setUserId(int userId) {
        this.userId = userId;
        eventListForSpecificUser();
    }

    public EcDashboardController() {
        eventModel = new EventModel();
        userModel = UserModel.getInstance();
    }

    public void initialize() {
        this.eventHelper = new EventHelper(eventListLv, userWindowHbox, userModel, eventModel, eventTypeLbl, eventLocationLbl, eventStartLbl, eventEndLbl, eventNotesLbl, eventDirLbl);
        eventModel = new EventModel();
        setupLogoutButton();
        setupEventList();
        eventHelper.eventListObserver();
    }

    private void setupLogoutButton() {
        logoutBtn.setOnAction(this::handleLogoutButtonClick);
    }

    @FXML
    private void handleLogoutButtonClick(ActionEvent event) {
        try {
            loadNewScene("/GUI/view/login.fxml", logoutBtn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshTable(){
        eventListLv.getItems().clear();
        eventListForSpecificUser();
    }

    private void eventListForSpecificUser(){
        // Fetch the events for a specific user
        try {
            ObservableList<Event> events = eventModel.getEventsForUser(userId);
            // Add the events to the ListView
            eventListLv.setItems(events);
        } catch (BBExceptions e) {
            showErrorDialog("Event Fetch Error", "Failed to fetch events for the user.");
        }
    }

    public void setupEventList() {
        eventListLv.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);

                if (empty || event == null) {
                    setText(null);
                } else {
                    // Set the text of the cell to the eventType of the Event
                    setText(String.valueOf(event.getEventType()));
                }
            }
        });
    }

    public void deleteEvent(ActionEvent actionEvent) {
        Event selected = eventListLv.getSelectionModel().getSelectedItem();
        if(selected != null){
            try {
                eventModel.deleteEvent(selected.getEventId());
                refreshTable();
            } catch (BBExceptions e) {
                showErrorDialog("Delete Error", "Failed to delete the event.");
            }
        }
    }

    public void createTicket(ActionEvent actionEvent) {
        if(eventListLv.getSelectionModel().getSelectedItem() != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/createTicket.fxml"));
                Parent root = loader.load();

                CreateTicketController control = loader.getController();
                control.setEvent(eventListLv.getSelectionModel().getSelectedItem());

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                showErrorDialog("Create Ticket Error", "Failed to create a ticket.");
            }
        } else {
            showErrorDialog("Selection Error", "Please select an event to create a ticket.");
        }
    }


    public void editEvent(ActionEvent actionEvent) {
        BE.Event selected = eventListLv.getSelectionModel().getSelectedItem();
        if(selected != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/manageEvent.fxml"));
                Parent root = loader.load();

                ManageEventController controller = loader.getController();
                controller.setDashboard(this);
                controller.setEvent(selected);

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                showErrorDialog("Edit Event Error", "Failed to edit the event.");
            }
        }
    }


    private void loadNewScene(String fxmlPath, Button button) throws IOException {
        // Load fxml file
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));

        // Create a new stage for the new screen
        Stage newStage = new Stage();
        newStage.initStyle(StageStyle.TRANSPARENT);

        // Create a new scene with the loaded parent and set it on the stage
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        newStage.setTitle("Ticket Interface");
        newStage.setScene(scene);

        // Get the current stage and close it
        Stage currentStage = (Stage) button.getScene().getWindow();
        currentStage.close();

        // Show the new stage
        newStage.show();
    }

    public void createEventBtn(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/createEvent.fxml"));
            Parent root = loader.load();

            CreateEvent controller = loader.getController();
            controller.setUserId(userId);
            controller.setRenameThisEcController(this);

            Stage createEventStage = new Stage();
            createEventStage.initStyle(StageStyle.DECORATED);

            Scene scene = new Scene(root);
            createEventStage.setTitle("Create Event");
            createEventStage.setScene(scene);

            createEventStage.show();
        } catch (IOException e) {
            showErrorDialog("Create Event Error", "Failed to create an event.");
        }
    }


    public void assignCoordinator(ActionEvent actionEvent) {
    }

    public void logoutBtn(ActionEvent actionEvent) {
    }

    private void showErrorDialog(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
