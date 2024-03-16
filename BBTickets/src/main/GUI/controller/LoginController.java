package GUI.controller;

import BE.User;
import BLL.BLLUser;
import DAL.UserDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    public Button loginBtn;

    private BLLUser bllUser;

    public TextField usernameField;
    public TextField passwordField;

    public LoginController() {
        bllUser = new BLLUser();
    }
    public void initialize() {
        loginBtn.setOnAction(event -> {
            System.out.println("Login button clicked");
            String username = usernameField.getText();
            String password = passwordField.getText();

            try {
                User user = bllUser.getUser(username, password);
                if (user != null) {
                    Stage stage = (Stage) loginBtn.getScene().getWindow();
                    Parent root = null;
                    if (user.getUser_type() == 1) {
                        // Open admin dashboard
                        System.out.println("Admin logged in");
                        root = FXMLLoader.load(getClass().getResource("/GUI/view/adminDashboard.fxml"));

                    } else if ((user.getUser_type() == 0)) {
                        // Open event coordinator dashboard
                        System.out.println("Event coordinator logged in");
                        root = FXMLLoader.load(getClass().getResource("/GUI/view/ecDashboard.fxml"));
                    }
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    System.out.println("Invalid username or password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}
