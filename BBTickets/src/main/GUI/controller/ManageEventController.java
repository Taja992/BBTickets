package GUI.controller;

import BE.Event;
import Exceptions.BBExceptions;
import GUI.model.EventModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ManageEventController {


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
    @FXML
    private Button  editEventBtn;

    private int eventId;

    EcDashboardController controller;

    //This needs to be changed to use the model instead of BLL
    private EventModel eventModel = new EventModel();

    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 0, 15);
        eventMinuteSpinner.setValueFactory(valueFactory);
        eventMinuteSpinner.setEditable(true);
        eventHourSpinner.setEditable(true);
    }

    public void setEvent(Event event){
        this.eventId = event.getEventId();
        eventTypeField.setText(event.getEventType());
        eventLocationField.setText(event.getEventLocation());
        eventStartDatePicker.setValue(event.getEventStartTime().toLocalDate());
        eventHourSpinner.getValueFactory().setValue(event.getEventStartTime().getHour());
        eventMinuteSpinner.getValueFactory().setValue(event.getEventStartTime().getMinute());
        if(event.getEventEndingTime() != null){
            eventEndDatePicker.setValue(event.getEventEndingTime().toLocalDate());
        }
        eventNotesField.setText(event.getEventNotes());
        locationGuidanceField.setText(event.getLocationGuidance());
    }

    public void setEditEventBtn(Button editEventBtn) {
        this.editEventBtn = editEventBtn;
    }

    public void setDashboard(EcDashboardController controller){
        this.controller = controller;
    }

    public void manageEvent(ActionEvent actionEvent) throws BBExceptions {
        if(!eventTypeField.getText().isEmpty()
                && eventStartDatePicker.getValue() != null && !eventLocationField.getText().isEmpty() ){
            String type = eventTypeField.getText();
            int hour = (int) eventHourSpinner.getValue();
            int minute = (int) eventMinuteSpinner.getValue();
            LocalDateTime startTime = LocalDateTime.of(eventStartDatePicker.getValue(), LocalTime.of(hour, minute));
            LocalDateTime endTime = null;
            if (eventEndDatePicker.getValue() != null) {
                endTime = LocalDateTime.of(eventEndDatePicker.getValue(), LocalTime.MIN);
            }
            String notes = eventNotesField.getText();
            String guidance = locationGuidanceField.getText();

            Event event = new Event(eventId, type,eventLocationField.getText(), startTime, endTime, notes, guidance);
            eventModel.manageEvent(event);
            controller.refreshTable();
        }
        closeWindow(actionEvent);
    }

    public void closeWindow(ActionEvent actionEvent) {
        // Re-enable the button
        if (editEventBtn != null) {
            editEventBtn.setDisable(false);
        }

        // Get the current stage from the action event and close it
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
