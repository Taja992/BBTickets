package DAL;

import BE.User;
import Exceptions.BBExceptions;

import java.sql.*;
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

    public User getUser(String username, String password) throws BBExceptions {
        String sql = "SELECT * FROM [User] WHERE username = ? AND password = ?";

        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int user_type = resultSet.getInt("user_type");
                User user = new User(user_type, password, username);
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
                int user_type = resultSet.getInt("user_type");
                String password = resultSet.getString("password");
                String username = resultSet.getString("username");
                User user = new User(user_type, password, username);
                allUsers.add(user);
            }
        } catch (SQLException e) {
            throw new BBExceptions("Failed to retrieve all users", e);
        }

        return allUsers;
    }


}