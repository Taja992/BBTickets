package BLL;

import BE.User;
import DAL.UserDAO;
import Exceptions.BBExceptions;

import java.util.List;

public class BLLUser {
    UserDAO userDAO = new UserDAO();

    public void newUser(User user) throws BBExceptions {
        userDAO.newUser(user);
    }

    public User getUser(String username, String password) throws BBExceptions {
        return userDAO.getUser(username, password);
    }

    public List<User> allUsers() throws BBExceptions {
        return userDAO.allUsers();
    }
}


