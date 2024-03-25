package BLL;

import BE.Event;
import BE.User;
import DAL.UserDAO;
import Exceptions.BBExceptions;
import java.util.List;

public class UserBLL {
    private UserDAO userDAO;

    public UserBLL() {
        userDAO = new UserDAO();
    }

    public void newUser(User user) throws BBExceptions {
        userDAO.newUser(user);
    }

    public void updateUser(User user) throws BBExceptions {
        userDAO.updateUser(user);
    }

    public List<User> allUsers() throws BBExceptions {
        return userDAO.allUsers();
    }

    public User getUser(String username, String password) throws BBExceptions {
        return userDAO.getUser(username, password);
    }

    public List<Event> getEventsForUser(int userId) throws BBExceptions {
        return userDAO.getEventsForUser(userId);
    }

    public List<User> getUsersForEvent(int eventId) throws BBExceptions {
        return userDAO.getUsersForEvent(eventId);
    }

    public void deleteUser(User selectedUser) throws BBExceptions {
        userDAO.deleteUser(selectedUser);
    }
}