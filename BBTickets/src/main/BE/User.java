package BE;

public class User {
    private int user_type;
    private String password;
    private String username;

    public User(Integer user_type, String password, String username) {
        this.user_type = user_type;
        this.password = password;
        this.username = username;
    }

    public int getUser_type() {return user_type;}

    public int setUser_type(int user_type) {return this.user_type = user_type;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}
}