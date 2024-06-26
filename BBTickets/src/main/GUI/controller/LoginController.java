package GUI.controller;

import BE.User;
import GUI.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;
import java.util.prefs.Preferences;

public class LoginController {


    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private Button loginBtn;
    @FXML
    private CheckBox rememberCheckBox;
    @FXML
    private Label errorMsgLabel;
    @FXML
    private Label forgotPWLbl;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private UserModel userModel;


    // Create a Preferences instance
    private Preferences prefs;

    public LoginController() {
        userModel = new UserModel();

        // Get the preferences for this user node
        prefs = Preferences.userNodeForPackage(LoginController.class);
    }

    public void initialize() {
        loadSavedCredentials();
        setupForgotPasswordLabel();
        setupLoginButton();
        setupPasswordFieldEnterKey();
        setupUsernameFieldEnterKey();
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

    // Handle the forgot password label click
    private void setupForgotPasswordLabel() {
        forgotPWLbl.setOnMouseClicked(this::handleForgotPasswordLabelClick);
    }

    @FXML
    private void handleForgotPasswordLabelClick(MouseEvent event) {
        errorMsgLabel.setText("Too bad;(");
    }
    // Handle the login button click

    private void setupLoginButton() {
        loginBtn.setOnAction(this::handleLoginButtonClick);
    }

    private void setupPasswordFieldEnterKey() {
        passwordField.setOnKeyPressed(event -> {
            if (Objects.requireNonNull(event.getCode()) == KeyCode.ENTER) {
                loginBtn.fire();
            }
        });
    }

    private void setupUsernameFieldEnterKey() {
        usernameField.setOnKeyPressed(event -> {
            if (Objects.requireNonNull(event.getCode()) == KeyCode.ENTER) {
                loginBtn.fire();
            }
        });
    }

    @FXML
    private void handleLoginButtonClick(ActionEvent event) {
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
                loader = new FXMLLoader(getClass().getResource("/GUI/view/AdminDashboard.fxml"));
                root = loader.load();
                AdminDashboardController controller = loader.getController();
                controller.setUserId(user.getUserId());
                stage.setTitle("Admin Dashboard");
            } else if ((user.getUser_type() == 0)) {
                loader = new FXMLLoader(getClass().getResource("/GUI/view/ECDashboard.fxml"));
                root = loader.load();
                EcDashboardController controller = loader.getController();
                controller.setUserId(user.getUserId());
                stage.setTitle("Event Coordinator Dashboard");
            }
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            //  stage.initStyle(StageStyle.DECORATED);
            stage.show();

            windowControls(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void windowControls(Stage primaryStage, Scene scene){
        BorderPane mainBp = (BorderPane) scene.lookup("#mainBp");
        Button closeBtn = (Button) scene.lookup("#closeBtn");

        // Add functionality to move the window around
        mainBp.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        mainBp.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        // Add functionality to close the window
        closeBtn.setOnAction(event -> primaryStage.close());
    }

    private void closeLoginWindow() {
        ((Stage) loginBtn.getScene().getWindow()).close();
    }
}