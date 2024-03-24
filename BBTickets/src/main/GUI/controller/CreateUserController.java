package GUI.controller;

import BE.User;
import Exceptions.BBExceptions;
import GUI.model.EventModel;
import GUI.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.io.IOException;

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
    @FXML
    private ListView<User> userListLv;
    private Stage createUserStage;
    private ToggleGroup toggleGroup;
    private UserModel userModel;
    private boolean editMode = false;
    private User userToEdit;

    public CreateUserController() {
        userModel = new UserModel();
    }

    public void initialize() {
        ToggleGroup toggleGroup = new ToggleGroup();
        roleAdmin.setToggleGroup(toggleGroup);
        roleEC.setToggleGroup(toggleGroup);


    }

    public void initEditMode(User userToEdit) {
        this.userToEdit = userToEdit;
        this.editMode = true;

        // Fill the text fields with the user's data
        usernameField.setText(userToEdit.getUsername());
        passwordField.setText(userToEdit.getPassword());
        if (userToEdit.getUser_type() == 1) {
            roleAdmin.setSelected(true);
        } else {
            roleEC.setSelected(true);
        }
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

        if (editMode) {
            // Update the existing user
            userToEdit.setUsername(username);
            userToEdit.setPassword(password);
            userToEdit.setUser_type(userType);
            userModel.updateUser(userToEdit);
        } else {
            // Create a new user
            User newUser = new User(userType, password, username);
            userModel.newUser(newUser);
        }

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        if (renameAdminDashboardController != null) {
            renameAdminDashboardController.refreshUserList();
        }
    }


    private RenameAdminDashboardController renameAdminDashboardController;

    public void setRenameAdminDashboardController(RenameAdminDashboardController renameAdminDashboardController) {
        this.renameAdminDashboardController = renameAdminDashboardController;
    }



    public void cancelCreateUser(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}