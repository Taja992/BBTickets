package Exceptions;

import java.sql.SQLException;

public class BBExceptions extends SQLException {

    public BBExceptions(String message){
        super();
    }
    public BBExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public BBExceptions(Throwable cause) {
        super(cause);
    }
}
