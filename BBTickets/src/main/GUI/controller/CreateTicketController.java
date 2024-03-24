package GUI.controller;


import BE.Customer;
import BE.Event;
import GUI.model.CustomerModel;
import GUI.model.TicketModel;
import GUI.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateTicketController implements Initializable {

    /*
    @FXML
    private TextField eventTypeField;
    @FXML
    private TextField customerIDField;
    @FXML
    private TextField eventIDField;
    */

    @FXML
    private ListView<Customer> customerLv;
    @FXML
    private TextField priceTxt;

    private TicketModel ticketModel = new TicketModel();
    private CustomerModel custModel = new CustomerModel();
    private List<Customer> allCustomers = new ArrayList<>();
    private Event selected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showCustomers();
        allCustomers.addAll(custModel.getAllCustomers());

    }

    public void closeWindow(ActionEvent actionEvent) {
        // Get the current stage from the action event and close it
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void setEvent(Event selected){
        this.selected = selected;
    }

    private void showCustomers(){
        customerLv.getItems().clear();
        customerLv.getItems().addAll(custModel.getAllCustomers());

    }

    public void addTicket(ActionEvent actionEvent) {
        if(customerLv.getSelectionModel().getSelectedItem() != null){
            Customer cust = allCustomers.get(customerLv.getSelectionModel().getSelectedIndex());


            double price = 0;
            if(!priceTxt.getText().isEmpty()){
                price = Double.parseDouble(priceTxt.getText());

            }


            ticketModel.createTicket(selected.getEventType(), cust.getCustId(), selected.getEventId(), price);
        }


    }


}
