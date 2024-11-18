import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class ByteMeSystem {
    private Map<String, Item> menuItems;
    private Map<String, Customer> customers;
    private PriorityQueue<Order> pendingOrders;
    private List<Order> completedOrders;
    private Map<String, List<Review>> reviews;

    public ByteMeSystem() {
        this.menuItems = new TreeMap<>();
        this.customers = new HashMap<>();
        this.pendingOrders = new PriorityQueue<>();
        this.completedOrders = new ArrayList<>();
        this.reviews = new HashMap<>();
    }

    public void addMenuItem(String id, String name, double price, String category) {
        menuItems.put(id, new Item(id, name, price, category));
    }

    public void updateMenuItem(String id, double newPrice, boolean available) {
        Item item = menuItems.get(id);
        if (item != null) {
            item.setPrice(newPrice);
            item.setAvailable(available);
        }
    }

    public void removeMenuItem(String id){
        menuItems.remove(id);
        // Update pending orders containing this item
        for (Order order : pendingOrders) {
            if (order.getItems().keySet().stream().anyMatch(item -> item.getId().equals(id))) {
                order.setStatus(Order.OrderStatus.denied);
            }
        }
    }

    public List<Order> getPendingOrders(){
        return new ArrayList<>(pendingOrders);
    }

    public void updateOrderStatus(String orderId, Order.OrderStatus status){
        pendingOrders.stream().filter(order -> order.getOrderId().equals(orderId)).findFirst().ifPresent(
                order ->{order.setStatus(status);
                    if(status == Order.OrderStatus.delivered){
                        pendingOrders.remove(order);
                        completedOrders.add(order);
                    }
                });
    }

    public Map<String, Double> generateDailySalesReport(LocalDateTime date) {
        List<Order> dailyOrders = completedOrders.stream().filter(order -> order.getOrderTime().toLocalDate().equals(date.toLocalDate())).collect(Collectors.toList());

        Map<String, Double> report = new HashMap<>();
        report.put("totalSales", dailyOrders.stream().mapToDouble(Order::getTotalAmount).sum());
        report.put("totalOrders", (double) dailyOrders.size());

        // Most popular items could be added here

        return report;
    }


    public void registerCustomer(String id, String name){
        customers.put(id, new Customer(id, name));
    }

    public void upgradeToVip(String customerId){
        Customer customer = customers.get(customerId);
        if (customer != null){
            customer.setVip(true);
        }
    }

    public List<Item> getMenu(){
        return new ArrayList<>(menuItems.values());
    }

    public List<Item> searchMenu(String keyword){
        return menuItems.values().stream().filter(item -> item.getName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
    }

    public List<Item> filterByCategory(String category){
        return menuItems.values().stream().filter(item -> item.getCategory().equals(category)).collect(Collectors.toList());
    }

    public List<Item> sortByPrice(boolean ascending){
        return menuItems.values().stream().sorted((i1, i2) -> ascending ? Double.compare(i1.getPrice(), i2.getPrice()) : Double.compare(i2.getPrice(), i1.getPrice())).collect(Collectors.toList());
    }

    public void addToCart(String customerId, String itemId, int quantity){
        Customer customer = customers.get(customerId);
        Item item = menuItems.get(itemId);
        if(customer!=null && item !=null && item.isAvailable()){
            customer.getCurrentCart().addItem(item, quantity);
        }
    }

    public Order placeOrder(String customerId, String specialRequest) {
        Customer customer = customers.get(customerId);
        if (customer != null && !customer.getCurrentCart().getItems().isEmpty()) {
            String orderId = "O" + System.currentTimeMillis();
            Order order = new Order(orderId, customerId, customer.isVip());
            order.setSpecialRequest(specialRequest);

            customer.getCurrentCart().getItems().forEach(order::addItem);
            customer.getCurrentCart().clear();

            // Add to pending orders and customer history
            pendingOrders.offer(order);
            customer.addToOrderHistory(order);

            return order;
        }
        return null;
    }

    public void addReview(String customerId, String itemId, int rating, String comment) {
        Item item = menuItems.get(itemId);
        if (item != null) {
            Review review = new Review(customerId, itemId, rating, comment);
            item.addReview(review);
            if (!reviews.containsKey(itemId)) {
                reviews.put(itemId, new ArrayList<>());
            }
            reviews.get(itemId).add(review);
        }
    }

    public List<Order> getCustomerActiveOrders(String customerId) {
        return pendingOrders.stream().filter(order -> order.getCustomerId().equals(customerId)).collect(Collectors.toList());
    }

    public void processRefund(String orderId) {
        pendingOrders.stream().filter(order -> order.getOrderId().equals(orderId)).findFirst().ifPresent(order -> {
                    order.setStatus(Order.OrderStatus.cancelled);
                });
    }

    public Item getMenuItem(String itemId) {
        return menuItems.get(itemId);
    }

    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }
    public boolean cancelOrder(String orderId) {
        Order order = null;
        for (Order o : pendingOrders) {
            if (o.getOrderId().equals(orderId)) {
                order = o;
                break;
            }
        }

        if (order == null || order.getStatus()==Order.OrderStatus.outForDelivery || order.getStatus() == Order.OrderStatus.delivered || order.getStatus() == Order.OrderStatus.cancelled || order.getStatus()==Order.OrderStatus.denied) {
            return false;
        }

        order.setStatus(Order.OrderStatus.cancelled);
        pendingOrders.remove(order);
        return true;
    }

    public Map<Item, Integer> getMostPopularItems(LocalDateTime date) {
        Map<Item, Integer> popularItems = new HashMap<>();

        for (Order order : getOrdersByDate(date)) {
            for (Map.Entry<Item, Integer> entry : order.getItems().entrySet()) {
                Item item = entry.getKey();
                int quantity = entry.getValue();
                popularItems.merge(item, quantity, Integer::sum);
            }
        }

        return popularItems;
    }

    private List<Order> getOrdersByDate(LocalDateTime date) {
        return completedOrders.stream()
                .filter(order -> order.getOrderTime().toLocalDate().equals(date.toLocalDate()))
                .collect(Collectors.toList());
    }
    public List<Review> getReviewsForItem(String itemId) {
        if (reviews.containsKey(itemId)) {
            return reviews.get(itemId);
        } else {
            return new ArrayList<>();
        }
    }

}

