package boundary;

import java.util.List;
import java.util.Scanner;
import entity.Restaurant;
import entity.Customer;

public class ConsoleUI {

    private Scanner scanner = new Scanner(System.in);

    public void displayTurnStart(Restaurant res, int turn) {
        // Print turn number, revenue, average satisfaction of paid customers
        // waiting queue, occupied tables, each customer’s satisfaction
        // order status, and the staff tasks available.

        System.out.println("Turn " + turn + " - " + "Current Revenue: " + res.getRevenueCents() / 100.0 + " - "
                + "Average Satisfaction: " + res.getAveragePaidSatisfaction());
        System.out.println("---------------------------------------");
        // Waiting queue display
        System.out.println("Waiting Queue: " + res.getWaitingQueue());
        // Occupied tables display
        System.out.println("Occupied Tables: " + res.getOccupiedTables());
        // Customer satisfaction display
        // to do: edit to display cust id andsatisfaction only
        System.out.println("All customers: " + res.getAllCustomers());
        // Display orders
        System.out.println("Active Orders: " + res.getOrders());
        // Staff tasks available
        System.out.println("Staff tasks available: "); // idk
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
                    int customerId = readInt("Customer ID: ");
                    // int tableId = readInt("Table ID: ");
                    return new WaiterAction(
                            WaiterAction.Type.SEAT, customerId);

                case 2:
                    int orderCustomerId = readInt("Customer ID: ");
                    return new WaiterAction(
                            WaiterAction.Type.TAKE_ORDER, orderCustomerId);

                case 3:
                    int orderId = readInt("Order ID: ");
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

    public ChefAction promptChefAction(Restaurant res) {

        while (true) {
            System.out.println("\nChoose Chef Action:");
            System.out.println("1. Prepare Order");
            System.out.println("2. Wait");

            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1:
                    int orderId = readInt("Order ID: ");
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
    }

    public void displayTurnSummary(Restaurant res, int turn) {
        // Print end-of-turn statistics
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
