package GUI.controller;


import BE.Customer;
import BE.Event;
import BE.TicketType;
import Exceptions.BBExceptions;
import GUI.model.CustomerModel;
import GUI.model.TicketModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    private List<TicketType> types = new ArrayList<>();
    private List<String> typesForBox = new ArrayList<>();

    private String uuid = "";

    private Button createTicketBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allCustomers.addAll(custModel.getAllCustomers());
        showCustomers();

        types.addAll(ticketModel.getAllTypes());

        for(TicketType type : types){
            typesForBox.add(type.getName());
        }

        typeChcBox.getItems().addAll(typesForBox);
        typeChcBox.setValue("Standard");
    }

    public void setCreateTicketBtn(Button createTicketBtn) {
        this.createTicketBtn = createTicketBtn;
    }

    public void closeWindow(ActionEvent actionEvent) {
        // Re-enable the button
        if (createTicketBtn != null) {
            createTicketBtn.setDisable(false);
        }

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
        if(!typeChcBox.getValue().isEmpty() && !filelocationTxt.getText().isEmpty()){
            if(newCustChkBox.isSelected()){ //make new ticket and customer
                if(!custNameTxt.getText().isEmpty() && !custEmailTxt.getText().isEmpty()){
                    Customer cust = new Customer(custModel.getLastCustomerID()+1, custNameTxt.getText(), custEmailTxt.getText());

                    //checking if a pdf of the same name exists (could cause corruption otherwise)
                    if(!ticketModel.doesPDFExist(filelocationTxt.getText() + "\\Ticket For " + cust.getCustomerName() + " to " + selectedEvent.getEventType() + ".pdf")){
                        custModel.newCustomer(cust);
                        showCustomers(); //to refresh the table with the new customer
                        finalizeTicket(cust);
                    } else{

                        if(saveOverPDFConfirmation()){
                            custModel.newCustomer(cust);
                            showCustomers(); //to refresh the table with the new customer
                            finalizeTicket(cust);
                        }
                    }
                }
            } else {
                //just make new ticket with selected customer
                if(customerLv.getSelectionModel().getSelectedItem() != null){
                    Customer cust = allCustomers.get(customerLv.getSelectionModel().getSelectedIndex());

                    //checking if pdf of the same name exists
                    if(!ticketModel.doesPDFExist(filelocationTxt.getText() + "\\Ticket For " + cust.getCustomerName() + " to " + selectedEvent.getEventType() + ".pdf")){
                        finalizeTicket(cust);
                    } else{
                        if(saveOverPDFConfirmation()){
                            finalizeTicket(cust);
                        }
                    }
                } else {
                    showErrorDialog("Empty Fields", "Please make sure you select a customer");
                }
            }


        } else {
            showErrorDialog("Empty Fields", "Please make sure you select a ticket type and file location");
        }
    }

    //I know the name is a bit confusing but this method exists because these lines of code are repeated
    private void finalizeTicket( Customer cust) throws IOException, BBExceptions {

        uuid = ticketModel.generateUUID();
        int typeId = types.get(typeChcBox.getSelectionModel().getSelectedIndex()).getId();

        if(!priceTxt.getText().isEmpty()){
            double price = Double.parseDouble(priceTxt.getText());
            ticketModel.createTicket(typeId, cust.getCustId(), selectedEvent.getEventId(), price, uuid);
            printTicket(cust);

        } else {
            ticketModel.createTicket(typeId, cust.getCustId(), selectedEvent.getEventId(), uuid);
            printTicket(cust);
        }
    }


    //method for saving a ticket pdf file
    public void printTicket(Customer cust) throws BBExceptions {
        int width = 1168;
        int height = 544;

        String type = typeChcBox.getSelectionModel().getSelectedItem();

        double price = -1;
        if(!priceTxt.getText().isEmpty()){
            price = Double.parseDouble(priceTxt.getText());
        }

        try {
            ticketModel.printTicketWithInfo(width,height,cust,selectedEvent, type, price, uuid, filelocationTxt.getText());
        } catch (IOException e) {
            showErrorDialog("error with file", "There was an error saving this file. If you're trying to save over another file, it may be because you have it open");
            throw new RuntimeException(e);
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

    private boolean saveOverPDFConfirmation(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Trying to save over existing pdf");
        alert.setHeaderText(null);
        alert.setContentText("This will create a new ticket with the same name");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }

    }

    public void reprintTktBtn(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/reprintTicket.fxml"));
        Parent root = loader.load();

        ReprintTicketController controller = loader.getController();
        controller.setEventId(selectedEvent.getEventId());
        controller.setEvent(selectedEvent);

        Customer selectedCustomer = customerLv.getSelectionModel().getSelectedItem();
        controller.setCustomer(selectedCustomer);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
