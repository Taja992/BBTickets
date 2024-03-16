package GUI.controller;

import BE.User;
import BLL.BLLUser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.prefs.Preferences;

public class LoginController {
    public Button loginBtn;
    public CheckBox rememberCheckBox;
    public Label errorMsgLabel;

    private BLLUser bllUser;

    public TextField usernameField;
    public TextField passwordField;

    // Create a Preferences instance
    private Preferences prefs;

    public LoginController() {
        bllUser = new BLLUser();

        // Get the preferences for this user node
        prefs = Preferences.userNodeForPackage(LoginController.class);
    }

    public void initialize() {
        // Load the saved username and password
        String savedUsername = prefs.get("username", "");
        String savedPassword = prefs.get("password", "");
        usernameField.setText(savedUsername);
        passwordField.setText(savedPassword);

        // Set the checkbox to selected if the username and password are saved
        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            rememberCheckBox.setSelected(true);
        }

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

                    Stage stage = (Stage) loginBtn.getScene().getWindow();
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
                    stage.show();
                } else {
                    errorMsgLabel.setText("Invalid username or password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}