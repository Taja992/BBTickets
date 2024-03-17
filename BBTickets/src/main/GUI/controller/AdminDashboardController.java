package GUI.controller;

import BE.Event;
import BLL.BLLEvent;
import BLL.BLLUser;
import Exceptions.BBExceptions;
import javafx.event.ActionEvent;
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
    public Button logoutBtn;
    public TableView<Event> eventList;
    public TableColumn<Event, String> eventTypeColumn;
    public TableColumn<Event, String> eventLocationColumn;
    public TableColumn<Event, LocalDateTime> eventStartTimeColumn;
    public TableColumn<Event, LocalDateTime> eventEndTimeColumn;
    public TableColumn<Event, String> eventNotesColumn;
    public TableColumn<Event, String> locationGuidanceColumn;


    private BLLEvent bllEvent;

    public AdminDashboardController(){
        bllEvent = new BLLEvent();
    }
    public void initialize() {
        logOut();
        setupEventTable();
        loadEvents();
    }

    public void loadEvents() {
        try {
            // Call getAllEvents from bllEvent and set the result as the items of eventList
            eventList.getItems().setAll(bllEvent.getAllEvents());
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
        BE.Event selected = eventList.getSelectionModel().getSelectedItem();
        if(selected != null){
            bllEvent.DeleteEvent(selected.getEventId());
            loadEvents();
        }
    }
}