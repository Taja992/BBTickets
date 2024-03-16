package DAL;

import BE.Event;
import Exceptions.BBExceptions;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;

import Exceptions.BBExceptions;

public class EventDAO {

    private ConnectionManager connectionManager;

    public EventDAO(){
        connectionManager = new ConnectionManager();
    }

    public void newEvent(Event event) throws BBExceptions {
        String sql = "INSERT INTO EventTable (event_type, event_location, event_start_time, event_ending_time, event_notes, location_guidance) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, event.getEventType());
            statement.setString(2, event.getEventLocation());
            statement.setTimestamp(3, Timestamp.valueOf(event.getEventStartTime()));
            if (event.getEventEndingTime() != null) {
                statement.setTimestamp(4, Timestamp.valueOf(event.getEventEndingTime()));
            } else {
                statement.setNull(4, Types.TIMESTAMP);
            }
            statement.setString(5, event.getEventNotes());
            statement.setString(6, event.getLocationGuidance());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BBExceptions("Failed to insert event", e);
        }
    }

    public void deleteEvent(int Id) throws BBExceptions {
        String sql = "DELETE FROM EventTable WHERE event_id = ?";
        try(Connection con = connectionManager.getConnection();){
            PreparedStatement pstmnt = con.prepareStatement(sql);
            pstmnt.setString(1, String.valueOf(Id));

            pstmnt.executeUpdate();

        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void manageEvent(Event event) throws BBExceptions, SQLServerException {
        String sql = "UPDATE EventTable SET " +
                "event_type = ?, event_location = ?, event_start_time = ?, " +
                "event_ending_time = ?, event_notes = ?, location_guidance = ? WHERE event_id = ?";

        try(Connection con = connectionManager.getConnection();){
            PreparedStatement pstmnt = con.prepareStatement(sql);
            pstmnt.setString(1, event.getEventType());
            pstmnt.setString(2, event.getEventLocation());
            pstmnt.setTimestamp(3, Timestamp.valueOf(event.getEventStartTime()));
            if(event.getEventEndingTime()!= null){
                pstmnt.setTimestamp(4, Timestamp.valueOf(event.getEventStartTime()));
            } else{
                pstmnt.setNull(4, Types.TIMESTAMP);
            }
            pstmnt.setString(5, event.getEventNotes());
            pstmnt.setString(6, event.getLocationGuidance());
            pstmnt.setInt(7, event.getEventId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
