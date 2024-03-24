
package BLL;

import BE.Customer;
import DAL.CustomerDAO;
import Exceptions.BBExceptions;

import java.util.List;

public class CustomerBLL {
    CustomerDAO DAO = new CustomerDAO();

    public void newCustomer(Customer customer) throws BBExceptions {
        DAO.newCustomer(customer);
    }

    public List<Customer> getAllCustomers() {
        return DAO.getAllCustomers();
    }

}
