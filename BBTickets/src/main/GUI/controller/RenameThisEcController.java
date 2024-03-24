package GUI.controller;

import BE.Event;
import Exceptions.BBExceptions;
import GUI.model.EventModel;
import GUI.model.UserModel;
import javafx.collections.FXCollections;
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
import java.util.*;
import java.io.IOException;
import java.time.LocalDateTime;

public class RenameThisEcController {

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

    public RenameThisEcController() {
        eventModel = new EventModel();
        userModel = new UserModel();}

    public void initialize() {
        this.eventHelper = new EventHelper(eventListLv, userWindowHbox, userModel, eventTypeLbl, eventLocationLbl, eventStartLbl, eventEndLbl, eventNotesLbl, eventDirLbl);
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
            List<Event> events = eventModel.getEventsForUser(userId);
            // Add the events to the ListView
            ObservableList<Event> observableList = FXCollections.observableArrayList(events);
            eventListLv.setItems(observableList);
        } catch (BBExceptions e) {
            e.printStackTrace();
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
    
    public void deleteEvent(ActionEvent actionEvent) throws BBExceptions {
        Event selected = eventListLv.getSelectionModel().getSelectedItem();
        if(selected != null){
            System.out.println(selected.getEventType());
            eventModel.deleteEvent(selected.getEventId());
            refreshTable();
        }
    }

    public void createTicket(ActionEvent actionEvent) throws IOException {
        if(eventListLv.getSelectionModel().getSelectedItem() != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/createTicket.fxml"));
            Parent root = loader.load();

            CreateTicketController control = loader.getController();
            control.setEvent(eventListLv.getSelectionModel().getSelectedItem());

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } else {
            //maybe we could print some error screen here
        }

    }

    public void editEvent(ActionEvent actionEvent) throws BBExceptions, IOException {
        BE.Event selected = eventListLv.getSelectionModel().getSelectedItem();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/createEvent.fxml"));
            Parent root = loader.load();

            // Get the controller and set the user ID
            CreateEvent controller = loader.getController();
            controller.setUserId(userId);
            controller.setRenameThisEcController(this);

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

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy  '⏰'HH:mm");
        return formatter.format(dateTime);
    }

    public void assignCoordinator(ActionEvent actionEvent) {
    }

    public void logoutBtn(ActionEvent actionEvent) {
    }
}
