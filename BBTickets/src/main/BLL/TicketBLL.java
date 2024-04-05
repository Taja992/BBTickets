package BLL;

import BE.Customer;
import BE.Event;
import DAL.TicketDAO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;

public class TicketBLL {

    private TicketDAO DAO = new TicketDAO();

    public void createTicket(String type, int customerId, int eventId, double price){
        DAO.createTicket(type, customerId, eventId, price);
    }

    public void createTicket(String type, int customerId, int eventId){
        DAO.createTicket(type, customerId, eventId);
    }

    public void printTicketWithInfo(int width, int height, Customer cust, Event event, String type, double price, String fileLocation) throws IOException {
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
        if(price >=0){
            stream.newLine(); //"newLine()" makes a new line with the spacing set by the "setLeading()" method
            stream.showText("Price: " + price);
        }


        stream.endText();

        stream.close();

        String eventType = event.getEventType();
        if(eventType.contains("?")){
            eventType = eventType.replace('?', ' ');
        }
        ticketDoc.save(fileLocation + "\\Ticket For " + eventType + ".pdf");
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

}
