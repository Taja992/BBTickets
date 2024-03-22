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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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
    private ListView<String> userListLv;
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
    private int userId;

    public RenameAdminDashboardController() {
        eventModel = new EventModel();
        userModel = new UserModel();
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void initialize() {
        setupEventListView();
        loadUsers();
        eventListObserver();
        DragAndDrop dragAndDrop = new DragAndDrop(userListLv, eventListLv);
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
            // Call getAllEvents from eventModel and set the result as the items of eventListLstV
            eventListLv.getItems().setAll(eventModel.getAllEvents());
        } catch (BBExceptions e) {
            e.printStackTrace();
        }
    }

    public void loadUsers() {
        try {
            List<User> users = userModel.getAllUsers();
            for (User user : users) {
                userListLv.getItems().add(user.getUsername());
            }
        } catch (BBExceptions e) {
            e.printStackTrace();
        }
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
            // Load createEvent.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/GUI/view/createUser.fxml"));

            // Create a new stage for the create event screen
            Stage createEventStage = new Stage();
            createEventStage.initStyle(StageStyle.DECORATED);

            // Create a new scene with the loaded parent and set it on the stage
            Scene scene = new Scene(root);
            createEventStage.setTitle("Create Event");
            createEventStage.setScene(scene);

            // Show the create event stage
            createEventStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    ///////////////////////Event and User Link////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    //This uses the selected event and shows the info on the main event window
    private void eventInfo(Event event){
        Event selected = eventListLv.getSelectionModel().getSelectedItem();
        eventTypeLbl.setText(selected.getEventType());
        eventLocationLbl.setText(selected.getEventLocation());
        eventStartLbl.setText(formatDateTime(selected.getEventStartTime()));
        eventEndLbl.setText(formatDateTime(selected.getEventEndingTime()));
        eventNotesLbl.setText(selected.getEventNotes());
        eventDirLbl.setText(selected.getLocationGuidance());
    }

    //This watches the event list for what has been selected
    private void eventListObserver() {
        eventListLv.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleEventSelection(newValue);
            }
        });
    }

    //This Applies the event to the eventInfo as well as populates the userbox showing which
    //users are responsible for the event
    private void handleEventSelection(Event newValue) {
        eventInfo(newValue);
        userWindowHbox.getChildren().clear();

        try {
            List<User> users = userModel.getUsersForEvent(newValue.getEventId());
            for (User user : users) {
                addUserToHbox(user);
            }
        } catch (BBExceptions e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    //This is just styling for the top box it creates a Vbox for each user, adds a circle and label containing their name
    private void addUserToHbox(User user) throws URISyntaxException {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Circle circle = createCircleForUser();
        Label label = createLabelForUser(user);
        vbox.getChildren().addAll(circle, label);
        userWindowHbox.getChildren().add(vbox);
    }

    //These adds the profile pic(random image atm) to the user
    private Circle createCircleForUser() throws URISyntaxException {
        Circle circle = new Circle(40);
        String imagePath = getRandomImagePath();
        if (imagePath != null) {
            Image image = new Image(imagePath, 80, 80, true, true);
            ImagePattern imagePattern = new ImagePattern(image);
            circle.setFill(imagePattern);
        } else {
            circle.setFill(Color.TEAL);
        }
        return circle;
    }

    //This is the formatting for the username label
    private Label createLabelForUser(User user) {
        Label label = new Label(user.getUsername());
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        return label;
    }


    //This gets the contents of the userImages folder inside the resources folder
    //Can be later swapped with a way to link profile pic
    private String getRandomImagePath() throws URISyntaxException {
        URL url = getClass().getClassLoader().getResource("userImages");
        if (url == null) {
            return null;
        }

        File folder = new File(url.toURI());
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null || listOfFiles.length == 0) {
            return null; // Return null if no files are found
        }

        //returns a random photo file string to apply
        int randomIndex = new Random().nextInt(listOfFiles.length);
        return listOfFiles[randomIndex].toURI().toString();
    }

}
