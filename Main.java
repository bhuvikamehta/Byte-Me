import java.util.Scanner;

public class Main {
    private static ByteMeSystem system;
    private static Scanner scanner;

    public static void main(String[] args) {
        system = new ByteMeSystem();
        scanner = new Scanner(System.in);
        initializeSampleMenuItems();

        while (true) {
            System.out.println("\nWelcome to Byte Me!!");
            System.out.println("1. Admin Login");
            System.out.println("2. Customer Login");
            System.out.println("3. New Customer Registration");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if(choice==1){
                adminLogin();
            }
            else if (choice==2){
                customerLogin();
            }
            else if (choice==3){
                registerNewCustomer();
            }
            else if (choice==4){
                System.out.println("thank you for using Byte Me!");
                System.out.println("exiting the application...");
                return;
            }
            else System.out.println("invalid choice");
        }
    }

    private static void customerLogin() {
        System.out.print("enter customer id: ");
        String customerId = scanner.nextLine();
        Customer customer = system.getCustomer(customerId);
        if (customer != null) {
            CustomerInterface.showMenu(system, scanner, customer);
        }
        else System.out.println("invalid customer id");
    }

    private static void adminLogin() {
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();
        if (!password.equals("1234")) {
            System.out.println("Invalid password!");
        }
        else AdminInterface.showMenu(system, scanner);
    }

    static int initialId=12625348;

    private static void registerNewCustomer() {
        System.out.print("enter name: ");
        String name = scanner.nextLine();
        String id = "C" + initialId;
        initialId+=1;
        system.registerCustomer(id, name);
        System.out.println("registration successful! your customer id is: " + id);
    }

    private static void initializeSampleMenuItems() {
        system.addMenuItem("1", "paneer parantha", 40.0, "main course");
        system.addMenuItem("2", "maggi", 35.0, "fast food");
        system.addMenuItem("3", "veg sandwich", 80.0, "snacks");
        system.addMenuItem("4", "tea", 50.0, "beverages");
        system.addMenuItem("5", "cold coffee", 40.0, "beverages");
    }
}
