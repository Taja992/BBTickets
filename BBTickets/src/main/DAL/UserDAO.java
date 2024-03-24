package DAL;

import BE.Event;
import BE.User;
import Exceptions.BBExceptions;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private ConnectionManager connectionManager;

    public UserDAO() {
        connectionManager = new ConnectionManager();
    }

    public void newUser(User user) throws BBExceptions {
        String sql = "INSERT INTO [User] (user_type, password, username) VALUES (?, ?, ?)";

        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, user.getUser_type());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getUsername());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BBExceptions("Failed to insert user", e);
        }
    }

    public void updateUser(User user) throws BBExceptions {
        String sql = "UPDATE [User] SET user_type = ?, password = ?, username = ? WHERE user_id = ?";

        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, user.getUser_type());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getUsername());
            statement.setInt(4, user.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BBExceptions("Failed to update user", e);
        }
    }

    public User getUser(String username, String password) throws BBExceptions {
        String sql = "SELECT * FROM [User] WHERE username = ? AND password = ?";

        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                int user_type = resultSet.getInt("user_type");
                User user = new User(userId, user_type, password, username);
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new BBExceptions("Failed to retrieve user", e);
        }
    }


    public List<User> allUsers() throws BBExceptions {
        List<User> allUsers = new ArrayList<>();
        String sql = "SELECT * FROM [User]";

        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int userID = resultSet.getInt("user_id");
                int user_type = resultSet.getInt("user_type");
                String password = resultSet.getString("password");
                String username = resultSet.getString("username");
                User user = new User(userID, user_type, password, username);
                allUsers.add(user);
            }
        } catch (SQLException e) {
            throw new BBExceptions("Failed to retrieve all users", e);
        }

        return allUsers;
    }

    public List<Event> getEventsForUser(int userId) throws BBExceptions {
        List<Event> userEvents = new ArrayList<>();
        String sql = "SELECT E.* FROM EventTable E INNER JOIN EventCoordCon ECC ON E.event_id = ECC.event_Id WHERE ECC.user_Id = ?";

        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int eventId = resultSet.getInt("event_id");
                String eventType = resultSet.getString("event_type");
                String eventLocation = resultSet.getString("event_location");
                Timestamp startTimestamp = resultSet.getTimestamp("event_start_time");
                //check for null
                LocalDateTime eventStartTime = startTimestamp != null ? startTimestamp.toLocalDateTime() : null;
                Timestamp endTimestamp = resultSet.getTimestamp("event_ending_time");
                //check for null
                LocalDateTime eventEndingTime = endTimestamp != null ? endTimestamp.toLocalDateTime() : null;
                String eventNotes = resultSet.getString("event_notes");
                String locationGuidance = resultSet.getString("location_guidance");

                Event event = new Event(eventId, eventType, eventLocation, eventStartTime, eventEndingTime, eventNotes, locationGuidance);
                userEvents.add(event);
            }
        } catch (SQLException e) {
            throw new BBExceptions("Failed to retrieve events for user", e);
        }

        return userEvents;
    }

    public List<User> getUsersForEvent(int eventId) throws BBExceptions {
        List<User> eventUsers = new ArrayList<>();
        String sql = "SELECT U.* FROM [User] U INNER JOIN EventCoordCon ECC ON U.user_id = ECC.user_Id WHERE ECC.event_Id = ?";

        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, eventId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                // Add other user fields as needed

                User user = new User(userId, username);
                eventUsers.add(user);
            }
        } catch (SQLException e) {
            throw new BBExceptions("Failed to retrieve users for event", e);
        }

        return eventUsers;
    }
}