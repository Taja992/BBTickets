package BLL;

import BE.Customer;
import BE.Event;
import BE.TicketType;
import BE.Ticket;
import DAL.TicketDAO;
import DAL.TicketTypeDAO;
import Exceptions.BBExceptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PageMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class TicketBLL {

    private TicketDAO DAO = new TicketDAO();
    private TicketTypeDAO typeDAO = new TicketTypeDAO();

    public void createTicket(int type, int customerId, int eventId, double price, String UUID){
        DAO.createTicket(type, customerId, eventId, price, UUID);
    }

    public void createTicket(int type, int customerId, int eventId, String UUID){
        DAO.createTicket(type, customerId, eventId, UUID);
    }

    public List<TicketType> getAllTypes(){
        return typeDAO.getAllTypes();
    }

    public void addType( String name) throws BBExceptions {
        typeDAO.addType( name);
    }

    public void removeType(int Id) throws BBExceptions {
        typeDAO.removeType(Id);
    }

    public void printTicketWithInfo(int width, int height, Customer cust, Event event, String type, double price, String uuid, String fileLocation) throws IOException  {

        PDDocument ticketDoc = new PDDocument(); //the document itself
        PDRectangle pageSize = new PDRectangle(width, height); //we can set the page size to be the same as a rectangle, thus making it customizable
        PDPage page = new PDPage(pageSize); //the page, it's size coming from the rectangle
        ticketDoc.addPage(page);

        PDPageContentStream stream = new PDPageContentStream(ticketDoc, page);

        addImages(stream, ticketDoc, width);

        stream.beginText();
        //note to self: 0,0 is the bottom left of the page

        PDType1Font bold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font regular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        stream.setNonStrokingColor(Color.WHITE);

        stream.setFont(bold, 30);
        stream.setLeading(32);
        stream.newLineAtOffset(280,470); //first bit of text (event type)
        stream.showText(event.getEventType());

        //"newLineAtOffset()" creates a new line based on the position of the previous line
        //so newLineAtOffset(0,0) would just make a new line at the same place as the old one,
        //and not at the positions (0,0) in the worldspace. I hope that makes sense
        //"newLine()" makes a new line with the spacing set by the "setLeading()" method

        stream.setFont(regular, 25);

        if(price >=0){ //if the user doesn't enter a price, it gets set as -1, so this code only works the user doesn't enter a price or sets it to negative
            stream.newLineAtOffset(-240,-350);
            stream.showText("Location: " + event.getEventLocation());
            stream.newLine();
            stream.showText("Price: " + price);
            stream.newLineAtOffset(490,32);
            stream.showText("Date: " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(event.getEventStartTime()));
            stream.newLine();
            stream.showText("Open Doors: " + event.getEventStartTime().toString().substring(event.getEventStartTime().toString().lastIndexOf('T')+1));
        } else{
            stream.newLineAtOffset(-240,-350);
            stream.showText("Location: " + event.getEventLocation());
            stream.newLineAtOffset(490,0);
            stream.showText("Date: " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(event.getEventStartTime()));
            stream.newLine();
            stream.showText("Open Doors: " + event.getEventStartTime().toString().substring(event.getEventStartTime().toString().lastIndexOf('T')+1));
        }

        printTypeAndName(stream, bold, regular, type, cust);

        stream.endText();



        BitMatrix bitMatrix = null;
        try {
            //creating a barcode (a Code128 barcode specifically) (I don't know what that means either)
            bitMatrix = new Code128Writer().encode(uuid, BarcodeFormat.CODE_128, 470, 45, null);
            BufferedImage bfImg = MatrixToImageWriter.toBufferedImage(bitMatrix);
            PDImageXObject xObject = JPEGFactory.createFromImage(ticketDoc, bfImg); //creating an object from the buffered image so it can be put on the doc
            //rotating barcode -90 degrees
            AffineTransform at = new AffineTransform(xObject.getHeight(), 0, 0, xObject.getWidth(),width-70, height-35);
            at.rotate(Math.toRadians(-90));
            stream.drawImage(xObject, new Matrix(at)); //drawing image on document



            //now doing the same but with QR codes
            bitMatrix = new QRCodeWriter().encode(uuid, BarcodeFormat.QR_CODE, 200, 200, null);
            bfImg = MatrixToImageWriter.toBufferedImage(bitMatrix);
            xObject = JPEGFactory.createFromImage(ticketDoc, bfImg);
            stream.drawImage(xObject, 850, 30);

        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

        stream.setLineDashPattern(new float[]{10,7}, 0);//makes a line pattern with 3 on, 1 off
        stream.moveTo(width-368, height);
        stream.lineTo(width-368, 0);
        stream.setLineWidth(2f);
        stream.stroke();
        stream.close();

        String baseFilename = fileLocation + "\\Ticket For " + cust.getCustomerName() + " to " + event.getEventType();
        String filename = baseFilename + ".pdf";
        int copyNumber = 2;

        while (doesPDFExist(filename)) {
            filename = baseFilename + " (" + copyNumber + ").pdf";
            copyNumber++;
        }

        ticketDoc.save(filename);
        ticketDoc.close();
    }

    private void printTypeAndName(PDPageContentStream stream, PDType1Font bold, PDType1Font regular, String type, Customer cust) throws IOException {
        stream.setNonStrokingColor(Color.BLACK);
        stream.setFont(bold, 30);
        stream.newLineAtOffset(330, 365);
        stream.showText("Ticket Type:");
        float titleWidth = bold.getStringWidth("Ticket Type:")/1000*30; //dividing by 1000 because the method gives width in 1/1000 and 25 is the font size
        float typeWidth = regular.getStringWidth(type)/1000*25;
        stream.setFont(regular, 25);
        stream.newLineAtOffset(titleWidth/2 - typeWidth/2, -32);
        stream.showText(type);

        float nameWidth = bold.getStringWidth("Name:")/1000*30;
        float nameStartX = typeWidth/2 - nameWidth/2;
        stream.setFont(bold, 30);
        stream.newLineAtOffset(nameStartX,-70);
        stream.showText("Name:");

        stream.setFont(regular, 25);
        stream.newLineAtOffset(nameWidth/2 - (regular.getStringWidth(cust.getCustomerName())/1000*25)/2, -32);
        stream.showText(cust.getCustomerName());

        stream.newLineAtOffset((regular.getStringWidth(cust.getCustomerName())/1000*25)/2 - (regular.getStringWidth(cust.getCustomerEmail())/1000*25)/2, -32);
        stream.showText(cust.getCustomerEmail());
    }

    /*
        float titleWidth1 = bold.getStringWidth("Ticket Type:")/1000*25; //dividing by 1000 because the method gives width in 1/1000 and 25 is the font size
        float typeWidth = regular.getStringWidth(type)/1000*25;
        float nameWidth = bold.getStringWidth("Name:")/1000*25;

     */

    private void addImages(PDPageContentStream stream, PDDocument document, int width){

        try {
            //adding red background image
            Path path = Paths.get(ClassLoader.getSystemResource("images/TicketBackgroundRed.png").toURI()); //getting path
            PDImageXObject image = PDImageXObject.createFromFile(path.toAbsolutePath().toString(), document); //converting to image object
            stream.drawImage(image, 0, 0);

            //adding blue background image
            Path bluePath = Paths.get(ClassLoader.getSystemResource("images/TicketBackgroundBlue.png").toURI()); //getting path
            PDImageXObject blueImage = PDImageXObject.createFromFile(bluePath.toAbsolutePath().toString(), document); //converting to image object
            stream.drawImage(blueImage, width-368, 0); //368=image width, this just means it'll appear to the right of the scene

            //adding blue background image
            Path stockPath = Paths.get(ClassLoader.getSystemResource("images/partyStockPicture.jpg").toURI()); //getting path
            PDImageXObject stockImage = PDImageXObject.createFromFile(stockPath.toAbsolutePath().toString(), document); //converting to image object
            stream.drawImage(stockImage, 90, 185);

            //adding yellow background image
            Path yellowPath = Paths.get(ClassLoader.getSystemResource("images/TicketBackgroundYellow.png").toURI()); //getting path
            PDImageXObject yellowImage = PDImageXObject.createFromFile(yellowPath.toAbsolutePath().toString(), document); //converting to image object
            stream.drawImage(yellowImage, 0, 0);

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean doesPDFExist(String filepath){
        try {
            File file = new File(filepath);
            PDDocument doc = Loader.loadPDF(file);
            doc.close();
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    /*
    //incase I want to add these later
    stream.addRect(390,220,20,20); //new rectangle

    //adding test image
    Path path = Paths.get(ClassLoader.getSystemResource("images/deleteThisLater.png").toURI()); //getting path
    PDImageXObject image = PDImageXObject.createFromFile(path.toAbsolutePath().toString(), document); //converting to image object
    stream.drawImage(image, 0, 469);
     */

    public String generateUUID(){
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        return  uuidStr;

    }

    public List<Ticket> getTickets(int customerId, int eventId) {
        List<Ticket> tickets = DAO.getTickets(customerId, eventId);
        return tickets;
    }

}
