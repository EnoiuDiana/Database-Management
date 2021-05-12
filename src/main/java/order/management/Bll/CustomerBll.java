package order.management.Bll;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import order.management.Bll.validators.EmailValidator;
import order.management.Bll.validators.PhoneNumberValidator;
import order.management.Bll.validators.Validator;
import order.management.Dao.CustomerDao;
import order.management.Model.Customer;

/**
 * This customer business logic class will extract the data from the database, using
 * the methods from the abstract dao to call the specific queries
 */

public class CustomerBll {

    private final List<Validator<Customer>> validators;
    private final CustomerDao customerDao;

    public CustomerBll() {
        validators = new ArrayList<>();
        validators.add(new EmailValidator());
        validators.add(new PhoneNumberValidator());

        customerDao = new CustomerDao();
    }

    public Customer findCustomerById(int id) {
        Customer customer = customerDao.findById(id);
        if (customer == null) {
            throw new NoSuchElementException("The customer with id =" + id + " was not found!");
        }
        return customer;
    }

    public List<Customer> findAllCustomers() {
        List<Customer> customers = customerDao.findAll();
        if (customers == null) {
            throw new NoSuchElementException("No customer was found!");
        }
        return customers;
    }

    public Customer insertCustomer(Customer customer) {
        return customerDao.insert(customer);
    }

    public Customer updateCustomer(Customer customer) {
        return customerDao.update(customer);
    }

    public void deleteCustomer(int id) {
        customerDao.delete(id);
    }

    /**
     * this method will validate the email address and the phone number
     * @param customer
     */
    public void validate(Customer customer) {
        for(Validator<Customer> validator : validators) {
            validator.validate(customer);
        }
    }
}