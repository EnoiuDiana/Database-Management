package order.management.Presentation.Controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import order.management.Bll.CustomerBll;
import order.management.Bll.ItemBll;
import order.management.Bll.PlacedOrderBll;
import order.management.Model.Customer;
import order.management.Model.Item;
import order.management.Model.PlacedOrder;
import order.management.Presentation.Controller.Util.DisplayPopup;
import order.management.Presentation.Controller.Util.NewSceneLoader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The controller of the placeOrder.fxml
 */
public class PlaceOrderFXMLController implements Initializable {

    //These items are for the choice box
    @FXML private ChoiceBox<Customer> customerChoiceBox;
    @FXML private ChoiceBox<Item> itemChoiceBox;

    //These items are for the text field for quantity
    @FXML private TextField quantityTextField;

    /**
     * Method to go back to the home fxml
     * @param event
     * @throws IOException
     */
    public void backButtonPushed(ActionEvent event) throws IOException {
        NewSceneLoader newSceneLoader = new NewSceneLoader();
        newSceneLoader.loadNewScene(event, "home");
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
     * method to get all items from the database
     * @return item observable list
     */
    public ObservableList<Item> getItemsFromDatabase() {
        ObservableList<Item> items = FXCollections.observableArrayList();
        List<Item> itemsList;
        ItemBll itemBll = new ItemBll();
        itemsList = itemBll.findAllItems();
        items.addAll(itemsList);
        return items;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //This items are for configuring the customer choice box
        ObservableList<Customer> customers = getCustomersFromDatabase();
        customerChoiceBox.setItems(customers);
        //This items are for configuring the items choice box
        ObservableList<Item> items = getItemsFromDatabase();
        itemChoiceBox.setItems(items);


    }

    /**
     * This method is called when place order button is pushed
     */
    public void placeOrderButtonPushed() {
        Customer selectedCustomer = customerChoiceBox.getValue();
        Item selectedItem = itemChoiceBox.getValue();
        int insertedQuantity = Integer.parseInt(quantityTextField.getText());

        if(insertedQuantity > selectedItem.getQuantity()) {
            DisplayPopup.displayPopup("Error", "Incorrect insertedQuantity. Order is not placed.");
        } else {
            PlacedOrder placedOrder = placeOrder(selectedCustomer, selectedItem, insertedQuantity);
            try {
                generateBillPdf(placedOrder);
            } catch (Exception e) {
                DisplayPopup.displayPopup("Error", "Cannot generate bill");
            }
        }
    }

    /**
     * This method will place the order in the database
     * @param selectedCustomer
     * @param selectedItem
     * @param insertedQuantity
     * @return placed order
     */
    public PlacedOrder placeOrder(Customer selectedCustomer, Item selectedItem, int insertedQuantity) {
        PlacedOrder placedOrder = new PlacedOrder();
        Date date = new Date(System.currentTimeMillis());
        placedOrder.setDate(date);
        placedOrder.setCustomerId(selectedCustomer.getId());
        placedOrder.setItemId(selectedItem.getId());
        placedOrder.setQuantity(insertedQuantity);
        placedOrder.setTotalPrice(selectedItem.getPrice() * insertedQuantity);
        PlacedOrderBll placedOrderBll = new PlacedOrderBll();
        placedOrderBll.insertPlacedOrder(placedOrder);

        //update the item
        ItemBll itemBll = new ItemBll();
        selectedItem.setQuantity(selectedItem.getQuantity()-insertedQuantity);
        itemBll.updateItem(selectedItem);
        return placedOrder;
    }

    /**
     * This method will generate a bill in a pdf format
     * @param placedOrder the order that was placed
     * @throws FileNotFoundException
     * @throws DocumentException
     */

    public void generateBillPdf(PlacedOrder placedOrder) throws FileNotFoundException, DocumentException {
        CustomerBll customerBll = new CustomerBll();
        ItemBll itemBll = new ItemBll();
        Customer selectedCustomer = customerBll.findCustomerById(placedOrder.getCustomerId());
        Item selectedItem = itemBll.findItemById(placedOrder.getItemId());
        int insertedQuantity = placedOrder.getQuantity();
        int totalPrice = placedOrder.getTotalPrice();

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("bill.pdf"));

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Paragraph bill = new Paragraph("Bill date: " + placedOrder.getDate() + "\n\n" , font);
        Paragraph customer = new Paragraph("Customer data\n" + "Name: " + selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName()
                + "\nPhone number: " + selectedCustomer.getPhone()
                + "\nEmail: " + selectedCustomer.getEmail()
                + "\nAddress: " + selectedCustomer.getAddress(), font);
        Paragraph item = new Paragraph("\nItem ordered\n" + "Item name: " + selectedItem.getName() + "\nPrice: " + selectedItem.getPrice()
                + "\nQuantity: " + insertedQuantity, font);
        Paragraph total = new Paragraph("\nTotal: " + totalPrice, font);
        document.add(bill);
        document.add(customer);
        document.add(item);
        document.add(total);
        document.close();
    }
}
