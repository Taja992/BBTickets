package GUI.model;

import BE.User;
import BLL.UserBLL;
import Exceptions.BBExceptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class UserModel {
    private UserBLL userBLL;
    private ObservableList<User> allUsers;

    public UserModel() {
        userBLL = new UserBLL();
        allUsers = FXCollections.observableArrayList();
        loadUsers();
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

    public List<User> getUsersForEvent(int eventId) throws BBExceptions {
        return userBLL.getUsersForEvent(eventId);
    }

    public void deleteUser(User selectedUser) throws BBExceptions {
        userBLL.deleteUser(selectedUser);
        loadUsers();
    }
}