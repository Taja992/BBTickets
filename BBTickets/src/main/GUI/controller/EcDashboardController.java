package GUI.controller;

import BE.Event;
import BE.User;
import Exceptions.BBExceptions;
import GUI.model.EventModel;
import GUI.model.UserModel;
import GUI.util.ListViewSetupUtility;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.StageStyle;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class EcDashboardController {

    public Button editEventBtn;
    public Button createTicketBtn;
    @FXML
    private VBox eventWindowVbox;
    @FXML
    private BorderPane mainBp;
    @FXML
    private VBox eventListVbox;
    @FXML
    private BorderPane nestedBp;
    @FXML
    private HBox userWindowHbox;
    @FXML
    private HBox bottomHbox;
    @FXML
    private Button createEventBtn;
    @FXML
    private Button closeBtn;
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
    private ListView<BE.Event> eventListLv;
    @FXML
    private ListView<User> userListLv;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button editProfileBtn;
    @FXML
    private Circle pictureHolder;
    private int userId;
    private User loggedInUser;


    private EventModel eventModel;
    private UserModel userModel;
    private EventHelper eventHelper;

    public void setUserId(int userId) {
        this.userId = userId;
        eventListForSpecificUser();
    }

    public EcDashboardController() {
        eventModel = new EventModel();
        userModel = new UserModel();}

    public void initialize() {
        this.eventHelper = new EventHelper(eventListLv, userWindowHbox, userModel, eventModel, eventTypeLbl, eventLocationLbl, eventStartLbl, eventEndLbl, eventNotesLbl, eventDirLbl);
        eventModel = new EventModel();
        setupLogoutButton();
        ListViewSetupUtility.setupEventListView(eventListLv, eventModel);
        eventHelper.eventListObserver();
        ListViewSetupUtility.setupUserListView(userListLv);
        DragAndDrop dragAndDrop = new DragAndDrop(userListLv, eventListLv, userWindowHbox, eventHelper);
        userListLv.setItems(userModel.getUsersByType(0));
    }


    private void setupLogoutButton() {
        logoutBtn.setOnAction(this::handleLogoutButtonClick);
    }

    @FXML
    private void handleLogoutButtonClick(ActionEvent event) {
        try {
            loadNewScene("/GUI/view/login.fxml", logoutBtn);
        } catch (IOException e) {
            showErrorDialog("You cannot log out.", "You may never log out again.");
            e.printStackTrace();
        }
    }


    public void refreshTable(){
        eventListLv.getItems().clear();
        eventListForSpecificUser();
    }

    private void eventListForSpecificUser(){
        // Fetch the events for a specific user
        try {
            ObservableList<Event> events = eventModel.getEventsForUser(userId);
            // Add the events to the ListView
            eventListLv.setItems(events);
        } catch (BBExceptions e) {
            showErrorDialog("Event Fetch Error", "Failed to fetch events for the user.");
        }
    }


    public void deleteEvent(ActionEvent actionEvent) {
        Event selected = eventListLv.getSelectionModel().getSelectedItem();
        if(selected != null){
            // Create a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this event?");

            // Show the dialog and wait for the user's response
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                // If the user clicked OK, delete the event
                try {
                    eventModel.deleteEvent(selected.getEventId());
                    refreshTable();
                } catch (BBExceptions e) {
                    showErrorDialog("Delete Error", "Failed to delete the event.");
                }
            }
        }
    }

    public void createTicket(ActionEvent actionEvent) {
        if(eventListLv.getSelectionModel().getSelectedItem() != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/createTicket.fxml"));
                Parent root = loader.load();

                CreateTicketController control = loader.getController();
                control.setEvent(eventListLv.getSelectionModel().getSelectedItem());
                control.setCreateTicketBtn(createTicketBtn);

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);

                // Disable the button
                ((Node) actionEvent.getSource()).setDisable(true);

                // Add a listener to the window close event to re-enable the button
                stage.setOnCloseRequest(event -> ((Node) actionEvent.getSource()).setDisable(false));
                
                stage.show();
            } catch (IOException e) {
                showErrorDialog("Create Ticket Error", "Failed to create a ticket.");
                e.printStackTrace();
            }
        } else {
            showErrorDialog("Selection Error", "Please select an event to create a ticket.");
        }
    }


    public void editEvent(ActionEvent actionEvent) {
        BE.Event selected = eventListLv.getSelectionModel().getSelectedItem();
        if(selected != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/manageEvent.fxml"));
                Parent root = loader.load();

                ManageEventController controller = loader.getController();
                controller.setDashboard(this);
                controller.setEvent(selected);

                controller.setEditEventBtn(editEventBtn);

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Edit Event");
                stage.setScene(scene);

                // Disable the button
                ((Node) actionEvent.getSource()).setDisable(true);

                // Add a listener to the window close event to re-enable the button
                stage.setOnCloseRequest(event -> ((Node) actionEvent.getSource()).setDisable(false));

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showErrorDialog("Edit Event Error", "Failed to edit the event.");
            }
        } else {
            showErrorDialog("Selection Error", "Please select an event to edit.");
        }
    }


    private void loadNewScene(String fxmlPath, Button button) throws IOException {
        // Load fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        // Create a new stage for the new screen
        Stage newStage = new Stage();
        newStage.initStyle(StageStyle.TRANSPARENT);

        // Create a new scene with the loaded parent and set it on the stage
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        newStage.setTitle("Ticket Interface");
        newStage.setScene(scene);

        // Get the current stage and close it
        Stage currentStage = (Stage) button.getScene().getWindow();
        currentStage.close();

        // Show the new stage
        newStage.show();

        // Check if the loaded controller is LoginController and call windowControls
        if (loader.getController() instanceof LoginController) {
            LoginController loginController = loader.getController();
            loginController.windowControls(newStage, scene);
        }
    }

    public void createEventBtn(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/createEvent.fxml"));
            Parent root = loader.load();

            CreateEvent controller = loader.getController();
            controller.setUserId(userId);
            controller.setRenameThisEcController(this);

            controller.setCreateEventBtn(createEventBtn);

            Stage createEventStage = new Stage();
            createEventStage.initStyle(StageStyle.DECORATED);

            Scene scene = new Scene(root);
            createEventStage.setTitle("Create Event");
            createEventStage.setScene(scene);

            // Disable the button
            ((Node) actionEvent.getSource()).setDisable(true);

            // Add a listener to the window close event to re-enable the button
            createEventStage.setOnCloseRequest(event -> ((Node) actionEvent.getSource()).setDisable(false));


            createEventStage.show();
        } catch (IOException e) {
            showErrorDialog("Create Event Error", "Failed to create an event.");
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




    public void assignCoordinator(ActionEvent actionEvent) {
    }

    public void logoutBtn(ActionEvent actionEvent) {
    }

    private void showErrorDialog(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
