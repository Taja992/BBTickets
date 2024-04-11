package DAL;

import BE.Customer;
import Exceptions.BBExceptions;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    private ConnectionManager connectionManager;

    public CustomerDAO() {
        connectionManager = new ConnectionManager();
    }

    public void newCustomer(Customer customer) throws BBExceptions {
        String sql = "INSERT INTO [Customer] (name, email) VALUES (?, ?)";

        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customer.getCustomerName());
            statement.setString(2, customer.getCustomerEmail());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new BBExceptions("Failed to retrieve user", e);
        }
    }

    public List<Customer> getAllCustomers(){
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer";


        try{
            Connection con = connectionManager.getConnection();
            PreparedStatement pstmnt = con.prepareStatement(sql);
            ResultSet rs = pstmnt.executeQuery();

            while (rs.next()){
                int id = rs.getInt("cust_id");
                String name = rs.getString("name");
                String email = rs.getString("email");

                Customer cust = new Customer(id, name, email);
                customers.add(cust);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customers;

    }

    public int getLastCustomerID(){
        int lastId = -1;
        String sql = "SELECT * FROM Customer WHERE cust_id = (SELECT IDENT_CURRENT('Customer'))";

        try {
            Connection con = connectionManager.getConnection();
            PreparedStatement pstmnt = con.prepareStatement(sql);
            ResultSet rs = pstmnt.executeQuery();
            while(rs.next()){
                lastId = rs.getInt("cust_id");
            }
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lastId;

    }


}