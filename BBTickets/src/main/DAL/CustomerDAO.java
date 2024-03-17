package DAL;

import BE.Customer;
import Exceptions.BBExceptions;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerDAO {

    private ConnectionManager connectionManager;

    public CustomerDAO() {
        connectionManager = new ConnectionManager();
    }

    public void newCustomer(Customer customer) {
        String sql = "INSERT INTO [Customer] (customer_name, customer_email) VALUES (?, ?, ?)";

        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customer.getCustomerName());
            statement.setString(2, customer.getCustomerEmail());
            statement.executeUpdate();

        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}