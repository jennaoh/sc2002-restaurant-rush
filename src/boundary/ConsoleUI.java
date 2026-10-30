package boundary;

import java.util.List;
import java.util.Scanner;
import entity.Restaurant;
import entity.Customer;
import entity.Order;

public class ConsoleUI {

    private Scanner scanner = new Scanner(System.in);

    public void displayTurnStart(Restaurant res, int turn) {
        // Print turn number, revenue, average satisfaction of paid customers
        // waiting queue, occupied tables, each customer’s satisfaction
        // order status, and the staff tasks available.
        System.out.println("\n".repeat(15));
        System.out.println(
                "Turn " + turn + " - " + "Current Revenue: $" + String.format("%.2f", res.getRevenueDollars()) + " - "
                        + "Average Satisfaction: " + res.getAveragePaidSatisfaction());
        System.out.println("-".repeat(100));
        // Waiting queue display
        System.out.println("Waiting Queue: " + res.getWaitingQueue());
        // Occupied tables display
        System.out.println("Occupied Tables: " + res.getOccupiedTables());
        // Customer satisfaction display
        // to do: edit to display cust id andsatisfaction only
        System.out.println("Customer Satisfaction: " + res.getAllCustomers());
        // Display orders
        System.out.println("Active Orders: " + res.getOrders());
        // Staff tasks available
        System.out.println("Staff tasks available: "); // to do
    }

    public WaiterAction promptWaiterAction(Restaurant res) {

        while (true) {
            System.out.println("\nChoose Waiter Action:");
            System.out.println("1. Seat Customer");
            System.out.println("2. Take Order");
            System.out.println("3. Serve Order");
            System.out.println("4. Wait");

            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1:
                    int customerId = readInt("Customer ID to seat: ");
                    return new WaiterAction(
                            WaiterAction.Type.SEAT, customerId);

                case 2:
                    int orderTableId = readInt("Table ID to take order from: ");
                    return new WaiterAction(
                            WaiterAction.Type.TAKE_ORDER, orderTableId);

                case 3:
                    int orderId = readInt("Order ID to serve: ");
                    return new WaiterAction(
                            WaiterAction.Type.SERVE, orderId);

                case 4:
                    return new WaiterAction(
                            WaiterAction.Type.WAIT, -1);

                default:
                    displayError("Please choose 1 to 4.");
            }
        }
    }

    public void displayOrderTaken(Order order) {
        System.out.println("Order Placed: " + order.getItem().getName()
                + " (Order #" + order.getId() + ") for Customer "
                + order.getCustomer().getId() + " - [" + order.getItem().getPrepUnits() + " PREP UNITS]");
    }

    public void displayOrderServed(Order order) {
        System.out.println("Order Served: " + order.getItem().getName()
                + " (Order #" + order.getId() + ") to Customer "
                + order.getCustomer().getId());
    }

    public ChefAction promptChefAction(Restaurant res) {

        while (true) {
            System.out.println("\nChoose Chef Action:");
            System.out.println("1. Prepare Order");
            System.out.println("2. Wait");

            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1:
                    int orderId = readInt("Order ID to prepare: ");
                    return new ChefAction(
                            ChefAction.Type.PREPARE, orderId);

                case 2:
                    return new ChefAction(
                            ChefAction.Type.WAIT, -1);

                default:
                    displayError("Please choose 1 or 2.");
            }
        }
    }

    public void displayError(String msg) {
        System.out.println("Invalid action: " + msg);
    }

    public void displayPaymentSuccess(Customer c) {
        // Print payment details
    }

    public void displayDepartures(List<Customer> left) {
        // Print customers who abandoned the restaurant
        System.out.println("\n".repeat(4));
        for (Customer cust : left) {
            System.out.println("Customer " + cust.getId() + " was unsatisfied and left!");
        }
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void displayTurnSummary(Restaurant res, int turn) {
        // Print end-of-turn statistics
        System.out.println("\n".repeat(15));
        System.out.println("TURN " + turn + " ENDED");
        System.out.println("Turn Statistics");
        System.out.println("-".repeat(100));
        System.out.println("Served Count: " + res.getServedCount());
        System.out.println("Unhappy Departures: " + res.getUnhappyDepartures());
        System.out.println("Current Revenue: $" + String.format("%.2f", res.getRevenueDollars()));
        System.out.println("Average Satisfaction of Paid Customers: " + res.getAveragePaidSatisfaction());
        // using hardcoded max turns here
        System.out.println("FINISHED TURN " + turn + " OUT OF 18");
        System.out.println((18 - turn) + " TURNS LEFT");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void displayFinalResult(boolean won) {
        System.out.println(won ? "VICTORY!" : "DEFEAT!");
        // Print final statistics
    }

    private int readInt(String message) {
        while (true) {
            System.out.print(message);

            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                displayError("Please enter a valid number.");
            }
        }
    }

}
