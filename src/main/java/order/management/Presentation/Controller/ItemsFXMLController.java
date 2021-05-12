package order.management.Presentation.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import order.management.Bll.ItemBll;
import order.management.Model.Item;
import order.management.Presentation.Controller.Util.NewSceneLoader;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The controller of the items.fxml
 */
public class ItemsFXMLController implements Initializable {
    //configure table view
    @FXML
    private TableView<Item> tableViewItem;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, Integer> quantityColumn;
    @FXML private TableColumn<Item, Integer> priceColumn;

    //These instance variables are used to create new item object
    @FXML private TextField nameTextField;
    @FXML private TextField quantityTextField;
    @FXML private TextField priceTextField;

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
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //load items from database
        tableViewItem.setItems(getItemsFromDatabase());

        //Update the table to allow for the fields to be editable
        tableViewItem.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        //this will allow to select multiple rows
        tableViewItem.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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

    /**
     * This method will allow users to edit the name from the table, by double clicking on a cell
     * @param editedCell
     */
    public void changeNameCellEvent(TableColumn.CellEditEvent editedCell) {

        Item itemSelected = tableViewItem.getSelectionModel().getSelectedItem();
        itemSelected.setName(editedCell.getNewValue().toString());

        ItemBll itemBll = new ItemBll();
        itemBll.updateItem(itemSelected);

        tableViewItem.setItems(getItemsFromDatabase());
    }

    /**
     * This method will allow users to edit the quantity from the table, by double clicking on a cell
     * @param editedCell
     */
    public void changeQuantityCellEvent(TableColumn.CellEditEvent editedCell) {
        Item itemSelected = tableViewItem.getSelectionModel().getSelectedItem();
        itemSelected.setQuantity(Integer.parseInt(editedCell.getNewValue().toString()));

        ItemBll itemBll = new ItemBll();
        itemBll.updateItem(itemSelected);

        tableViewItem.setItems(getItemsFromDatabase());
    }

    /**
     * This method will allow users to edit the price from the table, by double clicking on a cell
     * @param editedCell
     */
    public void changePriceCellEvent(TableColumn.CellEditEvent editedCell) {

        Item itemSelected = tableViewItem.getSelectionModel().getSelectedItem();
        itemSelected.setPrice(Integer.parseInt(editedCell.getNewValue().toString()));

        ItemBll itemBll = new ItemBll();
        itemBll.updateItem(itemSelected);

        tableViewItem.setItems(getItemsFromDatabase());
    }

    /**
     * This method will insert a new item in the database,
     * when the add new item button is pressed
     */
    public void newItemButtonPushed() {
        Item item = new Item(nameTextField.getText(), Integer.parseInt(priceTextField.getText()),
                Integer.parseInt(quantityTextField.getText()));
        ItemBll itemBll = new ItemBll();
        itemBll.insertItem(item);
        tableViewItem.getItems().add(item);
        nameTextField.clear();
        quantityTextField.clear();
        priceTextField.clear();
    }

    /**
     * This method will delete a item or items in the database,
     * when the delete item button is pressed
     */
    public void deleteItemsButtonPushed() {
        ObservableList<Item> selectedRows, allItems;
        ItemBll itemBll = new ItemBll();
        allItems = tableViewItem.getItems();
        selectedRows = tableViewItem.getSelectionModel().getSelectedItems();
        //loop over the selected rows and delete those customers
        for(Item item : selectedRows) {
            itemBll.deleteItem(item.getId());
        }
        allItems.removeAll(selectedRows);
        tableViewItem.setItems(getItemsFromDatabase());
    }
}
