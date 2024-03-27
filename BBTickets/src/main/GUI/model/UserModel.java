package GUI.model;

import BE.User;
import BLL.UserBLL;
import Exceptions.BBExceptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserModel {
    private static UserModel instance = null;
    private UserBLL userBLL;
    private ObservableList<User> allUsers;
    private Map<Integer, ObservableList<User>> usersForEvent = new HashMap<>();

    private UserModel() {
        userBLL = new UserBLL();
        allUsers = FXCollections.observableArrayList();
        loadUsers();
    }

    public static UserModel getInstance() {
        if (instance == null) {
            instance = new UserModel();
        }
        return instance;
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
        allUsers.add(user);
    }

    public void updateUser(User user) throws BBExceptions {
        userBLL.updateUser(user);
        int index = allUsers.indexOf(user);
        if (index != -1) {
            allUsers.set(index, user); // Update the user in the ObservableList
        }
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
        allUsers.remove(selectedUser);
    }
}