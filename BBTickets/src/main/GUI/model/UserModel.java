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

    public List<User> getAllUsers() throws BBExceptions {
        return bllUser.allUsers();
    }
}
