package GUI.controller;

import BE.Event;
import Exceptions.BBExceptions;
import GUI.model.EventModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    @FXML
    private DatePicker eventEndDatePicker;
    @FXML
    private TextField eventNotesField;
    @FXML
    private TextField locationGuidanceField;

    EcDashboardController controller;

    //This needs to be changed to use the model instead of BLL
    private EventModel eventModel = new EventModel();

    public void setId(int Id){
        eventIdField.setText(String.valueOf(Id));
    }

    public void setEvent(Event event){
        eventIdField.setText(String.valueOf(event.getEventId()));
        eventTypeField.setText(event.getEventType());
        eventLocationField.setText(event.getEventLocation());
        eventStartDatePicker.setValue(event.getEventStartTime().toLocalDate());
        if(event.getEventEndingTime() != null){
            eventEndDatePicker.setValue(event.getEventEndingTime().toLocalDate());
        }
    }

    public void setDashboard(EcDashboardController controller){
        this.controller = controller;
    }

    public void manageEvent(ActionEvent actionEvent) throws BBExceptions {
        if(!eventTypeField.getText().isEmpty()
                && eventStartDatePicker.getValue() != null && !eventLocationField.getText().isEmpty() ){
            int eventID = Integer.parseInt(eventIdField.getText());
            String type = eventTypeField.getText();
            int hour = (int) eventHourSpinner.getValue();
            int minute = (int) eventMinuteSpinner.getValue();
            LocalDateTime startTime = LocalDateTime.of(eventStartDatePicker.getValue(), LocalTime.of(hour, minute));
            LocalDateTime endTime = LocalDateTime.of(eventEndDatePicker.getValue(), LocalTime.MIN);
            String notes = eventNotesField.getText();
            String guidance = locationGuidanceField.getText();

            Event event = new Event(eventID,type,eventLocationField.getText(), startTime, endTime, notes, guidance);
            eventModel.manageEvent(event);
            controller.refreshTable();
        }

    }

    public void closeWindow(ActionEvent actionEvent) {

        Stage currentStage = (Stage) eventTypeField.getScene().getWindow();
        currentStage.close();
    }
}
