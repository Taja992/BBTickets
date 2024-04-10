package GUI.controller;

import GUI.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.File;

public class EditProfile {

    @FXML
    private Button cancelEditProfileBtn;
    @FXML
    private Button browseProfilePictureBtn;
    @FXML
    private Button saveProfileBtn;
    private UserModel userModel;

    @FXML
    private javafx.scene.control.TextField usernameField;
    @FXML
    private javafx.scene.control.TextField passwordField;
    @FXML
    private javafx.scene.control.TextField picturePathField;

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    @FXML
    private void browseProfilePicture(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            picturePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void saveProfile(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String picturePath = picturePathField.getText();
        userModel.editProfile(username, password, picturePath);
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void cancelEditProfile(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}


