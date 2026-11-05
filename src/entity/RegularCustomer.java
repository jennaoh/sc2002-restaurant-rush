package entity;

public class RegularCustomer extends Customer {

    private static final int REGULAR_DECAY_RATE = 8;

    public RegularCustomer(int id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    @Override
    public int getDecayRate() {
        return REGULAR_DECAY_RATE;
    }

    @Override
    public int calculateBill(int basePriceCents) {
        return basePriceCents;
    }

    @Override
    public String getCustomerType() {
        return "Regular";
    }

    @Override
    public MenuItem getOrderPreference(Menu m) {
        // Documented Rule: Customers with odd IDs prefer the ComboMeal
        if (getId() % 2 != 0) {
            for (MenuItem item : m.getItems()) {
                if (item instanceof ComboMenuItem) {
                    return item;
                }
            }
        }

        // Even IDs (or fallback if no combo exists): follow regular rotation
        return m.selectItem(this);
    }
}