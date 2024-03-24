package GUI.model;

import BE.User;
import BLL.UserBLL;
import Exceptions.BBExceptions;
import java.util.List;

public class UserModel {
    private UserBLL userBLL;

    public UserModel() {
        userBLL = new UserBLL();
    }

    public void newUser(User user) throws BBExceptions {
        userBLL.newUser(user);
    }
    public void updateUser(User user) throws BBExceptions {
        userBLL.updateUser(user);
    }
    public List<User> getAllUsers() throws BBExceptions {
        return userBLL.allUsers();
    }
    public User getUser(String username, String password) throws BBExceptions {
        return userBLL.getUser(username, password);
    }
    public List<User> getUsersForEvent(int eventId) throws BBExceptions {
        return userBLL.getUsersForEvent(eventId);
    }
}
