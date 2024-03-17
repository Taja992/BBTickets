package GUI.controller;

import BE.User;
import BLL.BLLUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.prefs.Preferences;

public class LoginController {

    @FXML
    private HBox titleBar;
    @FXML
    private Button closeButton;
    private double xOffset = 0;
    private double yOffset = 0;
    public Button loginBtn;
    public CheckBox rememberCheckBox;
    public Label errorMsgLabel;
    public Label forgotPWLbl;

    private BLLUser bllUser;

    public TextField usernameField;
    public PasswordField passwordField;

    // Create a Preferences instance
    private Preferences prefs;

    public LoginController() {
        bllUser = new BLLUser();

        // Get the preferences for this user node
        prefs = Preferences.userNodeForPackage(LoginController.class);
    }

    public void initialize() {
        windowMovement();


        // Load the saved username and password
        String savedUsername = prefs.get("username", "");
        String savedPassword = prefs.get("password", "");
        usernameField.setText(savedUsername);
        passwordField.setText(savedPassword);

        // Set the checkbox to selected if the username and password are saved
        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            rememberCheckBox.setSelected(true);
        }

        forgotPWLbl.setOnMouseClicked(event -> {
            errorMsgLabel.setText("Too bad;(");
        });

        loginBtn.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            try {
                User user = bllUser.getUser(username, password);
                if (user != null) {
                    // Save the username and password if the checkbox is selected
                    if (rememberCheckBox.isSelected()) {
                        prefs.put("username", username);
                        prefs.put("password", password);
                    } else {
                        prefs.remove("username");
                        prefs.remove("password");
                    }

                    Stage stage = new Stage();
                    Parent root = null;
                    if (user.getUser_type() == 1) {
                        root = FXMLLoader.load(getClass().getResource("/GUI/view/adminDashboard.fxml"));
                        System.out.println("Admin dashboard opened!");
                    } else if ((user.getUser_type() == 0)) {
                        root = FXMLLoader.load(getClass().getResource("/GUI/view/ecDashboard.fxml"));
                        System.out.println("EC dashboard opened!");
                    }
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.initStyle(StageStyle.DECORATED);
                    stage.show();

                    ((Stage) loginBtn.getScene().getWindow()).close();

                } else {
                    errorMsgLabel.setText("Invalid username or password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void windowMovement(){
        // Add functionality to move the window around
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        // Add functionality to close the window
        closeButton.setOnAction(event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
    }
}