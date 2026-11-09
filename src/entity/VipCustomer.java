package entity;

public class VipCustomer extends Customer {
    private static final int VIP_DECAY_RATE = 5;
    private static final double DISCOUNT_RATE = 0.1;

    public VipCustomer(int id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    @Override
    public int getDecayRate() {
        return VIP_DECAY_RATE;
    }

    @Override
    public int calculateBill(int basePriceCents) {
        return (int) Math.round(basePriceCents * (1.0 - DISCOUNT_RATE));
    }

    @Override
    public String getCustomerType() {
        return "VIP";
    }

    @Override
    public MenuItem getOrderPreference(Menu m) {
        // Choose based on regular selection rule (or first available item)
        return m.getItems().get(0);
    }

}