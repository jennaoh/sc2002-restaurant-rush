package entity;

public abstract class Customer {
    private final int id;
    private final int arrivalTurn;

    private int satisfaction;
    private CustomerStatus status;
    private Order order;
    private int turnServed;

    public abstract int getDecayRate();

    public abstract int calculateBill(int basePriceCents);

    public abstract MenuItem getOrderPreference(Menu menu);

    public abstract String getCustomerType();

    public Customer(int id, int arrivalTurn) {
        this.id = id;
        this.arrivalTurn = arrivalTurn;
        this.satisfaction = 100;
        this.status = CustomerStatus.WAITING_IN_QUEUE;
        this.order = null;
        this.turnServed = -1;
    }

    public int getId() {
        return id;
    }

    public int getArrivalTurn() {
        return arrivalTurn;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public Order getOrder() {
        return order;
    }

    public int getTurnServed() {
        return turnServed;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public void assignOrder(Order order) {
        this.order = order;
    }

    public void setTurnServed(int turnServed) {
        this.turnServed = turnServed;
    }

    // decrease satisfaction by the specified points (8 in stage 1)
    // with a min of 0, until food is served
    public void decreaseSatisfaction(int pts) {
        if (status == CustomerStatus.WAITING_IN_QUEUE
                || status == CustomerStatus.SEATED
                || status == CustomerStatus.ORDERED) {
            satisfaction = Math.max(0, satisfaction - pts);
        }
    }

    @Override
    public String toString() {
        return getCustomerType() + " Customer " + id
                + " | Status: " + status
                + " | Satisfaction: " + satisfaction;
    }
}
