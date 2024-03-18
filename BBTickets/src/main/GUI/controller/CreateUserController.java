package GUI.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateUserController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private RadioButton Admin;
    @FXML
    private RadioButton EventCoordinator;
    @FXML
    private Button createUserBtn;
    @FXML
    private Button cancelCreateUserBtn;
    private Stage createUserStage;


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
