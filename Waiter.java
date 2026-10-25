package staff;

import entity.*;

/**
 * Seats customers, takes orders and serves READY dishes: one task per turn.
 * Every method checks first and throws IllegalStateException with a message
 * if the choice is invalid. The task is used up only after the checks pass,
 * so a failed choice does not cost the manager the task.
 */
public class Waiter extends Staff {

    public Waiter() { super("Waiter", 1); }

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

    public Order takeOrder(Table t, Menu m, int id) {
        if (!hasTaskLeft()) throw new IllegalStateException("Waiter has already acted this turn.");
        Customer c = t.getCustomer();
        if (c == null || c.getStatus() != CustomerStatus.SEATED)
            throw new IllegalStateException("There is no seated customer at that table waiting to order.");
        useTask();
        Order o = new Order(id, c, m.selectItem(c));
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
