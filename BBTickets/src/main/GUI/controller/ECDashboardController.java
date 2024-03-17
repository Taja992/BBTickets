package GUI.controller;

import BE.Event;
import BLL.BLLEvent;
import Exceptions.BBExceptions;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;

public class ECDashboardController {

    public Button logoutBtn;
    public Button createEventBtn;
    @FXML
    private ListView eventList;

    private BLLEvent bllEvent;

    public void initialize() {
        setupLogoutButton();
        setupCreateEventButton();
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

    public void deleteEvent(ActionEvent actionEvent) throws BBExceptions {
        BE.Event selected = (Event) eventList.getSelectionModel().getSelectedItem();
        if(selected != null){
            bllEvent.DeleteEvent(selected.getEventId());
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

    public void editEvent(ActionEvent actionEvent) {
        BE.Event selected = (Event) eventList.getSelectionModel().getSelectedItem();
        if(selected != null){
            bllEvent.manageEvent(selected);
        }
    }

    private void setupCreateEventButton() {
        createEventBtn.setOnAction(event -> {
            try {
                loadNewScene("/GUI/view/createEvent.fxml", createEventBtn);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
}
