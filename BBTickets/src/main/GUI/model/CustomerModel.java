package GUI.model;

import BE.Customer;
import BLL.CustomerBLL;
import Exceptions.BBExceptions;

import java.util.List;

public class CustomerModel {
    CustomerBLL custBLL = new CustomerBLL();

    public void newCustomer(Customer customer) throws BBExceptions {
        custBLL.newCustomer(customer);
    }

    public List<Customer> getAllCustomers(){
        return custBLL.getAllCustomers();
    }
    public int getLastCustomerID(){
        return custBLL.getLastCustomerID();
    }
}
