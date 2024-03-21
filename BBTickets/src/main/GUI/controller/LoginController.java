package GUI.controller;

import BE.User;
import GUI.model.UserModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.prefs.Preferences;

public class LoginController {

    @FXML
    private BorderPane titleBar;
    @FXML
    private Button closeButton;
    private double xOffset = 0;
    private double yOffset = 0;
    public Button loginBtn;
    public CheckBox rememberCheckBox;
    public Label errorMsgLabel;
    public Label forgotPWLbl;

    private UserModel userModel;

    public TextField usernameField;
    public PasswordField passwordField;

    // Create a Preferences instance
    private Preferences prefs;

    public LoginController() {
        userModel = new UserModel();

        // Get the preferences for this user node
        prefs = Preferences.userNodeForPackage(LoginController.class);
    }

    public void initialize() {
        windowMovement();
        loadSavedCredentials();
        setupForgotPasswordLabel();
        setupLoginButton();
    }

    private void windowMovement(){
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

    private void loadSavedCredentials() {
        // Load the saved username and password
        String savedUsername = prefs.get("username", "");
        String savedPassword = prefs.get("password", "");
        usernameField.setText(savedUsername);
        passwordField.setText(savedPassword);

        // Set the checkbox to selected if the username and password are saved
        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            rememberCheckBox.setSelected(true);
        }
    }

    private void setupForgotPasswordLabel() {
        forgotPWLbl.setOnMouseClicked(event -> {
            errorMsgLabel.setText("Too bad;(");
        });
    }

    private void setupLoginButton() {
        loginBtn.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            try {
                User user = userModel.getUser(username, password);
                if (user != null) {
                    handleSuccessfulLogin(user, username, password);
                } else {
                    handleFailedLogin();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void handleSuccessfulLogin(User user, String username, String password) {
        if (rememberCheckBox.isSelected()) {
            saveUserCredentials(username, password);
        } else {
            removeUserCredentials();
        }

        openDashboard(user);
        closeLoginWindow();
    }

    private void handleFailedLogin() {
        errorMsgLabel.setText("Invalid username or password");
    }

    private void saveUserCredentials(String username, String password) {
        prefs.put("username", username);
        prefs.put("password", password);
    }

    private void removeUserCredentials() {
        prefs.remove("username");
        prefs.remove("password");
    }

    private void openDashboard(User user) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader;
            if (user.getUser_type() == 1) {
                loader = new FXMLLoader(getClass().getResource("/GUI/view/renameAdminDashboard.fxml"));
                root = loader.load();
                RenameAdminDashboardController controller = loader.getController();
                controller.setUserId(user.getUserId());
                System.out.println("Admin dashboard opened!");
                stage.setTitle("Admin Dashboard");
            } else if ((user.getUser_type() == 0)) {
                loader = new FXMLLoader(getClass().getResource("/GUI/view/ecDashboard.fxml"));
                root = loader.load();
                ECDashboardController controller = loader.getController();
                controller.setUserId(user.getUserId());
                System.out.println("EC dashboard opened!");
                stage.setTitle("Event Coordinator Dashboard");
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.DECORATED);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeLoginWindow() {
        ((Stage) loginBtn.getScene().getWindow()).close();
    }
}