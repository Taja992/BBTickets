package GUI.controller;

import BE.Event;
import BLL.BLLEvent;
import Exceptions.BBExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.time.LocalDate;
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

    ECDashboardController controller;

    //This needs to be changed to use the model instead of BLL
    private BLLEvent bllEvent = new BLLEvent();

    public void setId(int Id){
        eventIdField.setText(String.valueOf(Id));
    }

    public void setEvent(Event event){
        eventIdField.setText(String.valueOf(event.getEventId()));
        eventTypeField.setText(event.getEventType());
        eventLocationField.setText(event.getEventLocation());
        eventStartDatePicker.setValue(event.getEventStartTime().toLocalDate());

    }

    public void setDashboard(ECDashboardController controller){
        this.controller = controller;
    }

    public void manageEvent(ActionEvent actionEvent) throws BBExceptions {
        int eventID = Integer.parseInt(eventIdField.getText());
        String type = eventTypeField.getText();
        int hour = (int) eventHourSpinner.getValue();
        int minute = (int) eventMinuteSpinner.getValue();
        LocalDateTime startTime = LocalDateTime.of(eventStartDatePicker.getValue(), LocalTime.of(hour, minute));

        Event event = new Event(eventID,type,eventLocationField.getText(), startTime);
        bllEvent.manageEvent(event);
        controller.refreshTable();
    }
}
