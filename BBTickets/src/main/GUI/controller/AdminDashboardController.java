package GUI.controller;

import BE.Event;
import BE.User;
import Exceptions.BBExceptions;
import GUI.model.EventModel;
import GUI.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AdminDashboardController {
    public Button createUserBtn;
    public Button closeBtn;
    @FXML
    private BorderPane mainBp;
    @FXML
    private VBox eventListVbox;
    @FXML
    private ListView<BE.Event> eventListLv;
    @FXML
    private ListView<User> userListLv;
    @FXML
    private BorderPane nestedBp;
    @FXML
    private HBox userWindowHbox;
    @FXML
    private VBox eventWindowVbox;
    @FXML
    private HBox bottomHbox;
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
    private EventModel eventModel;
    private UserModel userModel;
    private EventHelper eventHelper;
    private int userId;

    public AdminDashboardController() {
        userModel = UserModel.getInstance();
        eventModel = new EventModel();
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void initialize() {
        this.eventHelper = new EventHelper(eventListLv, userWindowHbox, userModel, eventModel, eventTypeLbl, eventLocationLbl, eventStartLbl, eventEndLbl, eventNotesLbl, eventDirLbl);
        setupEventListView();
        userListLv.setItems(userModel.getAllUsers());
        listViewcell();
        eventHelper.eventListObserver();
        DragAndDrop dragAndDrop = new DragAndDrop(userListLv, eventListLv, userWindowHbox, eventHelper);
        rightClickMenu();
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
            Parent root = FXMLLoader.load(getClass().getResource("/GUI/view/login.fxml"));

            // Create a new stage for the login screen
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.TRANSPARENT);

            // Create a new scene with the loaded parent and set it on the stage
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            loginStage.setTitle("Ticket Interface");
            loginStage.setScene(scene);

            // Get the current stage and close it
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

            // Show the login stage
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void deleteEvent(ActionEvent actionEvent) throws BBExceptions {
        //gets the selected item from the table and deletes it (does nothing if nothing is selected)
        BE.Event selected = eventListLv.getSelectionModel().getSelectedItem();
        if (selected != null) {
            eventModel.deleteEvent(selected.getEventId());
            loadEventsToListView(); //reloads the table so it updates with the item deleted
        }
    }


    private void setupEventListView() {
        // Set the cell factory of the ListView
        eventListLv.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    // Set the text of the cell to the eventType of the Event
                    setText(String.valueOf(item.getEventType()));
                }
            }
        });

        // Load the events into the ListView
        loadEventsToListView();
    }

    private void loadEventsToListView() {
        try {
            eventListLv.getItems().setAll(eventModel.getAllEvents());
        } catch (BBExceptions e) {
            e.printStackTrace();
            showErrorDialog("Load Events Error", "Failed to load events: " + e.getMessage());
        }
    }

    public void loadUsers() {
        List<User> users = userModel.getAllUsers();
        userListLv.getItems().setAll(users);
    }

    private void listViewcell() {
        userListLv.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getUsername());
                }
            }
        });
    }


    @FXML
    public void createUserBtn(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/createUser.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Create User Error", "Failed to load user creation view: " + e.getMessage());
        }
    }


    private void removeUserFromEvent() {

    }

    private void editUser() {
        User selectedUser = userListLv.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/CreateUser.fxml"));
                    Parent root = loader.load();
                    CreateUserController controller = loader.getController();
                    controller.initEditMode(selectedUser);

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
        if (selectedUser != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete User");
            alert.setContentText("Are you sure you want to delete this user?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                // User chose OK, proceed with deletion
                try {
                    userModel.deleteUser(selectedUser);
                } catch (BBExceptions e) {
                    e.printStackTrace();
                    String errorMessage = "Failed to delete user " + selectedUser.getUsername() + ": " + e.getMessage();
                    showErrorDialog("Delete User Error", errorMessage);
                }
            } else {
                // User chose Cancel or closed the dialog, do nothing
            }
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
