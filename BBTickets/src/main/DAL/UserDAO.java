package DAL;

import BE.Event;
import BE.User;
import Exceptions.BBExceptions;


import java.io.IOException;


import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private ConnectionManager connectionManager;

    public UserDAO() {
        connectionManager = new ConnectionManager();
    }

    public void newUser(User user) throws BBExceptions {
        String sql = "INSERT INTO [User] (user_type, password, username, profile_picture) VALUES (?, ?, ?, ?)";

        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, user.getUser_type());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getUsername());

            // Set the profile picture as null
            statement.setNull(4, Types.BINARY);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BBExceptions("Failed to insert user", e);
        }
    }

    public void updateUser(User user) throws BBExceptions {
        String sql = "UPDATE [User] SET user_type = ?, password = ?, username = ?, profile_picture = ? WHERE user_id = ?";

        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, user.getUser_type());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getUsername());
            statement.setBytes(4, user.getProfilePicture());
            statement.setInt(5, user.getUserId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BBExceptions("Failed to update user", e);
        }
    }
    public void updateProfilePicture(int userId, byte[] profilePicture) throws SQLException {
        String sql = "UPDATE [User] SET profile_picture = ? WHERE user_id = ?";
        Connection connection = connectionManager.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setBytes(1, profilePicture);
        pstmt.setInt(2, userId);
        pstmt.executeUpdate();
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
                byte[] profilePicture = resultSet.getBytes("profile_picture");
                User user = new User(userId, user_type, password, username, profilePicture);
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
                byte[] profilePicture = resultSet.getBytes("profile_picture");
                User user = new User(userID, user_type, password, username, profilePicture);
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
                byte[] profilePicture = resultSet.getBytes("profile_picture");
                if (resultSet.wasNull()) {
                    profilePicture = null;
                }

                // Add other user fields as needed
                User user = new User(userId, username, profilePicture); // Include the profile picture when creating the User object
                eventUsers.add(user);
            }
        } catch (SQLException e) {
            throw new BBExceptions("Failed to retrieve users for event", e);
        }

        return eventUsers;
    }

    public void deleteUser(User selectedUser) throws BBExceptions {
        String sql = "DELETE FROM [User] WHERE user_id = ?";

        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, selectedUser.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}