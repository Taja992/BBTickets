package GUI.model;

import BE.User;
import BLL.UserBLL;
import DAL.UserDAO;
import Exceptions.BBExceptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserModel {

    private UserDAO userDAO;
    private UserBLL userBLL;
    private ObservableList<User> allUsers;
    private Map<Integer, ObservableList<User>> usersForEvent = new HashMap<>();

    public UserModel() {
        userBLL = new UserBLL();
        allUsers = FXCollections.observableArrayList();
        loadUsers();
        userDAO = new UserDAO();

    }

    private void loadUsers() {
        try {
            allUsers.setAll(userBLL.allUsers());
        } catch (BBExceptions e) {
            e.printStackTrace();
        }
    }

    public ObservableList<User> getAllUsers() {
        return allUsers;
    }

    public void newUser(User user) throws BBExceptions {
        userBLL.newUser(user);
        loadUsers();
    }

    public void updateUser(User user) throws BBExceptions {
        userBLL.updateUser(user);
        loadUsers();
    }


    public User getUser(String username, String password) throws BBExceptions {
        return userBLL.getUser(username, password);
    }


    public ObservableList<User> getUsersForEvent(int eventId) throws BBExceptions {
        usersForEvent.put(eventId, FXCollections.observableArrayList(userBLL.getUsersForEvent(eventId)));
        return usersForEvent.get(eventId);
    }

    public void deleteUser(User selectedUser) throws BBExceptions {
        userBLL.deleteUser(selectedUser);
        loadUsers();
    }

    public ObservableList<User> getUsersByType(int userType) {
        return allUsers.filtered(user -> user.getUser_type() == userType);
    }

    public void editProfile(String username, String password, String picturePath) {
        User user = new User(0, password, username);
        user.setProfilePicture(picturePath.getBytes());
        try {
            userBLL.updateUser(user);
        } catch (BBExceptions e) {
            e.printStackTrace();
        }
    }

    public User getUserById(int userId) throws BBExceptions {
        for (User user : allUsers) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        throw new BBExceptions("User with id " + userId + " not found.");
    }

    public void updateProfilePicture(int userId, byte[] pictureData) throws SQLException, SQLException {
        userDAO.updateProfilePicture(userId, pictureData);
    }
}