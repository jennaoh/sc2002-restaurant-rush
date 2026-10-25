package staff;

import entity.*;

/** Advances one order by one preparation unit per turn. */
public class Chef extends Staff {

    public Chef() { super("Chef", 1); }

    public void prepareOrder(Order o) {
        if (!hasTaskLeft()) throw new IllegalStateException("Chef has already worked this turn.");
        if (o.getStatus() != OrderStatus.PLACED && o.getStatus() != OrderStatus.IN_PROGRESS)
            throw new IllegalStateException("That order cannot be prepared (it is " + o.getStatus() + ").");
        useTask();
        o.advancePrep();
    }
}
