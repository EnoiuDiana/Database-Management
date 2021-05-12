package order.management.Bll;
import java.util.List;
import java.util.NoSuchElementException;

import order.management.Dao.PlacedOrderDao;
import order.management.Model.PlacedOrder;

/**
 * This placedOrder business logic class will extract the data from the database, using
 * the methods from the abstract dao to call the specific queries
 */

public class PlacedOrderBll {

    private final PlacedOrderDao placedOrderDao;

    public PlacedOrderBll() {
        placedOrderDao = new PlacedOrderDao();
    }

    public PlacedOrder findPlacedOrderById(int id) {
        PlacedOrder placedOrder = placedOrderDao.findById(id);
        if (placedOrder == null) {
            throw new NoSuchElementException("The placed order with id =" + id + " was not found!");
        }
        return placedOrder;
    }

    public List<PlacedOrder> findAllPlacedOrders() {
        List<PlacedOrder> placedOrders = placedOrderDao.findAll();
        if (placedOrders == null) {
            throw new NoSuchElementException("No placedOrder was found!");
        }
        return placedOrders;
    }

    public PlacedOrder insertPlacedOrder(PlacedOrder placedOrder) {
        return placedOrderDao.insert(placedOrder);
    }

    public PlacedOrder updatePlacedOrder(PlacedOrder placedOrder) {
        return placedOrderDao.update(placedOrder);
    }
    public void deletePlacedOrder(int id) {
        placedOrderDao.delete(id);
    }

}
