package GUI.controller;


import BE.Customer;
import BE.Event;
import BE.TicketType;
import Exceptions.BBExceptions;
import GUI.model.CouponModel;
import GUI.model.CustomerModel;
import GUI.model.TicketModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.Image;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class CreateTicketController implements Initializable {


    public ChoiceBox couponChkBox;
    @FXML
    private ListView<Customer> customerLv;
    @FXML
    private TextField priceTxt;
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
    private CouponModel couponModel = new CouponModel();

    private List<TicketType> types = new ArrayList<>();
    private List<String> typesForBox = new ArrayList<>();
    List<String> couponNotes = couponModel.getAllCouponNotes();

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
        couponChkBox.getItems().addAll(couponNotes);
        couponChkBox.setValue("No Coupon");
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

    public void createCoupon(ActionEvent actionEvent) {
        String couponNote = couponChkBox.getSelectionModel().getSelectedItem().toString();
        String couponUUID = couponModel.getCouponByNote(couponNote).getUuid();
        String directoryPath = filelocationTxt.getText();
        String fileLocation = directoryPath + "/" + couponNote + " Coupon.pdf";

        // Check if directoryPath is not empty
        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Directory path is empty. Please provide a valid directory path.");
            return;
        }

        // Check if directory exists, if not create it
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean isCreated = directory.mkdirs();
            if (!isCreated) {
                showAlert(Alert.AlertType.WARNING, "Failed to create directory. Please check the directory path and permissions.");
                return;
            }
        }

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileLocation));
            document.open();
            document.add(new Paragraph(couponNote));

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(couponUUID, BarcodeFormat.QR_CODE, 200, 200);

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            Image image = Image.getInstance(qrImage, Color.gray);
            document.add(image);

            document.close();
            showAlert(Alert.AlertType.INFORMATION, "Coupon PDF Created!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "An error occurred while creating the PDF.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
