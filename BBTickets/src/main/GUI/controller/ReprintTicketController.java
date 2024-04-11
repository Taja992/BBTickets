package GUI.controller;

import BE.Customer;
import BE.Event;
import BE.Ticket;
import GUI.model.TicketModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReprintTicketController {

    @FXML
    private ListView<Ticket> userTktLv;
    private Event event;
    private Customer customer;
    private TicketModel ticketModel;
    private int eventId;


    public ReprintTicketController() {
        ticketModel = new TicketModel();
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        showTickets();
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
        showTickets();
    }


    public void reprintTkt(ActionEvent actionEvent) {
        Ticket selectedTicket = userTktLv.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            try {
                Event selectedEvent = this.event;

                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory = directoryChooser.showDialog(new Stage());

                if (selectedDirectory != null) {
                    String fileLocation = selectedDirectory.getAbsolutePath();

                    ticketModel.printTicketWithInfo(
                            1168, // width
                            544, // height
                            customer,
                            selectedEvent,
                            selectedTicket.getType(),
                            selectedTicket.getPrice(),
                            selectedTicket.getUuid(),
                            fileLocation // Use the selected directory
                    );
                } else {
                    showErrorDialog("No Directory Selected", "Please select a directory to save the PDF.");
                }
            } catch (IOException e) {
                showErrorDialog("Error with file", "There was an error saving this file. If you're trying to save over another file, it may be because you have it open");
                throw new RuntimeException(e);
            }
        } else {
            showErrorDialog("No Ticket Selected", "Please select a ticket to reprint.");
        }
    }

    public void showTickets() {
        if (customer != null && eventId != 0) {
            int customerId = customer.getCustId();

            List<Ticket> tickets = ticketModel.getTickets(customerId, eventId);

            ObservableList<Ticket> observableList = FXCollections.observableArrayList(tickets);
            userTktLv.setItems(observableList);
        }
    }

    private void showErrorDialog(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}