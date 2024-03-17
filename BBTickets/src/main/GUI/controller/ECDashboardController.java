package GUI.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;

public class ECDashboardController {

    public Button logoutBtn;
    public Button createEventBtn;

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