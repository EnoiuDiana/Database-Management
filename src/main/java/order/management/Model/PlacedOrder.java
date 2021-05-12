package order.management.Model;

import java.util.Date;

/**
 * the placed order model class
 */
public class PlacedOrder {
    private int id;
    private Date date;
    private int customerId;
    private int itemId;
    private int quantity;
    private int totalPrice;

    public PlacedOrder() {

    }

    public PlacedOrder(int id, Date date, int customerId, int itemId, int quantity, int totalPrice) {
        this.id = id;
        this.date = date;
        this.customerId = customerId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public PlacedOrder(Date date, int customerId, int itemId, int quantity, int totalPrice) {
        this.date = date;
        this.customerId = customerId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
