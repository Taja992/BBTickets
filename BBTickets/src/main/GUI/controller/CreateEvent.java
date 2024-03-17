package GUI.controller;

import BE.Event;
import BLL.BLLEvent;
import Exceptions.BBExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class CreateEvent {

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

    public CreateEvent(){
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
            // Get the current stage from the action event and close it
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        } catch (BBExceptions e) {
            e.printStackTrace();
        }
    }

    public void closeWindow(ActionEvent actionEvent) {
        // Get the current stage from the action event and close it
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
