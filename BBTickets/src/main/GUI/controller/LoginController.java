package GUI.controller;

import BE.User;
import BLL.BLLUser;
import DAL.UserDAO;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
                    if (user.getUser_type() == 1) {
                        // Open admin dashboard
                        System.out.println("Admin logged in");
                    } else {
                        // Open event coordinator dashboard
                        System.out.println("Event coordinator logged in");
                    }
                } else {
                    System.out.println("Invalid username or password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}
