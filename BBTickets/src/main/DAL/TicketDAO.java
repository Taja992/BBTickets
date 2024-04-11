package DAL;

import BE.Ticket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    private ConnectionManager connectionManager = new ConnectionManager();

    public void createTicket(int typeId, int customerId, int eventId, double price, String UUID) {
        String sql = "insert into Tickets (type, customer_id, event_id, price, uuid) VALUES (?,?,?,?,?)";

        try(Connection con = connectionManager.getConnection()){

            PreparedStatement pstmnt = con.prepareStatement(sql);
            pstmnt.setInt(1, typeId);
            pstmnt.setInt(2, customerId);
            pstmnt.setInt(3, eventId);
            pstmnt.setDouble(4, price);
            pstmnt.setString(5, UUID);
            pstmnt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTicket(int typeId, int customerId, int eventId, String UUID) {
        String sql = "insert into Tickets (type, customer_id, event_id, uuid) VALUES (?,?,?,?)";

        try(Connection con = connectionManager.getConnection()){

            PreparedStatement pstmnt = con.prepareStatement(sql);
            pstmnt.setInt(1, typeId);
            pstmnt.setInt(2, customerId);
            pstmnt.setInt(3, eventId);
            pstmnt.setString(4, UUID);
            pstmnt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ticket> getTickets(int customerId, int eventId) {
        String sql = "SELECT * FROM Tickets WHERE customer_id = ? AND event_id = ?";

        List<Ticket> tickets = new ArrayList<>();

        try (Connection con = connectionManager.getConnection()) {
            PreparedStatement pstmnt = con.prepareStatement(sql);
            pstmnt.setInt(1, customerId);
            pstmnt.setInt(2, eventId);

            ResultSet rs = pstmnt.executeQuery();

            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setType(rs.getString("type"));
                ticket.setCustomerId(rs.getInt("customer_id"));
                ticket.setEventId(rs.getInt("event_id"));
                ticket.setPrice(rs.getDouble("price"));
                ticket.setUUID(rs.getString("uuid"));

                tickets.add(ticket);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tickets;
    }
}
