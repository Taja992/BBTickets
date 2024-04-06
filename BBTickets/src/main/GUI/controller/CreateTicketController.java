package GUI.controller;


import BE.Customer;
import BE.Event;
import Exceptions.BBExceptions;
import GUI.model.CustomerModel;
import GUI.model.TicketModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateTicketController implements Initializable {



    @FXML
    private ListView<Customer> customerLv;
    @FXML
    private TextField priceTxt;
    @FXML
    private Button chooseBtn;
    @FXML
    private TextField filelocationTxt;
    @FXML
    private CheckBox newCustChkBox;
    @FXML
    private TextField custNameTxt;
    @FXML
    private TextField custEmailTxt;
    @FXML
    private ChoiceBox<String> typeChcBox;

    private TicketModel ticketModel = new TicketModel();
    private CustomerModel custModel = new CustomerModel();
    private List<Customer> allCustomers = new ArrayList<>();
    private Event selectedEvent;
    private String[] ticketTypes = {"Standard", "VIP"};
    private String uuid = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allCustomers.addAll(custModel.getAllCustomers());
        showCustomers();
        typeChcBox.getItems().addAll(ticketTypes);
    }

    public void closeWindow(ActionEvent actionEvent) {
        // Get the current stage from the action event and close it
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void setEvent(Event selected){
        this.selectedEvent = selected;
    }

    private void showCustomers(){
        customerLv.getItems().clear();
        customerLv.getItems().addAll(allCustomers);

    }

    public void addTicket(ActionEvent actionEvent) throws BBExceptions, IOException {
        if(!typeChcBox.getValue().isEmpty() && !filelocationTxt.getText().isEmpty()){

            if(newCustChkBox.isSelected()){
                if(!custNameTxt.getText().isEmpty() && !custEmailTxt.getText().isEmpty()){
                    Customer cust = new Customer(custModel.getLastCustomerID()+1, custNameTxt.getText(), custEmailTxt.getText());
                    custModel.newCustomer(cust);
                    showCustomers(); //to refresh the table with the new customer
                    finalizeTicket(actionEvent, cust);

                }
            } else {
                if(customerLv.getSelectionModel().getSelectedItem() != null){
                    Customer cust = allCustomers.get(customerLv.getSelectionModel().getSelectedIndex());
                    finalizeTicket(actionEvent, cust);

                }
            }
        } else {
            showErrorDialog("Empty Fields", "Please make sure you select a ticket type and file location");
        }



    }

    //I know the name is a bit confusing but this method exists because these lines of code are repeated
    private void finalizeTicket(ActionEvent actionEvent, Customer cust) throws IOException, BBExceptions {
        uuid = ticketModel.generateUUID();

        if(!priceTxt.getText().isEmpty()){
            double price = Double.parseDouble(priceTxt.getText());
            ticketModel.createTicket(typeChcBox.getValue(), cust.getCustId(), selectedEvent.getEventId(), price, uuid);
            printTicket(actionEvent);

        } else {
            ticketModel.createTicket(typeChcBox.getValue(), cust.getCustId(), selectedEvent.getEventId(), uuid);
            printTicket(actionEvent);
        }
    }


    //method for saving a ticket pdf file
    public void printTicket(ActionEvent actionEvent) throws IOException, BBExceptions {
        int width = 450;
        int height = 300;

        if(!filelocationTxt.getText().isEmpty() && !typeChcBox.getValue().isEmpty()){
            String type = typeChcBox.getSelectionModel().getSelectedItem();

            double price = -1;
            if(!priceTxt.getText().isEmpty()){
                price = Double.parseDouble(priceTxt.getText());
            }
            if(uuid.isEmpty()){ //in case this method wasn't called by the addTicket method. this way it can still have a UUID
                uuid = ticketModel.generateUUID();
            }

            if(newCustChkBox.isSelected()){
                //create a new customer if the new customer checkbox is selected
                if(!custNameTxt.getText().isEmpty() && !custEmailTxt.getText().isEmpty()){
                    Customer cust = new Customer(custNameTxt.getText(),custEmailTxt.getText());

                    ticketModel.printTicketWithInfo(width,height,cust,selectedEvent, type, price, uuid, filelocationTxt.getText());
                }
            } else{
                //uses the selected customer to print tickert
                if(customerLv.getSelectionModel().getSelectedItem() != null){
                    Customer selectedCust = customerLv.getSelectionModel().getSelectedItem();
                    ticketModel.printTicketWithInfo(width,height,selectedCust,selectedEvent, type, price, uuid, filelocationTxt.getText());
                }
            }
        }

    }


    public void chooseFile(ActionEvent actionEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose location to save your ticket pdf");

        File directory = chooser.showDialog(customerLv.getScene().getWindow()); //getting the selected folder

        if(directory != null){
            String folderLocation = directory.getAbsolutePath();
            filelocationTxt.setText(folderLocation);
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
