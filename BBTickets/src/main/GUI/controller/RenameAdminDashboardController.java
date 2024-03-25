package GUI.controller;

import BE.Event;
import BE.User;
import Exceptions.BBExceptions;
import GUI.model.EventModel;
import GUI.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class RenameAdminDashboardController {
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

    public RenameAdminDashboardController() {
        eventModel = new EventModel();
        userModel = new UserModel();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void initialize() {
        this.eventHelper = new EventHelper(eventListLv, userWindowHbox, userModel, eventTypeLbl, eventLocationLbl, eventStartLbl, eventEndLbl, eventNotesLbl, eventDirLbl);
        setupEventListView();
        userListLv.setItems(userModel.getAllUsers());
        listViewcell();
        eventHelper.eventListObserver();
        DragAndDrop dragAndDrop = new DragAndDrop(userListLv, eventListLv, userWindowHbox, eventHelper);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit user");
        editItem.setOnAction(e -> editUser());
        contextMenu.getItems().add(editItem);

        MenuItem deleteItem = new MenuItem("Delete user");
        deleteItem.setOnAction(e -> deleteUser());
        contextMenu.getItems().add(deleteItem);

        userListLv.setContextMenu(contextMenu);
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
            Stage currentStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

            // Show the login stage
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void assignCoordinator(ActionEvent actionEvent) {
    }

    public void createTicket(ActionEvent actionEvent) {
    }

    public void deleteEvent(ActionEvent actionEvent)  throws BBExceptions {
        //gets the selected item from the table and deletes it (does nothing if nothing is selected)
        BE.Event selected = eventListLv.getSelectionModel().getSelectedItem();
        if (selected != null) {
            eventModel.deleteEvent(selected.getEventId());
            loadEventsToListView(); //reloads the table so it updates with the item deleted
        }
    }

    public void editEvent(ActionEvent actionEvent) {
    }

    public void createEventBtn(ActionEvent actionEvent) {
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
            // Call getAllEvents from eventModel and set the result as the items of eventListLv
            eventListLv.getItems().setAll(eventModel.getAllEvents());
        } catch (BBExceptions e) {
            e.printStackTrace();
        }
    }

    public void loadUsers() {
        List<User> users = userModel.getAllUsers();
        userListLv.getItems().setAll(users);
    }

    private void listViewcell(){
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

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy  '⏰'HH:mm");
        return formatter.format(dateTime);
    }

    @FXML
    public void createUserBtn(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/createUser.fxml"));
            Parent root = loader.load();

            CreateUserController controller = loader.getController();
            controller.setRenameAdminDashboardController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void editUser() {
        User selectedUser = userListLv.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/CreateUser.fxml"));
                Parent root = loader.load();

                CreateUserController controller = loader.getController();
                controller.initEditMode(selectedUser);
                controller.setRenameAdminDashboardController(this);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteUser() {
        User selectedUser = userListLv.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                userModel.deleteUser(selectedUser);

            } catch (BBExceptions e) {
                e.printStackTrace();
            }
        }
    }

}
