import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final String id;
    private String name;
    private boolean isVip;
    private List<Order> orderHistory;
    private Cart currentCart;

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
        this.isVip = false;
        this.orderHistory = new ArrayList<>();
        this.currentCart = new Cart();
    }

    public String getId() { return id; }
    public boolean isVip() { return isVip; }
    public void setVip(boolean vip) { this.isVip = vip; }
    public List<Order> getOrderHistory() { return orderHistory; }
    public Cart getCurrentCart() { return currentCart; }

    public void addToOrderHistory(Order order) {
        orderHistory.add(order);
    }
}