
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private Scanner scanner = new Scanner(System.in);

    public void displayTurnStart(Restaurant res, int turn) {
        // Print turn, revenue, average satisfaction
        // Print waiting queue and tables
        // Print customers, orders and available tasks
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
                int tableId = readInt("Table ID: ");
                return new WaiterAction(
                    WaiterAction.Type.SEAT, customerId, tableId);

            case 2:
                int orderCustomerId = readInt("Customer ID: ");
                return new WaiterAction(
                    WaiterAction.Type.TAKE_ORDER, orderCustomerId, -1);

            case 3:
                int orderId = readInt("Order ID: ");
                return new WaiterAction(
                    WaiterAction.Type.SERVE, orderId, -1);

            case 4:
                return new WaiterAction(
                    WaiterAction.Type.WAIT, -1, -1);

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

    public void displayFinalResult(Restaurant res, boolean won) {
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
