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
    public TextField eventTypeField;
    public TextField eventLocationField;
    public DatePicker eventStartDatePicker;
    public Spinner<Integer> eventHourSpinner;
    public Spinner<Integer> eventMinuteSpinner;

    @FXML
    private TextField eventIdField;


    @FXML
    private Button addEvent;
    private final BLLEvent bllEvent;

    public AppController(){
        bllEvent = new BLLEvent();
    }

    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 15);
        eventMinuteSpinner.setValueFactory(valueFactory);
    }


    public void addEvent(ActionEvent actionEvent) {
        String eventType = eventTypeField.getText();
        String eventLocation = eventLocationField.getText();
        int hour = eventHourSpinner.getValue();
        int minute = eventMinuteSpinner.getValue();
        LocalDateTime eventStartTime = LocalDateTime.of(eventStartDatePicker.getValue(), LocalTime.of(hour, minute));
        Event event = new Event(eventType, eventLocation, eventStartTime);

        try {
            bllEvent.newEvent(event);
        } catch (BBExceptions e) {
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
