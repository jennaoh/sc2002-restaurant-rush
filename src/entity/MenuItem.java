package entity;

/** One dish on the menu. Money is stored as integer cents. */
public class MenuItem {
    private final String name;
    private final int priceCents;
    private final int prepUnits;

    public MenuItem(String name, int priceCents, int prepUnits) {
        this.name = name;
        this.priceCents = priceCents;
        this.prepUnits = prepUnits;
    }

    public String getName() { return name; }
    public int getPriceCents() { return priceCents; }
    public int getPrepUnits() { return prepUnits; }

    @Override
    public String toString() {
        return String.format("%s ($%d.%02d, %d prep)", name, priceCents / 100, priceCents % 100, prepUnits);
    }
}
