package GUI.controller;

import BE.Event;
import BLL.BLLEvent;
import Exceptions.BBExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ManageEventController {

    @FXML
    private TextField eventIdField;
    @FXML
    private TextField eventTypeField;
    @FXML
    private TextField eventLocationField;
    @FXML
    private DatePicker eventStartDatePicker;
    @FXML
    private Spinner eventHourSpinner;
    @FXML
    private Spinner eventMinuteSpinner;
    private BLLEvent bllEvent = new BLLEvent();

    public void setId(int Id){
        eventIdField.setText(String.valueOf(Id));
    }

    public void manageEvent(ActionEvent actionEvent) throws BBExceptions {

        int eventID = Integer.parseInt(eventIdField.getText());
        String type = eventTypeField.getText();
        int hour = (int) eventHourSpinner.getValue();
        int minute = (int) eventMinuteSpinner.getValue();
        LocalDateTime startTime = LocalDateTime.of(eventStartDatePicker.getValue(), LocalTime.of(hour, minute));

        Event event = new Event(eventID,type,eventLocationField.getText(), startTime);
        bllEvent.manageEvent(event);
    }
}
