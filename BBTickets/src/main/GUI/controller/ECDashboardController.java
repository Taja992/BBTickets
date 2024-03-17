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

        createEventBtn.setOnAction(event -> {
            try {
                // Load createEvent.fxml
                Parent root = FXMLLoader.load(getClass().getResource("/GUI/view/createEvent.fxml"));

                // Create a new stage for the create event screen
                Stage createEventStage = new Stage();
                createEventStage.initStyle(StageStyle.TRANSPARENT);

                // Create a new scene with the loaded parent and set it on the stage
                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                createEventStage.setTitle("Ticket Interface");
                createEventStage.setScene(scene);

                // Get the current stage and close it
                Stage currentStage = (Stage) createEventBtn.getScene().getWindow();
                currentStage.close();

                // Show the create event stage
                createEventStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        createEventBtn.setOnAction(event -> {
            try {
                // Load createEvent.fxml
                Parent root = FXMLLoader.load(getClass().getResource("/GUI/view/createEvent.fxml"));

                // Create a new stage for the create event screen
                Stage createEventStage = new Stage();
                createEventStage.initStyle(StageStyle.TRANSPARENT);

                // Create a new scene with the loaded parent and set it on the stage
                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                createEventStage.setTitle("Create Event");
                createEventStage.setScene(scene);

                // Show the create event stage
                createEventStage.show();
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
}
