package GUI.controller;

import BE.Event;
import Exceptions.BBExceptions;
import GUI.model.EventModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.*;
import java.io.IOException;
import java.time.LocalDateTime;

public class ECDashboardController {

    @FXML
    private Button logoutBtn;
    @FXML
    private Button createEventBtn;
    @FXML
    private TableView<Event> eventList;
    @FXML
    private TableColumn<Event, String> eventTypeColumn;
    @FXML
    private TableColumn<Event, String> eventLocationColumn;
    @FXML
    private TableColumn<Event, LocalDateTime> eventStartTimeColumn;
    @FXML
    private TableColumn<Event, LocalDateTime> eventEndTimeColumn;
    @FXML
    private TableColumn<Event, String> eventNotesColumn;
    @FXML
    private TableColumn<Event, String> locationGuidanceColumn;
    private int userId;
    private EventModel eventModel;

    public void setUserId(int userId) {
        this.userId = userId;
        eventTableForSpecificUser();
    }

    public void initialize() {
        eventModel = new EventModel();
        setupLogoutButton();
        setupEventTable();

    }

    private void setupLogoutButton() {
        logoutBtn.setOnAction(event -> {
            try {
                loadNewScene("/GUI/view/login.fxml", logoutBtn);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void refreshTable(){
        eventList.getItems().clear();
        eventTableForSpecificUser();
    }

    private void eventTableForSpecificUser(){
        // Fetch the events for a specific user
        try {
            List<Event> events = eventModel.getEventsForUser(userId);
            // Add the events to the TableView
            eventList.getItems().addAll(events);
        } catch (BBExceptions e) {
            e.printStackTrace();
        }
    }

    public void setupEventTable() {
        eventTypeColumn.setCellValueFactory(new PropertyValueFactory<>("eventType"));
        eventLocationColumn.setCellValueFactory(new PropertyValueFactory<>("eventLocation"));
        eventStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("eventStartTime"));
        eventEndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("eventEndingTime"));
        eventNotesColumn.setCellValueFactory(new PropertyValueFactory<>("eventNotes"));
        locationGuidanceColumn.setCellValueFactory(new PropertyValueFactory<>("locationGuidance"));
    }

    public void deleteEvent(ActionEvent actionEvent) throws BBExceptions {
        Event selected = eventList.getSelectionModel().getSelectedItem();
        if(selected != null){
            System.out.println(selected.getEventType());
            eventModel.deleteEvent(selected.getEventId());
            refreshTable();
        }
    }

    public void createTicket(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/CreateTicketTemp.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    public void editEvent(ActionEvent actionEvent) throws BBExceptions, IOException {
        BE.Event selected = eventList.getSelectionModel().getSelectedItem();
        if(selected != null){
            System.out.println(selected.getEventType());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/manageEvent.fxml"));
            Parent root = loader.load();

            ManageEventController controller = loader.getController();
            controller.setDashboard(this);
            controller.setEvent(selected);


            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
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
            // Load createEvent.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/GUI/view/createEvent.fxml"));

            // Create a new stage for the create event screen
            Stage createEventStage = new Stage();
            createEventStage.initStyle(StageStyle.DECORATED);

            // Create a new scene with the loaded parent and set it on the stage
            Scene scene = new Scene(root);
            createEventStage.setTitle("Create Event");
            createEventStage.setScene(scene);

            // Show the create event stage
            createEventStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
