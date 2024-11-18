import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;
import java.util.Map;

public class AdminInterface {
    public static void showMenu(ByteMeSystem system, Scanner scanner) {
        boolean isLoggedIn = true;
        while (isLoggedIn) {
            System.out.println("\nAdmin Menu");
            System.out.println("1. Manage Menu Items");
            System.out.println("2. Manage Orders");
            System.out.println("3. Generate Report");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                menuManagement(system, scanner);
            } else if (choice == 2) {
                orderManagement(system, scanner);
            } else if (choice == 3) {
                generateReports(system);
            } else if (choice == 4) {
                System.out.println("logging out...");
                isLoggedIn = false;
            } else {
                System.out.println("Invalid choice");
            }
        }
    }

    private static void menuManagement(ByteMeSystem system, Scanner scanner) {
        while (true) {
            System.out.println("\nMenu Management");
            System.out.println("1. Add new item");
            System.out.println("2. Update existing item");
            System.out.println("3. Remove item");
            System.out.println("4. View all items");
            System.out.println("5. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                addNewItem(system, scanner);
            } else if (choice == 2) {
                updateItem(system, scanner);
            } else if (choice == 3) {
                removeItem(system, scanner);
            } else if (choice == 4) {
                viewAllItems(system);
            } else if (choice == 5) {
                return;
            } else {
                System.out.println("Invalid choice");
            }
        }
    }

    private static void addNewItem(ByteMeSystem system, Scanner scanner) {
        System.out.print("enter item id: ");
        String id = scanner.nextLine();

        System.out.print("enter item name: ");
        String name = scanner.nextLine();

        System.out.print("enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("enter category: ");
        String category = scanner.nextLine();

        system.addMenuItem(id, name, price, category);
        System.out.println("Item added successfully.");
    }

    private static void updateItem(ByteMeSystem system, Scanner scanner) {
        viewAllItems(system);
        System.out.print("enter item id to update: ");
        String id = scanner.nextLine();

        System.out.print("enter new price (or -1 to skip): ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("is item available? (true/false)-> ");
        boolean available = scanner.nextBoolean();

        if (price != -1) {
            system.updateMenuItem(id, price, available);
            System.out.println("Item updated successfully.");
        }
    }

    private static void removeItem(ByteMeSystem system, Scanner scanner) {
        viewAllItems(system);
        System.out.print("Enter item id to remove-> ");
        String id = scanner.nextLine();

        system.removeMenuItem(id);
        System.out.println("Item removed successfully.");
    }

    private static void viewAllItems(ByteMeSystem system) {
        System.out.println("\nCurrent Menu Items->");
        for (Item item : system.getMenu()) {
            System.out.println(item);
        }
    }

    private static void orderManagement(ByteMeSystem system, Scanner scanner) {
        while (true) {
            System.out.println("\nOrder Management");
            System.out.println("1. View pending orders");
            System.out.println("2. Update order status");
            System.out.println("3. Process refund");
            System.out.println("4. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                viewPendingOrders(system);
            } else if (choice == 2) {
                updateOrderStatus(system, scanner);
            } else if (choice == 3) {
                processRefund(system, scanner);
            } else if (choice == 4) {
                return;
            } else {
                System.out.println("Invalid choice");
            }
        }
    }

    private static void viewPendingOrders(ByteMeSystem system) {
        System.out.println("\nPending Orders:");
        List<Order> pendingOrders = system.getPendingOrders();
        if (pendingOrders.isEmpty()) {
            System.out.println("no pending orders");
            return;
        }
        for (Order order : pendingOrders) {
            System.out.println("\nOrder id-> " + order.getOrderId());
            System.out.println("Customer id-> " + order.getCustomerId());
            System.out.println("Status-> " + order.getStatus());
            System.out.println("VIP Order-> " + order.isVipOrder());
            System.out.println("Special Request-> " + order.getSpecialRequest());
            System.out.println("Items->");
            order.getItems().forEach((item, quantity) ->
                    System.out.println(item.getName() + " - " + quantity + "no."));
            System.out.println("Total Amount-> ₹" + order.getTotalAmount());
        }
    }

    private static void updateOrderStatus(ByteMeSystem system, Scanner scanner) {
        viewPendingOrders(system);
        List<Order> pendingOrders = system.getPendingOrders();
        if (pendingOrders.isEmpty()) {
            return;
        }
        System.out.print("Enter order id to update: ");
        String orderId = scanner.nextLine();

        System.out.println("Available status:");
        for (Order.OrderStatus status : Order.OrderStatus.values()) {
            System.out.println("- " + status);
        }
        System.out.print("Enter new status: ");
        String status = scanner.nextLine();
        system.updateOrderStatus(orderId, Order.OrderStatus.valueOf(status));
        System.out.println("Order status updated");
    }

    private static void processRefund(ByteMeSystem system, Scanner scanner) {
        System.out.print("Enter order id for refund: ");
        String orderId = scanner.nextLine();

        system.processRefund(orderId);
        System.out.println("Refund processed");
    }

    private static void generateReports(ByteMeSystem system) {
        Map<String, Double> report = system.generateDailySalesReport(java.time.LocalDateTime.now());
        Map<Item, Integer> popularItems = system.getMostPopularItems(java.time.LocalDateTime.now());

        System.out.println("\nDaily Sales Report");
        System.out.printf("Total Sales: ₹%.2f%n", report.get("totalSales"));
        System.out.printf("Total Orders: %.0f%n", report.get("totalOrders"));

        // Display the most popular item
        if (!popularItems.isEmpty()) {
            Item mostPopular = popularItems.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .get()
                    .getKey();
            int quantity = popularItems.get(mostPopular);
            System.out.printf("Most Popular Item: %s - %d sold%n", mostPopular.getName(), quantity);
        } else {
            System.out.println("No items sold today.");
        }
    }
}
