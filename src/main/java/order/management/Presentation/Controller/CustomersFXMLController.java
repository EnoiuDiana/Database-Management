package order.management.Presentation.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import order.management.Bll.CustomerBll;
import order.management.Model.Customer;
import order.management.Presentation.Controller.Util.DisplayPopup;
import order.management.Presentation.Controller.Util.NewSceneLoader;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The controller of the customers.fxml
 */
public class CustomersFXMLController implements Initializable {

    //configure table view
    @FXML private TableView<Customer> tableViewCustomer;
    @FXML private TableColumn<Customer, String> firstNameColumn;
    @FXML private TableColumn<Customer, String> lastNameColumn;
    @FXML private TableColumn<Customer, String> emailColumn;
    @FXML private TableColumn<Customer, String> phoneColumn;
    @FXML private TableColumn<Customer, String> addressColumn;

    //These instance variables are used to create new customer object
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField phoneTextField;
    @FXML private TextField addressTextField;

    /**
     * Method to go back to the home fxml
     * @param event
     * @throws IOException
     */
    public void backButtonPushed(ActionEvent event) throws IOException {
        NewSceneLoader newSceneLoader = new NewSceneLoader();
        newSceneLoader.loadNewScene(event, "home");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set up the columns in the table
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        //load customers from database
        tableViewCustomer.setItems(getCustomersFromDatabase());

        //Update the table to allow for the fields to be editable
        tableViewCustomer.setEditable(true);
        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        addressColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        //this will allow to select multiple rows
        tableViewCustomer.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * method to get all customers from the database
     * @return customer observable list
     */

    public ObservableList<Customer> getCustomersFromDatabase() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        List<Customer> customersList;
        CustomerBll customerBll = new CustomerBll();
        customersList = customerBll.findAllCustomers();
        customers.addAll(customersList);

        return customers;
    }

    /**
     * This method will allow users to edit the firstname from the table, by double clicking on a cell
     * @param editedCell
     */
    public void changeFirstNameCellEvent(CellEditEvent editedCell) {

        Customer customerSelected = tableViewCustomer.getSelectionModel().getSelectedItem();
        customerSelected.setFirstName(editedCell.getNewValue().toString());

        CustomerBll customerBll = new CustomerBll();
        customerBll.updateCustomer(customerSelected);

        tableViewCustomer.setItems(getCustomersFromDatabase());
    }

    /**
     * This method will allow users to edit the lastname from the table, by double clicking on a cell
     * @param editedCell
     */
    public void changeLastNameCellEvent(CellEditEvent editedCell) {
        Customer customerSelected = tableViewCustomer.getSelectionModel().getSelectedItem();
        customerSelected.setLastName(editedCell.getNewValue().toString());

        CustomerBll customerBll = new CustomerBll();
        customerBll.updateCustomer(customerSelected);

        tableViewCustomer.setItems(getCustomersFromDatabase());
    }

    /**
     * This method will allow users to edit the email from the table, by double clicking on a cell
     * If email is not correct it will display a popup window.
     * @param editedCell
     */
    public void changeEmailCellEvent(CellEditEvent editedCell) {

        Customer customerSelected = tableViewCustomer.getSelectionModel().getSelectedItem();
        customerSelected.setEmail(editedCell.getNewValue().toString());
        try {
            CustomerBll customerBll = new CustomerBll();
            customerBll.validate(customerSelected);
            customerBll.updateCustomer(customerSelected);
        }catch(Exception e) {
            DisplayPopup.displayPopup("Error", "Could not perform the edit. Incorrect data.");
        } finally {
            tableViewCustomer.setItems(getCustomersFromDatabase());
        }
    }

    /**
     * This method will allow users to edit the phone from the table, by double clicking on a cell
     * If phone is not correct it will display a popup window.
     * @param editedCell
     */
    public void changePhoneCellEvent(CellEditEvent editedCell) {
        Customer customerSelected = tableViewCustomer.getSelectionModel().getSelectedItem();
        customerSelected.setPhone(editedCell.getNewValue().toString());
        try {
            CustomerBll customerBll = new CustomerBll();
            customerBll.validate(customerSelected);
            customerBll.updateCustomer(customerSelected);
        }catch(Exception e) {
            DisplayPopup.displayPopup("Error", "Could not perform the edit. Incorrect data.");
        } finally {
            tableViewCustomer.setItems(getCustomersFromDatabase());
        }
    }

    /**
     * This method will allow users to edit the address from the table, by double clicking on a cell
     * @param editedCell
     */
    public void changeAddressCellEvent(CellEditEvent editedCell) {
        Customer customerSelected = tableViewCustomer.getSelectionModel().getSelectedItem();
        customerSelected.setAddress(editedCell.getNewValue().toString());

        CustomerBll customerBll = new CustomerBll();
        customerBll.updateCustomer(customerSelected);

        tableViewCustomer.setItems(getCustomersFromDatabase());
    }

    /**
     * This method will insert a new customer in the database,
     * when the add new customer button is pressed
     */
    public void newCustomerButtonPushed() {
        Customer customer = new Customer(firstNameTextField.getText(),
                lastNameTextField.getText(), emailTextField.getText(),
                phoneTextField.getText(),addressTextField.getText());
        CustomerBll customerBll = new CustomerBll();
        try {
            customerBll.validate(customer);
            customerBll.insertCustomer(customer);
            //Get all items from table as a list, then add the new item to the list
            tableViewCustomer.getItems().add(customer);
        } catch (Exception e) {
            DisplayPopup.displayPopup("Error", "Could not perform the edit. Incorrect data.");
        } finally {
            firstNameTextField.clear();
            lastNameTextField.clear();
            emailTextField.clear();
            phoneTextField.clear();
            addressTextField.clear();
        }
    }

    /**
     * This method will delete a customer or customers in the database,
     * when the delete customer button is pressed
     */
    public void deleteCustomersButtonPushed() {

        ObservableList<Customer> selectedRows, allItems;
        CustomerBll customerBll = new CustomerBll();
        allItems = tableViewCustomer.getItems();
        selectedRows = tableViewCustomer.getSelectionModel().getSelectedItems();
        //loop over the selected rows and delete those customers
        for(Customer customer : selectedRows) {
            customerBll.deleteCustomer(customer.getId());
        }
        allItems.removeAll(selectedRows);
        tableViewCustomer.setItems(getCustomersFromDatabase());
    }

}
