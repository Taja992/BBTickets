package BE;

public class User {
    private int userId;
    private int user_type;
    private String password;
    private String username;
    private String roleName;
    private byte[] profilePicture;

    ///why?
    public User(Integer userID, Integer user_type, String password, String username) {
        this.userId = userID;
        this.user_type = user_type;
        this.password = password;
        this.username = username;

    }
    public User(Integer userId, Integer user_type, String password, String username, byte[] profilePicture) {
        this.user_type = user_type;
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.profilePicture = profilePicture;
    }

    public User(Integer userId, String username, byte[] profilePicture) {
        this.userId = userId;
        this.username = username;
        this.profilePicture = profilePicture;
    }



    public User (Integer user_type, String password, String username){
        this.user_type = user_type;
        this.password = password;
        this.username = username;
    }

    public User(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public int getUser_type() {return user_type;}

    public int setUser_type(int user_type) {return this.user_type = user_type;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getUserId() {
        return userId;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRoleName() {
        return roleName;
    }

}
