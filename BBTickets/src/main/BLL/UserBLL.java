package BLL;

import BE.Event;
import BE.User;
import DAL.UserDAO;
import Exceptions.BBExceptions;

import java.util.List;

public class UserBLL {
    UserDAO userDAO = new UserDAO();

    public void newUser(User user) throws BBExceptions {
        userDAO.newUser(user);
    }

    public User getUser(String username, String password) throws BBExceptions {
        return userDAO.getUser(username, password);
    }

    //this code needs to be moved to UserModel at some point -> public List<User> getAllUsers() throws BBExceptions
    public List<User> allUsers() throws BBExceptions {
        List<User> allUsers = userDAO.allUsers();
        for (User user : allUsers) {
            String roleName = switch (user.getUser_type()) {
                case 0 -> "Event Coordinator";
                case 1 -> "Admin";
                default -> "Unknown";
            };
            user.setRoleName(roleName);
        }
        return allUsers;
    }

    public List<Event> getEventsForUser(int userId) throws BBExceptions {
        return userDAO.getEventsForUser(userId);
    }

    public List<User> getUsersForEvent(int eventId) throws BBExceptions {
        return userDAO.getUsersForEvent(eventId);
    }
}


