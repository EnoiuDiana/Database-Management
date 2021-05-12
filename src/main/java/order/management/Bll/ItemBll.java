package order.management.Bll;

import java.util.List;
import java.util.NoSuchElementException;

import order.management.Dao.ItemDao;
import order.management.Model.Item;

/**
 * This item business logic class will extract the data from the database, using
 * the methods from the abstract dao to call the specific queries
 */

public class ItemBll {

    private final ItemDao itemDao;

    public ItemBll() {
        itemDao = new ItemDao();
    }

    public Item findItemById(int id) {
        Item item = itemDao.findById(id);
        if (item == null) {
            throw new NoSuchElementException("The item with id =" + id + " was not found!");
        }
        return item;
    }

    public List<Item> findAllItems() {
        List<Item> items = itemDao.findAll();
        if (items == null) {
            throw new NoSuchElementException("No item was found!");
        }
        return items;
    }

    public Item insertItem(Item item) {
        return itemDao.insert(item);
    }

    public Item updateItem(Item item) {
        return itemDao.update(item);
    }
    public void deleteItem(int id) {
        itemDao.delete(id);
    }

}
