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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @FXML
    private Button chooseBtn;
    @FXML
    private TextField filelocationTxt;

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

            //change type to be either VIP or Standard (or any other options)
            ticketModel.createTicket(selected.getEventType(), cust.getCustId(), selected.getEventId(), price);
        }


    }


    public void printTicket(ActionEvent actionEvent) throws IOException, URISyntaxException {
        if(!filelocationTxt.getText().isEmpty()){
            PDDocument document = new PDDocument();
            PDRectangle rect = new PDRectangle(0,0,420,550); //we can customize this to be any size
            PDPage page = new PDPage(rect);
            //if you want standard page sizes, you can do something like "PDRectangle.A6"
            //0,0 = bottom left
            document.addPage(page);

            PDPageContentStream stream = new PDPageContentStream(document, page);


            stream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            stream.beginText();
            stream.newLineAtOffset(0,0); //first bit of text
            stream.showText("testing text");

            stream.newLineAtOffset(130,240); //2nd bit of text
            stream.showText("testing text");
            stream.endText();

            stream.addRect(390,220,20,20); //new rectangle

            //adding test image
            Path path = Paths.get(ClassLoader.getSystemResource("images/deleteThisLater.png").toURI()); //getting path
            PDImageXObject image = PDImageXObject.createFromFile(path.toAbsolutePath().toString(), document); //converting to image object
            stream.drawImage(image, 0, 469);

            stream.close();

            document.save(filelocationTxt.getText() + "\\testPDF" + selected.getEventType() + ".pdf");
            document.close();
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
