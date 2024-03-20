package GUI.controller;

import BLL.TicketBLL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateTicketController {
    @FXML
    private TextField eventTypeField;
    @FXML
    private TextField customerIDField;
    @FXML
    private TextField eventIDField;
    @FXML
    private TextField priceField;

    // BLL needs to be changed to TicketModel
    private TicketBLL ticketBLL = new TicketBLL();

    public void closeWindow(ActionEvent actionEvent) {
        // Get the current stage from the action event and close it
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void addTicket(ActionEvent actionEvent) {
        if(!eventIDField.getText().isEmpty() && !customerIDField.getText().isEmpty()){
            String eventType = eventTypeField.getText();
            int eventId = Integer.parseInt(eventIDField.getText());
            int customerId = Integer.parseInt(customerIDField.getText());
            double price = Double.parseDouble(priceField.getText());

            ticketBLL.createTicket(eventType, eventId, customerId, price);
        }

    }
}
