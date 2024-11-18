import java.util.Map;
import java.util.Scanner;
import java.util.List;

public class CustomerInterface {
    public static void showMenu(ByteMeSystem system, Scanner scanner, Customer customer) {
        while (true) {
            System.out.println("\nCustomer Menu");
            System.out.println("1. Browse Menu");
            System.out.println("2. Cart Operations");
            System.out.println("3. Order Tracking");
            System.out.println("4. View Order History");
            System.out.println("5. Upgrade to VIP");
            System.out.println("6. Write Review");
            System.out.println("7. View Reviews");
            System.out.println("8. Cancel Order");
            System.out.println("9. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice==1){
                browseMenu(system, scanner);
            }
            else if (choice==2){
                cartOperations(system, scanner,customer);
            }
            else if (choice == 3) {
                trackOrders(system, customer);
            }
            else if(choice==4){
                viewOrderHistory(system,customer);
            }
            else if (choice==5){
                upgradeToVip(system, customer);
            }
            else if (choice==6){
                writeReview(system, scanner, customer);
            }
            else if(choice==9){
                return;
            }
            else if(choice==8){
                cancelOrder(system,scanner,customer);
            }
            else if(choice==7){
                viewReviews(system,scanner);
            }
            else{
                System.out.println("invalid choice");
            }
        }
    }

    private static void browseMenu(ByteMeSystem system, Scanner scanner){
        while (true){
            System.out.println("\nBrowse Menu");
            System.out.println("1. View all items");
            System.out.println("2. Search items");
            System.out.println("3. Filter by category");
            System.out.println("4. Sort by price");
            System.out.println("5. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice==1){
                displayItems(system.getMenu());
            }
            else if (choice==2){
                searchItems(system, scanner);
            }
            else if(choice==3){
                filterByCategory(system, scanner);
            }
            else if(choice==4){
                sortByPrice(system, scanner);
            }
            else if(choice==5){
                return;
            }
            else{
                System.out.println("invalid choice");
            }
        }
    }

    private static void displayItems(List<Item> items){
        System.out.println("\nAvailable Items:");
        for (Item item : items){
            System.out.println(item);
        }
    }

    private static void searchItems(ByteMeSystem system, Scanner scanner){
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine();
        List<Item> results = system.searchMenu(keyword);
        displayItems(results);
    }

    private static void filterByCategory(ByteMeSystem system, Scanner scanner){
        String category=null;

            System.out.println("categories available are:");
            System.out.println("1. main course");
            System.out.println("2. fast food");
            System.out.println("3. snacks");
            System.out.println("4. beverages");
            System.out.print("choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1){
                category = "main course";
            }
            else if (choice == 2){
                category = "fast food";
            }
            else if (choice == 3){
                category = "snacks";
            }
            else if (choice == 4){
                category = "beverages";
            }
            else{
                System.out.println("invalid choice");
            }


        List<Item> results = system.filterByCategory(category);
        displayItems(results);
    }

    private static void sortByPrice(ByteMeSystem system, Scanner scanner){
        System.out.print("Sort ascending? (true/false): ");
        boolean ascending = scanner.nextBoolean();
        List<Item> results = system.sortByPrice(ascending);
        displayItems(results);
    }

    private static void cartOperations(ByteMeSystem system, Scanner scanner, Customer customer){
        while (true){
            System.out.println("\nCart Operations");
            System.out.println("1. Add item to cart");
            System.out.println("2. Modify Quantity");
            System.out.println("3. Remove item from cart");
            System.out.println("4. View cart");
            System.out.println("5. Checkout");
            System.out.println("6. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice==1){
                addToCart(system, scanner, customer);
            }
            else if(choice==3){
                removeFromCart(system, scanner, customer);
            }
            else if(choice==4){
                viewCart(customer);
            }
            else if(choice==5){
                checkout(system, scanner, customer);
            }
            else if(choice==6){
                return;
            }
            else if(choice==2){
                modifyQuantity(system,scanner,customer);
            }
            else{
                System.out.println("invalid choice");
            }
        }
    }

    private static void addToCart(ByteMeSystem system, Scanner scanner, Customer customer){
        displayItems(system.getMenu());
        System.out.print("enter item id: ");
        String itemId = scanner.nextLine();
        System.out.print("enter quantity: ");
        int quantity = scanner.nextInt();
        system.addToCart(customer.getId(), itemId, quantity);
        System.out.println("Item added to cart");
    }

    private static void modifyQuantity(ByteMeSystem system,Scanner scanner, Customer customer){
        Cart cart = customer.getCurrentCart();
        Map<Item, Integer> cartItems = cart.getItems();

        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.print("Enter the item ID to modify: ");
        String itemId = scanner.nextLine();

        Item item = system.getMenuItem(itemId);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        if (!cartItems.containsKey(item)) {
            System.out.println("Item not found in cart.");
            return;
        }

        int currentQuantity = cartItems.get(item);
        System.out.printf("Current quantity for %s: %d%n", item.getName(), currentQuantity);

        System.out.print("Enter the new quantity: ");
        int newQuantity = scanner.nextInt();
        scanner.nextLine();

        cart.updateQuantity(item, newQuantity);
        System.out.println("Quantity updated successfully.");

    }

    private static void removeFromCart(ByteMeSystem system, Scanner scanner, Customer customer){
        viewCart(customer);
        System.out.print("enter item id to remove: ");
        String itemId = scanner.nextLine();
        customer.getCurrentCart().removeItem(system.getMenuItem(itemId));
        System.out.println("Item removed from cart");
    }

    private static void viewCart(Customer customer){
        Cart cart = customer.getCurrentCart();
        System.out.println("\ncurrent cart:");
        if (cart.getItems().isEmpty()){
            System.out.println("cart is empty!");
            return;
        }
        cart.getItems().forEach((item, quantity) ->
                System.out.printf("%s x%d = ₹%.2f%n", item.getName(), quantity, item.getPrice() * quantity));
        System.out.printf("Total: ₹%.2f%n", cart.getTotal());
    }

    private static void checkout(ByteMeSystem system, Scanner scanner, Customer customer){
        viewCart(customer);
        if (customer.getCurrentCart().getItems().isEmpty()){
            return;
        }

        System.out.print("any special requests? (press enter to skip) ");
        String specialRequest = scanner.nextLine();

        Order order = system.placeOrder(customer.getId(), specialRequest);
        if (order != null){
            System.out.println("order placed successfully.");
            System.out.println("order id: " + order.getOrderId());
            System.out.println("total amount: ₹" + order.getTotalAmount());
        }
        else{
            System.out.println("could not place order!");
        }
    }

    private static void trackOrders(ByteMeSystem system, Customer customer){
        List<Order> activeOrders = system.getCustomerActiveOrders(customer.getId());
        System.out.println("\nActive orders:");
        if (activeOrders.isEmpty()){
            System.out.println("No active orders.");
            return;
        }
        for (Order order : activeOrders){
            System.out.println("\norder id-> " + order.getOrderId());
            System.out.println("status-> " + order.getStatus());
            System.out.println("items->");
            order.getItems().forEach((item, quantity) ->
                    System.out.println("- " + item.getName() + " x" + quantity));
            System.out.println("Total Amount-> ₹" + order.getTotalAmount());
        }
    }

    private static void viewOrderHistory(ByteMeSystem system,Customer customer){
        System.out.println("\nOrder History->");
        List<Order> history = customer.getOrderHistory();
        if (history.isEmpty()){
            System.out.println("No orders in history.");
            return;
        }
        for (int i = 0; i < history.size(); i++) {
            Order order = history.get(i);
            System.out.println("\n[" + (i + 1) + "] order id-> " + order.getOrderId());
            System.out.println("status-> " + order.getStatus());
            System.out.println("order date-> " + order.getOrderTime());
            System.out.println("total amount-> ₹" + order.getTotalAmount());
        }
        System.out.print("\nEnter the order number to re-order (or 0 to go back): ");
        Scanner scanner = new Scanner(System.in);
        int orderNumber = scanner.nextInt();
        scanner.nextLine();

        if (orderNumber == 0) {
            return;
        }

        if (orderNumber < 1 || orderNumber > history.size()) {
            System.out.println("Invalid order number.");
            return;
        }

        Order selectedOrder = history.get(orderNumber - 1);
        Map<Item, Integer> orderItems = selectedOrder.getItems();

        for (Map.Entry<Item, Integer> entry : orderItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            system.addToCart(customer.getId(), item.getId(), quantity);
            System.out.println("Added " + quantity + " x " + item.getName() + " to the cart.");
        }

        System.out.println("\nItems added to the cart. Would you like to proceed to checkout?");
        System.out.print("(y/n): ");
        String proceedToCheckout = scanner.nextLine();

        if (proceedToCheckout.equals("y")) {
            checkout(system, scanner, customer);
        }
    }

    private static void upgradeToVip(ByteMeSystem system, Customer customer){
        if (customer.isVip()){
            System.out.println("you are already a VIP customer!");
            return;
        }
        System.out.println("\nVIP Membership Cost: ₹500");
        System.out.print("Do you want to upgrade to VIP? (y/n): ");
        Scanner scanner = new Scanner(System.in);
        String upgrade = scanner.nextLine();
        if (upgrade.equals("y")){
            system.upgradeToVip(customer.getId());
            System.out.println("congratulations! you are now a VIP member.");
        }
    }

    private static void writeReview(ByteMeSystem system, Scanner scanner, Customer customer){
        viewOrderHistory(system,customer);
        System.out.print("enter item id to review: ");
        String itemId = scanner.nextLine();

        System.out.print("enter rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine();

        System.out.print("enter review comment: ");
        String comment = scanner.nextLine();

        system.addReview(customer.getId(), itemId, rating, comment);
        System.out.println("review submitted.");
    }

    private static void cancelOrder(ByteMeSystem system, Scanner scanner, Customer customer) {
        List<Order> activeOrders = system.getCustomerActiveOrders(customer.getId());
        if (activeOrders.isEmpty()) {
            System.out.println("You have no active orders to cancel.");
            return;
        }

        System.out.println("\nActive Orders:");
        for (Order order : activeOrders) {
            System.out.println("Order ID: " + order.getOrderId() + ", Status: " + order.getStatus());
        }

        System.out.print("Enter the order ID you want to cancel: ");
        String orderId = scanner.nextLine();

        if (system.cancelOrder(orderId)) {
            System.out.println("order cancelled successfully.");
        } else {
            System.out.println("could not cancel the order.");
        }
    }

    private static void viewReviews(ByteMeSystem system, Scanner scanner) {
        System.out.print("Enter the item ID to view reviews: ");
        String itemId = scanner.nextLine();

        List<Review> reviews = system.getReviewsForItem(itemId);

        if (reviews.isEmpty()) {
            System.out.println("No reviews found for this item.");
            return;
        }

        System.out.println("\nReviews for item ID " + itemId + ":");
        for (Review review : reviews) {
            System.out.println("Rating: " + review.getRating());
            System.out.println("Comment: " + review.getComment());
            System.out.println("------------------------");
        }
    }
}

