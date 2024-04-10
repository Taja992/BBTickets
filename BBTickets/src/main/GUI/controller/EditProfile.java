package GUI.controller;

import Exceptions.BBExceptions;
import GUI.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EditProfile {

    @FXML
    private Button cancelEditProfileBtn;
    @FXML
    private Button browseProfilePictureBtn;
    @FXML
    private Button saveProfileBtn;
    private UserModel userModel;
    private BE.User loggedInUser;

    @FXML
    private javafx.scene.control.TextField usernameField;
    @FXML
    private javafx.scene.control.TextField passwordField;
    @FXML
    private javafx.scene.control.TextField picturePathField;

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public void setLoggedInUser(BE.User loggedInUser) {
        this.loggedInUser = loggedInUser;
        usernameField.setText(loggedInUser.getUsername());
        passwordField.setText(loggedInUser.getPassword());
        //check if picture is not null
        if (loggedInUser.getProfilePicture() != null) {
            picturePathField.setText(new String(loggedInUser.getProfilePicture()));
        } else {
            picturePathField.setText("");
        }
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
    private void saveProfile(ActionEvent actionEvent) throws BBExceptions {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String picturePath = picturePathField.getText();
        loggedInUser.setUsername(username);
        loggedInUser.setPassword(password);

        // Save the image path instead of the image data
        loggedInUser.setProfilePicture(picturePath.getBytes());

        userModel.updateUser(loggedInUser); // Update the user details in the database
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


