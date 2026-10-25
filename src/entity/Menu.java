package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The restaurant menu: Salad, Burger and Pasta.
 *
 * Food choice rule (documented as required by the brief): deterministic.
 * The customer with id n orders items.get(n % 3), so ids 0,1,2,3... give
 * Salad, Burger, Pasta, Salad... The same run always gives the same orders.
 */
public class Menu {
    private final List<MenuItem> items = new ArrayList<>();

    public Menu() {
        items.add(new MenuItem("Salad", 900, 1));
        items.add(new MenuItem("Burger", 1500, 1));
        items.add(new MenuItem("Pasta", 1800, 2));
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /** Picks the dish this customer orders, using the deterministic rule above. */
    public MenuItem selectItem(Customer c) {
        return items.get(c.getId() % items.size());
    }
}
