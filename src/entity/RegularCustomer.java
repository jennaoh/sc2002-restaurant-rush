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
        return m.selectItem(this);
    }
}