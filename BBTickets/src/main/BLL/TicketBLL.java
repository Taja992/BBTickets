package BLL;

import BE.Customer;
import BE.Event;
import DAL.TicketDAO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

public class TicketBLL {

    private TicketDAO DAO = new TicketDAO();

    public void createTicket(String type, int customerId, int eventId, double price, String UUID){
        DAO.createTicket(type, customerId, eventId, price, UUID);
    }

    public void createTicket(String type, int customerId, int eventId, String UUID){
        DAO.createTicket(type, customerId, eventId, UUID);
    }

    public void printTicketWithInfo(int width, int height, Customer cust, Event event, String type, double price, String uuid, String fileLocation) throws IOException  {
        PDDocument ticketDoc = new PDDocument(); //the document itself
        PDRectangle pageSize = new PDRectangle(width, height); //we can set the page size to be the same as a rectangle, thus making it customizable
        PDPage page = new PDPage(pageSize); //the page, it's size coming from the rectangle
        ticketDoc.addPage(page);

        PDPageContentStream stream = new PDPageContentStream(ticketDoc, page);


        stream.beginText();
        //note to self: 0,0 is the bottom left of the page

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
        if(price >=0){ //if the user doesn't enter a price, it gets set as -1, so this code only works the user doesn't enter a price or sets it to negative
            stream.newLine();
            stream.showText("Price: " + price);
        }

        stream.endText();


        BitMatrix bitMatrix = null;
        try {
            //creating a barcode (a Code128 barcode specifically) (I don't know what that means either)
            bitMatrix = new Code128Writer().encode(uuid, BarcodeFormat.CODE_128, (int) (width*0.1), (int) (height*0.08), null);
            BufferedImage bfImg = MatrixToImageWriter.toBufferedImage(bitMatrix);
            PDImageXObject xObject = JPEGFactory.createFromImage(ticketDoc, bfImg); //creating an object from the buffered image so it can be put on the doc
            stream.drawImage(xObject, 0, 5); //actually drawing the image on the document
            /*
                another note to self: the coordinates for drawing an image are based on the
                previous thing drawn, including text. so the above barcode coordinates have
                to be relative to the last piece of text I drew (I'm screaming into my pillow)
             */

            //now doing the same but with QR codes
            bitMatrix = new QRCodeWriter().encode(uuid, BarcodeFormat.QR_CODE, 20, 20, null);
            bfImg = MatrixToImageWriter.toBufferedImage(bitMatrix);
            xObject = JPEGFactory.createFromImage(ticketDoc, bfImg);
            stream.drawImage(xObject, 0, (int) (height*0.1));

        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

        stream.close();

        String eventType = event.getEventType();
        if(eventType.contains("?")){ //files can't be saved if their name contains "?" so I just wrote a thing to get rid of those
            eventType = eventType.replace('?', ' ');
        }
        ticketDoc.save(fileLocation + "\\Ticket For " + cust.getCustomerName() + " to " + eventType + ".pdf");
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

    public String generateUUID(){
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        return  uuidStr;

    }

}
