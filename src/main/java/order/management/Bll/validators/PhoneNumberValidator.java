package order.management.Bll.validators;

import order.management.Model.Customer;

/**
 * The phone validator class
 */
public class PhoneNumberValidator implements Validator<Customer>{

    /**
     * this method will check if a phone number is correct or not
     * @param t receives an customer as parameter
     */
    @Override
    public void validate(Customer t) {
        try{
            Integer.parseInt(t.getPhone());
        } catch (Exception e) {
            throw new IllegalArgumentException("Phone number is not numeric!");
        }
        if(t.getPhone().length() != 10) {
            throw new IllegalArgumentException("Phone number is not valid!");
        }
    }
}
