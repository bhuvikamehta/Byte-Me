import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Order implements Comparable<Order> {
    private String orderId;
    private String customerId;
    private Map<Item, Integer> items;
    private double totalAmount;
    private OrderStatus status;
    private LocalDateTime orderTime;
    private String specialRequest;
    private boolean isVipOrder;

    public enum OrderStatus {
        received, preparing, outForDelivery, delivered, cancelled, denied
    }

    public Order(String orderId, String customerId, boolean isVipOrder) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.isVipOrder = isVipOrder;
        this.items = new HashMap<>();
        this.status = OrderStatus.received;
        this.orderTime = LocalDateTime.now();
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public boolean isVipOrder() {
        return isVipOrder;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public void addItem(Item item, int quantity) {
        items.put(item, items.getOrDefault(item, 0) + quantity);
        calculateTotal();
    }

    public void removeItem(Item item) {
        items.remove(item);
        calculateTotal();
    }

    private void calculateTotal() {
        totalAmount = items.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    @Override
    public int compareTo(Order other) {
        // VIP orders take priority
        if (this.isVipOrder && !other.isVipOrder) return -1;
        if (!this.isVipOrder && other.isVipOrder) return 1;
        // If both are VIP or both are regular, sort by order time
        return this.orderTime.compareTo(other.orderTime);
    }
}
