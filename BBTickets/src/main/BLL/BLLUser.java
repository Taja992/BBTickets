package BLL;

import BE.Event;
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
        List<Event> events = userDAO.getEventsForUser(userId);
        // Check if the list is empty
        if (events.isEmpty()) {
            System.out.println("List is empty for UserID: " + userId);
        } else {
            // Print the events to the console
            for (Event event : events) {
                System.out.println(event);
            }
        }
        return events;
    }
}


