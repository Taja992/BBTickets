package GUI.controller;

import BE.Event;
import BLL.BLLEvent;
import Exceptions.BBExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppController {

    @FXML
    private TextField eventIdField;
    @FXML
    private TextField eventTypeField;
    @FXML
    private TextField eventLocationField;
    @FXML
    private DatePicker eventStartDatePicker;
    @FXML
    private Spinner<Integer> eventHourSpinner;
    @FXML
    private Spinner<Integer> eventMinuteSpinner;
    @FXML
    private DatePicker eventEndDatePicker;
    @FXML
    private TextField eventNotesField;
    @FXML
    private TextField locationGuidanceField;
    @FXML
    private Button addEvent;
    private final BLLEvent bllEvent;

    public AppController(){
        bllEvent = new BLLEvent();
    }

    public void initialize() {
    }


    public void addEvent(ActionEvent actionEvent) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/createEvent.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    

    public void deleteEvent(ActionEvent actionEvent) throws BBExceptions {
        if(!eventIdField.getText().isEmpty()){
            bllEvent.DeleteEvent(Integer.parseInt(eventIdField.getText()));
        }
    }

    public void manageEvent(ActionEvent actionEvent) throws IOException {
        if(!eventIdField.getText().isEmpty()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/manageEvent.fxml"));
            Parent root = loader.load();

            ManageEventController controller = loader.getController();
            controller.setId(Integer.parseInt(eventIdField.getText()));

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }

    }
}
