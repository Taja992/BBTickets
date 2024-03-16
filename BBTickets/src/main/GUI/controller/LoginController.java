package GUI.controller;

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
        });

    }


}
