package boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import entity.Restaurant;
import staff.Staff;
import entity.Customer;
import entity.Order;

public class ConsoleUI {

    private Scanner scanner = new Scanner(System.in);

    private String formatAverageSatisfaction(double avg) {
        if (Double.isNaN(avg)) {
            return "N/A";
        }
        return String.format("%.2f", avg);
    }

    public void displayTurnStart(Restaurant res, int turn) {
        System.out.println("\n".repeat(15));
        System.out.println(
                "Turn " + turn + " - Current Revenue: $" + String.format("%.2f", res.getRevenueDollars())
                        + " - Average Satisfaction: " + formatAverageSatisfaction(res.getAveragePaidSatisfaction()));
        System.out.println("-".repeat(100));

        System.out.println("Waiting Queue: " + res.getWaitingQueue());
        System.out.println("Occupied Tables: " + res.getOccupiedTables());

        // Display customer satisfaction scores cleanly
        List<String> satisfactionEntries = new ArrayList<>();
        for (Customer c : res.getAllCustomers()) {
            satisfactionEntries.add(c.getCustomerType() + " Customer " + c.getId() + ": " + c.getSatisfaction());
        }
        System.out.println("Customer Satisfaction: " + (satisfactionEntries.isEmpty() ? "None" : satisfactionEntries));

        System.out.println("Active Orders: " + res.getOrders());

        // Polymorphic staff tasks display
        System.out.println("Staff tasks available:");
        for (Staff staff : res.getStaff()) {
            System.out.println(staff.getClass().getSimpleName() + ": " + staff.getAvailableTaskDescription());
        }
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
                    return new WaiterAction(WaiterAction.Type.SEAT, customerId);
                case 2:
                    int orderTableId = readInt("Table ID to take order from: ");
                    return new WaiterAction(WaiterAction.Type.TAKE_ORDER, orderTableId);
                case 3:
                    int orderId = readInt("Order ID to serve: ");
                    return new WaiterAction(WaiterAction.Type.SERVE, orderId);
                case 4:
                    return new WaiterAction(WaiterAction.Type.WAIT, -1);
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
                    return new ChefAction(ChefAction.Type.PREPARE, orderId);
                case 2:
                    return new ChefAction(ChefAction.Type.WAIT, -1);
                default:
                    displayError("Please choose 1 or 2.");
            }
        }
    }

    public void displayError(String msg) {
        System.out.println("Invalid action: " + msg);
    }

    public void displayPaymentSuccess(Customer c) {
        // Optional hook for payment feedback
    }

    public void displayDepartures(List<Customer> left) {
        System.out.println("\n".repeat(4));
        for (Customer cust : left) {
            System.out.println("Customer " + cust.getId() + " was unsatisfied and left!");
        }
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void displayTurnSummary(Restaurant res, int turn) {
        System.out.println("\n".repeat(15));
        System.out.println("TURN " + turn + " ENDED");
        System.out.println("Turn Statistics");
        System.out.println("-".repeat(100));
        System.out.println("Served Count: " + res.getServedCount());
        System.out.println("Unhappy Departures: " + res.getUnhappyDepartures());
        System.out.println("Current Revenue: $" + String.format("%.2f", res.getRevenueDollars()));
        System.out.println("Average Satisfaction of Paid Customers: "
                + formatAverageSatisfaction(res.getAveragePaidSatisfaction()));
        System.out.println("FINISHED TURN " + turn + " OUT OF 18");
        System.out.println((18 - turn) + " TURNS LEFT");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void displayFinalResult(boolean won) {
        System.out.println(won ? "VICTORY!" : "DEFEAT!");
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