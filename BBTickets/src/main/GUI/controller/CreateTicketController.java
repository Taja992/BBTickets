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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showCustomers();
        allCustomers.addAll(custModel.getAllCustomers());
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
        customerLv.getItems().addAll(custModel.getAllCustomers());

    }

    public void addTicket(ActionEvent actionEvent) throws BBExceptions, IOException {
        if(customerLv.getSelectionModel().getSelectedItem() != null && !typeChcBox.getValue().isEmpty()
                && !filelocationTxt.getText().isEmpty()){
            Customer cust = allCustomers.get(customerLv.getSelectionModel().getSelectedIndex());


            if(!priceTxt.getText().isEmpty()){
                double price = Double.parseDouble(priceTxt.getText());
                ticketModel.createTicket(typeChcBox.getValue(), cust.getCustId(), selectedEvent.getEventId(), price);
                printTicket(actionEvent);

            } else {
                ticketModel.createTicket(typeChcBox.getValue(), cust.getCustId(), selectedEvent.getEventId());
                printTicket(actionEvent);
            }

        }


    }


    public void printTicket(ActionEvent actionEvent) throws IOException, BBExceptions {
        int width = 450;
        int height = 300;

        if(!filelocationTxt.getText().isEmpty() && !typeChcBox.getValue().isEmpty()){
            String type = typeChcBox.getSelectionModel().getSelectedItem();

            double price = -1;
            if(!priceTxt.getText().isEmpty()){
                price = Double.parseDouble(priceTxt.getText());
            }

            if(newCustChkBox.isSelected()){
                //create a new customer if the new customer checkbox is selected
                if(!custNameTxt.getText().isEmpty() && !custEmailTxt.getText().isEmpty()){
                    Customer cust = new Customer(custNameTxt.getText(),custEmailTxt.getText());
                    custModel.newCustomer(cust);
                    ticketModel.printTicketWithInfo(width,height,cust,selectedEvent, type, price, filelocationTxt.getText());
                }
            } else{
                if(customerLv.getSelectionModel().getSelectedItem() != null){
                    Customer selectedCust = customerLv.getSelectionModel().getSelectedItem();
                    ticketModel.printTicketWithInfo(width,height,selectedCust,selectedEvent, type, price, filelocationTxt.getText());
                }
            }
        }

    }


    public void chooseFile(ActionEvent actionEvent) {

        //fileChooser = the thing that lets you open the file explorer
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose location to save your ticket pdf");

        File selected = chooser.showOpenDialog(customerLv.getScene().getWindow()); //getting the selected file

        if(selected != null){

            String fileLocation = selected.getAbsolutePath();

            //for the purposes of this method, we just want a folder to save the pdf,
            //not a file. So we remove the part of the path containing the file
            //and just get the folder that the file is contained in
            if(fileLocation.contains(".")){
                fileLocation = fileLocation.substring(0,fileLocation.lastIndexOf("\\"));
                //the double slash is because "\" makes java ignore the text next to it
            }

            filelocationTxt.setText(fileLocation);

        }

    }
}
