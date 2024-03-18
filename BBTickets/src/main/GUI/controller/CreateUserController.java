package GUI.controller;

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

public void initialize() {
    ToggleGroup toggleGroup = new ToggleGroup();
    roleAdmin.setToggleGroup(toggleGroup);
    roleEC.setToggleGroup(toggleGroup);

    }
    @FXML
    private void createUser(ActionEvent actionEvent) {

        String username = usernameField.getText();
        String password = passwordField.getText();



        // Handle creating the user here
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
