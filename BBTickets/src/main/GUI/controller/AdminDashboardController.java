package GUI.controller;

import BE.Event;
import BE.User;
import Exceptions.BBExceptions;
import GUI.model.EventModel;
import GUI.model.UserModel;
import GUI.util.ListViewSetupUtility;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.StageStyle;


import java.io.ByteArrayInputStream;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Arrays;

import java.io.IOException;


public class AdminDashboardController {

    @FXML
    private Button createUserBtn;
    @FXML
    private Button closeBtn;
    @FXML
    private JFXToggleButton toggleUserListBtn;
    @FXML
    private ListView<BE.Event> eventListLv;
    @FXML
    private ListView<User> userListLv;
    @FXML
    private HBox userWindowHbox;
    @FXML

    private VBox eventWindowVbox;
    @FXML
    private HBox bottomHbox;
    @FXML
    private Button editProfileBtn;
    @FXML
    private Button createEventBtn;
    @FXML
    private Button logoutBtn;
    @FXML

    private Label eventTypeLbl;
    @FXML
    private Label eventLocationLbl;
    @FXML
    private Label eventStartLbl;
    @FXML
    private Label eventEndLbl;
    @FXML
    private Label eventNotesLbl;
    @FXML
    private Label eventDirLbl;
    @FXML
    private Circle pictureHolder;
    private EventModel eventModel;
    private UserModel userModel;
    private EventHelper eventHelper;
    private int userId;
    private User loggedInUser;



    public AdminDashboardController() throws BBExceptions {
        eventModel = new EventModel();
        userModel = new UserModel();
    }

    public void setUserId(int userId) throws BBExceptions {
        this.userId = userId;
        setProfilePicture();

    }
    public void setProfilePicture() throws BBExceptions {
        loggedInUser = userModel.getUserById(userId);
        Image image = null;
        if (loggedInUser != null && loggedInUser.getProfilePicture() != null) {
            System.out.println("User is logged in");
            byte[] imageData = loggedInUser.getProfilePicture();
            if (imageData != null && imageData.length > 0) {
                // Convert byte array to input stream
                try (InputStream imageStream = new ByteArrayInputStream(imageData)) {
                    image = new Image(imageStream);
                } catch (IOException e) {
                    e.printStackTrace();
                    String defaultImagePath = "/images/pictureplaceholder.png";
                    image = new Image(defaultImagePath);
                }
            }
            else
            {
                //no image add default
                String defaultImagePath = "/images/pictureplaceholder.png";
                image = new Image(defaultImagePath);
            }
        }
        else
        {
            //no image add default
            String defaultImagePath = "/images/pictureplaceholder.png";
            image = new Image(defaultImagePath);
        }

        ImagePattern imagePattern = new ImagePattern(image);
        pictureHolder.setFill(imagePattern);
    }



    public void initialize() {
        this.eventHelper = new EventHelper(eventListLv, userWindowHbox, userModel, eventModel, eventTypeLbl, eventLocationLbl, eventStartLbl, eventEndLbl, eventNotesLbl, eventDirLbl);
        ListViewSetupUtility.setupEventListView(eventListLv, eventModel);
        userListLv.setItems(userModel.getUsersByType(1)); // this is since toggle is starting on admin
        ListViewSetupUtility.setupUserListView(userListLv);
        eventHelper.eventListObserver();
        DragAndDrop dragAndDrop = new DragAndDrop(userListLv, eventListLv, userWindowHbox, eventHelper);
        rightClickMenu();
        closeBtn.setId("closeBtn");
    }

    public void rightClickMenu() {

        ContextMenu userlistContextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit user");
        editItem.setOnAction(e -> editUser());
        userlistContextMenu.getItems().add(editItem);

        MenuItem deleteItem = new MenuItem("Delete user");
        deleteItem.setOnAction(e -> deleteUser());
        userlistContextMenu.getItems().add(deleteItem);


        userListLv.setContextMenu(userlistContextMenu);
    }

    public void logoutBtn(ActionEvent actionEvent) {
        try {
            // Load login.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/login.fxml"));
            Parent root = loader.load();

            // Create a new stage for the login screen
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.TRANSPARENT);

            // Create a new scene with the loaded parent and set it on the stage
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            loginStage.setTitle("Ticket Interface");
            loginStage.setScene(scene);

            // Get the current stage and close it
            Stage currentStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

            // Show the login stage
            loginStage.show();

            // Check if the loaded controller is LoginController and call windowControls
            if (loader.getController() instanceof LoginController) {
                LoginController loginController = loader.getController();
                loginController.windowControls(loginStage, scene);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void deleteEvent(ActionEvent actionEvent)  throws BBExceptions {
        //gets the selected item from the table and deletes it (does nothing if nothing is selected)
        BE.Event selected = eventListLv.getSelectionModel().getSelectedItem();
        if (selected != null) {
            eventModel.deleteEvent(selected.getEventId());
            loadEventsToListView(); //reloads the table so it updates with the item deleted
        }
    }


    private void loadEventsToListView() {
        try {
            eventListLv.getItems().setAll(eventModel.getAllEvents());
        } catch (BBExceptions e) {
            e.printStackTrace();
            showErrorDialog("Load Events Error", "Failed to load events: " + e.getMessage());
        }
    }


    public void createUserBtn(ActionEvent actionEvent) {
        Event selectedEvent = eventListLv.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/createUser.fxml"));
            Parent root = loader.load();

            CreateUserController controller = loader.getController();
            controller.setUserModel(userModel);
            controller.setEventHelper(eventHelper);
            controller.setSelectedEvent(selectedEvent);
            controller.setCreateUserBtn(createUserBtn);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Disable the button
            ((Node) actionEvent.getSource()).setDisable(true);

            // Add a listener to the window close event to re-enable the button
            stage.setOnCloseRequest(event -> ((Node) actionEvent.getSource()).setDisable(false));
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Create User Error", "Failed to load user creation view: " + e.getMessage());
        }
    }

    public void editProfileBtn(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/EditProfile.fxml"));
            Parent root = loader.load();

            EditProfile controller = loader.getController();
            controller.setUserModel(userModel);
            User loggedInUser = userModel.getUserById(userId);
            controller.setLoggedInUser(loggedInUser);


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | BBExceptions e) {
            e.printStackTrace();
            showErrorDialog("Edit Profile Error", "Failed to load profile editing view: " + e.getMessage());
        }
    }


    private void editUser() {
        User selectedUser = userListLv.getSelectionModel().getSelectedItem();
        Event selectedEvent = eventListLv.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/CreateUser.fxml"));
                Parent root = loader.load();

                CreateUserController controller = loader.getController();
                controller.setUserModel(userModel);
                controller.setUserToEdit(selectedUser);
                controller.setEventHelper(eventHelper);
                controller.setSelectedEvent(selectedEvent);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showErrorDialog("Edit User Error", "Failed to load user editing view: " + e.getMessage());
            }
        }
    }

    private void deleteUser() {
        User selectedUser = userListLv.getSelectionModel().getSelectedItem();
        Event selectedEvent = eventListLv.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                userModel.deleteUser(selectedUser);
                if (selectedEvent != null) {
                    eventHelper.refreshUserWindowHbox(selectedEvent);
                }
            } catch (BBExceptions e) {
                e.printStackTrace();
                String errorMessage = "Failed to delete user " + selectedUser.getUsername() + ": " + e.getMessage();
                showErrorDialog("Delete User Error", errorMessage);
            }
        }
    }

    private void showErrorDialog(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void toggleUserList(ActionEvent actionEvent) {
        if (toggleUserListBtn.isSelected()) {
            userListLv.setItems(userModel.getUsersByType(0)); // Show only users of type 0 (EC)
        } else {
            userListLv.setItems(userModel.getUsersByType(1)); // Show only users of type 1 (Admin)
        }
    }
}
