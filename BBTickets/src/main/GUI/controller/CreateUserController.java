package GUI.controller;

import BE.Event;
import BE.User;
import Exceptions.BBExceptions;
import GUI.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
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
    private UserModel userModel;
    private User userToEdit;
    private EventHelper eventHelper;
    private Event selectedEvent;
    @FXML
    private Button createUserBtn;


    public CreateUserController() {
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public void setEventHelper(EventHelper eventHelper) {
        this.eventHelper = eventHelper;
    }
    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public void setUserToEdit(User user) {
        this.userToEdit = user;
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        if (user.getUser_type() == 1) {
            roleAdmin.setSelected(true);
        } else if (user.getUser_type() == 0) {
            roleEC.setSelected(true);
        }
    }

    public void initialize() {
        ToggleGroup toggleGroup = new ToggleGroup();
        roleAdmin.setToggleGroup(toggleGroup);
        roleEC.setToggleGroup(toggleGroup);
    }

    public void setCreateUserBtn(Button createEventBtn) {
        this.createUserBtn = createEventBtn;
    }

    @FXML
    private void createUser(ActionEvent actionEvent) {
        int userType;
        if (roleAdmin.isSelected()) {
            userType = 1; // 1 for admin
        } else if (roleEC.isSelected()) {
            userType = 0; // 0 for event coordinator
        } else {
            showErrorDialog("You must select a role.");
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            if (userToEdit != null) {
                // Update the existing user
                userToEdit.setUsername(username);
                userToEdit.setPassword(password);
                userToEdit.setUser_type(userType);
                userModel.updateUser(userToEdit);
                if(selectedEvent != null) {
                eventHelper.refreshUserWindowHbox(selectedEvent);
                }
            } else {
                // Create a new user
                User newUser = new User(userType, password, username);
                userModel.newUser(newUser);
            }

        } catch (BBExceptions e) {
            e.printStackTrace();
            showErrorDialog("Failed to create or edit user.");
        }

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void cancelCreateUser(ActionEvent actionEvent) {
        // Re-enable the button
        if (createUserBtn != null) {
            createUserBtn.setDisable(false);
        }

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void showErrorDialog(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}