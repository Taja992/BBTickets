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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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

    public void addTicket(ActionEvent actionEvent) {
        if(customerLv.getSelectionModel().getSelectedItem() != null){
            Customer cust = allCustomers.get(customerLv.getSelectionModel().getSelectedIndex());


            double price = 0;
            if(!priceTxt.getText().isEmpty()){
                price = Double.parseDouble(priceTxt.getText());

            }

            //change type to be either VIP or Standard (or any other options)
            ticketModel.createTicket(selectedEvent.getEventType(), cust.getCustId(), selectedEvent.getEventId(), price);
        }


    }


    public void printTicket(ActionEvent actionEvent) throws IOException, URISyntaxException, BBExceptions {
        int width = 450;
        int height = 300;

        if(!filelocationTxt.getText().isEmpty() && !typeChcBox.getValue().isEmpty()){
            String type = typeChcBox.getSelectionModel().getSelectedItem();

            if(newCustChkBox.isSelected()){
                //create a new customer if the new customer checkbox is selected
                if(!custNameTxt.getText().isEmpty() && !custEmailTxt.getText().isEmpty()){
                    Customer cust = new Customer(custNameTxt.getText(),custEmailTxt.getText());
                    custModel.newCustomer(cust);
                    printTicketWithInfo(width, height, cust, selectedEvent, type);
                }
            } else{
                if(customerLv.getSelectionModel().getSelectedItem() != null){
                    Customer selectedCust = customerLv.getSelectionModel().getSelectedItem();
                    printTicketWithInfo(width, height, selectedCust, selectedEvent, type);
                }
            }
        }

    }

    private void printTicketWithInfo(int width, int height, Customer cust, Event event, String type) throws IOException {
        PDDocument ticketDoc = new PDDocument();
        PDRectangle pageSize = new PDRectangle(width, height);
        PDPage page = new PDPage(pageSize);
        ticketDoc.addPage(page);

        PDPageContentStream stream = new PDPageContentStream(ticketDoc, page);


        stream.beginText();
        stream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        stream.setLeading(15);
        stream.newLineAtOffset(10,height - 40); //first bit of text
        stream.showText("Customer Name: " + cust.getCustomerName());

        stream.newLineAtOffset(160, 0);
        stream.showText("Customer Email: " + cust.getCustomerEmail());

        //"newLineAtOffset()" creates a new line based on the position of the previous line
        //so newLineAtOffset(0,0) would just make a new line at the same place as the old one,
        //and not at the positions (0,0) in the worldspace. I hope that makes sense
        stream.newLineAtOffset(-160,-15);
        stream.showText("Event: " + event.getEventType());

        stream.newLine(); //"newLine()" makes a new line with the spacing set by the "setLeading()" method
        stream.showText("Ticket Type: " + type);


        stream.endText();

        stream.close();

        String eventType = event.getEventType();
        if(eventType.contains("?")){
            eventType = eventType.replace('?', ' ');
        }
        ticketDoc.save(filelocationTxt.getText() + "\\Ticket For " + eventType + ".pdf");
        ticketDoc.close();

    }


    /*
    //incase I want to add these later
    stream.addRect(390,220,20,20); //new rectangle

    //adding test image
    Path path = Paths.get(ClassLoader.getSystemResource("images/deleteThisLater.png").toURI()); //getting path
    PDImageXObject image = PDImageXObject.createFromFile(path.toAbsolutePath().toString(), document); //converting to image object
    stream.drawImage(image, 0, 469);
     */


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
