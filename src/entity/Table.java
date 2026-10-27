package entity;
//represents one restaurant table

// each table can only accommodate at most one customer
//table remains occupied until the customer pays or abandons the restaurant (0 satisfaction)

public class Table {
    private final int tableNumber;
    private Customer currentCustomer;

    public Table(int tableNumber) {
        this.tableNumber = tableNumber;
        this.currentCustomer = null;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    // true if a customer is occupying the table
    public boolean isOccupied() {
        return currentCustomer != null;
    }

    // Seats a customer if the table is available
    // return true if seating succeeds, false otherwise
    public boolean seatCustomer(Customer customer) {
        if (customer == null || isOccupied()) {
            return false;
        }

        currentCustomer = customer;
        return true;
    }

    // returns the customer currently occupying this table
    public Customer getCustomer() {
        return currentCustomer;
    }

    // frees the table after payment or customer abandonment
    public void clear() {
        currentCustomer = null;
    }

    @Override
    public String toString() {
        if (!isOccupied()) {
            return "Table " + tableNumber + " (Available)";
        }

        return "Table " + tableNumber + " (Customer " + currentCustomer.getId() + ")";
    }
}
