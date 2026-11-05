package staff;

import entity.*;
public class Host extends Staff{
    @Override
    public String getAvailableTaskDescription() {
        return hasTaskLeft() ? "1 seating available" : "Seating completed";
    }

    public Host() { super("Host", 1); }

    public void seatCustomer(Customer c, Table t) {
        if (!hasTaskLeft()) {
            throw new IllegalStateException("Host has already seated a customer this turn.");
        }
        if (c.getStatus() != CustomerStatus.WAITING_IN_QUEUE) {
            throw new IllegalStateException("That customer is not in the waiting queue.");
        }
        if (t.isOccupied()) {
            throw new IllegalStateException("Table " + t.getTableNumber() + " is occupied.");
        }
        useTask();
        t.seatCustomer(c);
        c.setStatus(CustomerStatus.SEATED);
    }
}
