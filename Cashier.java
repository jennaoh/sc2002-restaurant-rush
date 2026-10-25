package staff;

import entity.*;

/** Takes one payment per turn. The controller calls it automatically, oldest customer first. */
public class Cashier extends Staff {

    public Cashier() { super("Cashier", 1); }

    /** Returns the amount paid in cents. */
    public int collectPayment(Customer c, Order o, Table t) {
        if (!hasTaskLeft()) throw new IllegalStateException("Cashier has already taken a payment this turn.");
        if (c.getStatus() != CustomerStatus.READY_TO_PAY)
            throw new IllegalStateException("That customer is not ready to pay.");
        useTask();
        o.setStatus(OrderStatus.PAID);
        c.setStatus(CustomerStatus.PAID);
        t.clear();
        return o.getBillCents();
    }
}
