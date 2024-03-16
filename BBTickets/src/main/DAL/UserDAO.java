package DAL;
import BE.Event;
import BE.User;
import Exceptions.BBExceptions;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;

import Exceptions.BBExceptions;
public class UserDAO {
    private ConnectionManager connectionManager;

    public UserDAO() {
        connectionManager = new ConnectionManager();
    }

    public void newUser(User user) throws BBExceptions {
        String sql = "INSERT INTO UserType (admin, event_controller, password, username) VALUES (?, ?, ?, ?)";

        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getAdmin());
            statement.setString(2, user.getEvent_controller());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getUsername());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BBExceptions("Failed to insert user", e);
        }
    }

    public User getUser(String username, String password) throws BBExceptions {
        String sql = "SELECT * FROM UserType WHERE username = ? AND password = ?";

        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String admin = resultSet.getString("admin");
                String event_controller = resultSet.getString("event_controller");
                User user = new User(admin, event_controller, password, username);
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new BBExceptions("Failed to retrieve user", e);
        }
    }
}
