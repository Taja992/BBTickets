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
        List<User> allUsers = userDAO.allUsers();
        for (User user : allUsers) {
            String roleName = switch (user.getUser_type()) {
                case 0 -> "Admin";
                case 1 -> "Event Coordinator";
                default -> "Unknown";
            };
            user.setRoleName(roleName);
        }
        return allUsers;
    }
}


