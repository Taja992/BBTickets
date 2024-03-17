package GUI.controller;

import BE.Event;
import BE.User;
import BLL.BLLEvent;
import BLL.BLLUser;
import Exceptions.BBExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDateTime;

public class AdminDashboardController {

    @FXML
    private Button logoutBtn;
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
    @FXML
    private TableColumn<User, Integer> typeColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private TableView<User> userList;


    private final BLLEvent bllEvent;
    private final BLLUser bllUser;

    public AdminDashboardController(){
        bllEvent = new BLLEvent();
        bllUser = new BLLUser();
    }
    public void initialize() {
        logOut();
        loadEvents();
        loadUsers();
        setupEventTable();
        setupUserTable();
    }

    public void loadEvents() {
        try {
            // Call getAllEvents from bllEvent and set the result as the items of eventList
            eventList.getItems().setAll(bllEvent.getAllEvents());
        } catch (BBExceptions e) {
            e.printStackTrace();
        }
    }

    public void loadUsers() {
        try {
            // Call getAllEvents from bllEvent and set the result as the items of eventList
            userList.getItems().setAll(bllUser.allUsers());
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

    public void setupUserTable() {
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("user_type"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
    }

    public void logOut(){
        logoutBtn.setOnAction(event -> {
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
                Stage currentStage = (Stage) logoutBtn.getScene().getWindow();
                currentStage.close();

                // Show the login stage
                loginStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }


    public void deleteEvent(ActionEvent actionEvent) throws BBExceptions {
        //gets the selected item from the table and deletes it (does nothing if nothing is selected)
        BE.Event selected = eventList.getSelectionModel().getSelectedItem();
        if(selected != null){
            bllEvent.DeleteEvent(selected.getEventId());
            loadEvents(); //reloads the table so it updates with the item deleted
        }
    }
}