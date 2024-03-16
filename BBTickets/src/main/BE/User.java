package BE;

public class User {
    private String admin;
    private String event_controller;
    private String password;
    private String username;

    public User(String admin, String event_controller, String password, String username) {
        this.admin = admin;
        this.event_controller = event_controller;
        this.password = password;
        this.username = username;
    }

    public String getAdmin() {return admin;}

    public void setAdmin(String admin) {this.admin = admin;}

    public String getEvent_controller() {return event_controller;}

    public void setEvent_controller(String event_controller) {this.event_controller = event_controller;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}
}
