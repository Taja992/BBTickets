package DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TicketDAO {

    private ConnectionManager connectionManager = new ConnectionManager();

    public void createTicket(String type, int customerId, int eventId, double price, String UUID) {
        String sql = "insert into Tickets (type, customer_id, event_id, price, uuid) VALUES (?,?,?,?,?)";

        try(Connection con = connectionManager.getConnection()){

            PreparedStatement pstmnt = con.prepareStatement(sql);
            pstmnt.setString(1, type);
            pstmnt.setInt(2, customerId);
            pstmnt.setInt(3, eventId);
            pstmnt.setDouble(4, price);
            pstmnt.setString(5, UUID);
            pstmnt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void createTicket(String type, int customerId, int eventId, String UUID) {
        String sql = "insert into Tickets (type, customer_id, event_id, uuid) VALUES (?,?,?,?)";

        try(Connection con = connectionManager.getConnection()){

            PreparedStatement pstmnt = con.prepareStatement(sql);
            pstmnt.setString(1, type);
            pstmnt.setInt(2, customerId);
            pstmnt.setInt(3, eventId);
            pstmnt.setString(4, UUID);
            pstmnt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
