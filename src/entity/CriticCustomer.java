package entity;

public class CriticCustomer extends Customer {
    private static final int CRITIC_DECAY_RATE = 12;
    private static final String PREFERRED_ITEM = "Burger";

    public CriticCustomer(int id, int arrivalTurn){
        super(id, arrivalTurn);
    }
    @Override 
    public int getDecayRate(){
        return CRITIC_DECAY_RATE;
    }

    @Override 
    public int calculateBill(int basePriceCents){
        return basePriceCents;
    }

    @Override
    public String getCustomerType() {
        return "Critic";
    }

    @Override 
    public MenuItem getOrderPreference(Menu m){
        // Find and return the MenuItem matching "Burger" from Menu
        for (MenuItem item : m.getItems()) {
            if (item.getName().equalsIgnoreCase(PREFERRED_ITEM)) {
                return item;
            }
        }
        // Fallback to the first item if "Burger" is somehow not on the menu
        return m.getItems().get(0);
    }

}
