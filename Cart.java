import java.util.HashMap;
import java.util.Map;

class Cart {
    private Map<Item, Integer> items;

    public Cart() {
        this.items = new HashMap<>();
    }

    public void addItem(Item item, int quantity) {
        items.put(item, items.getOrDefault(item, 0) + quantity);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void updateQuantity(Item item, int quantity) {
        if (quantity <= 0) {
            items.remove(item);
        } else {
            items.put(item, quantity);
        }
    }

    public double getTotal() {
        return items.entrySet().stream().mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue()).sum();
    }

    public Map<Item, Integer> getItems() {
        return new HashMap<>(items);
    }

    public void clear() {
        items.clear();
    }
}