package staff;

import entity.*;

/**
 * Seats customers, takes orders and serves READY dishes: one task per turn.
 */
public class Waiter extends Staff {
    @Override
    public String getAvailableTaskDescription() {
        return hasTaskLeft() ? "1 task available (Seat / Take Order / Serve)" : "Task completed";
    }

    public Waiter() { 
        super("Waiter", 1); 
    }

    public void seatCustomer(Customer c, Table t) {
        if (!hasTaskLeft()) throw new IllegalStateException("Waiter has already acted this turn.");
        if (c.getStatus() != CustomerStatus.WAITING_IN_QUEUE)
            throw new IllegalStateException("That customer is not waiting in the queue.");
        if (t.isOccupied())
            throw new IllegalStateException("Table " + t.getTableNumber() + " is occupied.");
        useTask();
        t.seatCustomer(c);
        c.setStatus(CustomerStatus.SEATED);
    }

    public Order takeOrder(Table t, Menu m, int id, HappyHour happyHour) {
        if (!hasTaskLeft()) throw new IllegalStateException("Waiter has already acted this turn.");
        Customer c = t.getCustomer();
        if (c == null || c.getStatus() != CustomerStatus.SEATED)
            throw new IllegalStateException("There is no seated customer at that table waiting to order.");
        useTask();

        MenuItem item = c.getOrderPreference(m);

        // 1. VIP / Customer discount applied first
        int priceAfterCustomer = c.calculateBill(item.getPriceCents());

        // 2. Happy Hour discount applied second if active on the happyHour instance
        int lockedPrice = happyHour.applyDiscount(priceAfterCustomer);

        // 3. Price is locked when order is taken
        Order o = new Order(id, c, item, lockedPrice);
        
        c.setStatus(CustomerStatus.ORDERED);
        return o;
    }

    /** Serves an order that was already READY before the Waiter acted this turn. */
    public void serveOrder(Order o, int turn) {
        if (!hasTaskLeft()) throw new IllegalStateException("Waiter has already acted this turn.");
        if (!o.isReady())
            throw new IllegalStateException(o.getItem().getName() + " is not READY yet.");
        useTask();
        o.setStatus(OrderStatus.SERVED);
        o.getCustomer().setStatus(CustomerStatus.SERVED);
        o.getCustomer().setTurnServed(turn);
    }
}