package DAL;

import BE.Event;
import Exceptions.BBExceptions;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Exceptions.BBExceptions;

public class EventDAO {

    private final ConnectionManager connectionManager;

    public EventDAO(){
        connectionManager = new ConnectionManager();
    }

    public int newEvent(Event event) throws BBExceptions {
        String sql = "INSERT INTO EventTable (event_type, event_location, event_start_time, event_ending_time, event_notes, location_guidance) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            prepareStatementWithEventDetails(statement, event);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating event failed, no rows affected.");
            }

            return getGeneratedKey(statement);
        } catch (SQLException e) {
            throw new BBExceptions("Failed to insert event", e);
        }
    }

    private void prepareStatementWithEventDetails(PreparedStatement statement, Event event) throws SQLException {
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
    }

    private int getGeneratedKey(PreparedStatement statement) throws SQLException {
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating event failed, no ID obtained.");
            }
        }
    }

    public void deleteEvent(int Id) throws BBExceptions {
        String sql = "DELETE EventTable FROM EventTable WHERE EventTable.event_id = ? ";

        try(Connection con = connectionManager.getConnection()){

            PreparedStatement pstmnt = con.prepareStatement(sql);
            pstmnt.setString(1, String.valueOf(Id));

            pstmnt.executeUpdate();

        }  catch (SQLException e) {
            throw new BBExceptions("Failed to get delete event", e);
        }

    }

    public void manageEvent(Event event) throws BBExceptions {
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

            pstmnt.executeUpdate();
        }  catch (SQLException e) {
            throw new BBExceptions("Failed to edit event", e);
        }
    }
    
    public List<Event> getAllEvents() throws BBExceptions {
        List<Event> allEvents = new ArrayList<>();

        String sql = "SELECT * FROM EventTable";

        try(Connection connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                int eventId = rs.getInt("event_id");
                String eventType = rs.getString("event_type");
                String eventLocation = rs.getString("event_location");
                LocalDateTime eventStartTime = rs.getTimestamp("event_start_time").toLocalDateTime();
                Timestamp eventEndingTimeTs = rs.getTimestamp("event_ending_time"); // Get the event ending time from the current row
                LocalDateTime eventEndingTime = eventEndingTimeTs != null ? eventEndingTimeTs.toLocalDateTime() : null; // Convert the event ending time to a LocalDateTime if it's not null
                String eventNotes = rs.getString("event_notes");
                String locationGuidance = rs.getString("location_guidance");

                Event event = new Event(eventId, eventType, eventLocation, eventStartTime, eventEndingTime, eventNotes, locationGuidance);
                allEvents.add(event);
            }
        } catch (SQLException e) {
            throw new BBExceptions("Failed to get all events", e);
        }
        return allEvents;
    }

    public void assignUserToEvent(int userId, int eventId) throws BBExceptions {
        String sql = "INSERT INTO EventCoordCon (user_Id, event_Id) VALUES (?, ?)";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, eventId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new BBExceptions("Failed to assign user to event", e);
        }
    }

    public void removeUserFromEvent(int userId, int eventId) throws BBExceptions {
        String sql = "DELETE FROM EventCoordCon WHERE user_id = ? AND event_Id = ?";

        try(Connection cm = connectionManager.getConnection();
        PreparedStatement pstmt = cm.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, eventId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new BBExceptions("Failed to remove user from event");
        }
    }

}
