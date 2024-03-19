package GUI.controller;

import BE.User;
import Exceptions.BBExceptions;
import GUI.model.EventModel;
import GUI.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class CreateUserController {

    @FXML
    private RadioButton roleAdmin;
    @FXML
    private RadioButton roleEC;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button createUserBtn;
    @FXML
    private Button cancelCreateUserBtn;
    private Stage createUserStage;
    private ToggleGroup toggleGroup;
    private UserModel userModel;
    private AdminDashboardController adminDashboard;
    public CreateUserController(){
        userModel = new UserModel();
    }

public void initialize() {
    ToggleGroup toggleGroup = new ToggleGroup();
    roleAdmin.setToggleGroup(toggleGroup);
    roleEC.setToggleGroup(toggleGroup);

    }
    @FXML
    private void createUser(ActionEvent actionEvent) throws BBExceptions {
        int userType;
        if (roleAdmin.isSelected()) {
            userType = 1; // 1 for admin
        } else if (roleEC.isSelected()) {
            userType = 0; // 0 for event coordinator
        } else {
            // Handle the case where no role is selected
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText();

        // Handle creating the user here
        // You might need to modify this part based on how you're creating users
        User newUser = new User(userType, password, username);
        userModel.newUser(newUser);
        adminDashboard.loadUsers();
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void cancelCreateUser(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

    }
}
