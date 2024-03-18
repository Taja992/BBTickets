package GUI.model;

import BE.User;
import BLL.BLLUser;
import Exceptions.BBExceptions;
import java.util.List;

public class UserModel {
    private BLLUser bllUser;

    public UserModel() {
        bllUser = new BLLUser();
    }

    public void newUser(User user) throws BBExceptions {
        bllUser.newUser(user);
    }

    public List<User> getAllUsers() throws BBExceptions {
        return bllUser.allUsers();
    }
}
