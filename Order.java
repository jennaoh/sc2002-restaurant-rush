package entity;

/** One customer's order for one dish. */
public class Order {
    private final int id;
    private final Customer customer;
    private final MenuItem item;
    private int prepProgress = 0;
    private OrderStatus status = OrderStatus.PLACED;

    public Order(int id, Customer customer, MenuItem item) {
        this.id = id;
        this.customer = customer;
        this.item = item;
    }

    /** Does one preparation unit. Becomes READY when the last unit is done. */
    public void advancePrep() {
        prepProgress++;
        status = isDone() ? OrderStatus.READY : OrderStatus.IN_PROGRESS;
    }

    private boolean isDone() { return prepProgress >= item.getPrepUnits(); }

    public boolean isReady() { return status == OrderStatus.READY; }

    public int getId() { return id; }
    public Customer getCustomer() { return customer; }
    public MenuItem getItem() { return item; }
    public int getPrepProgress() { return prepProgress; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus s) { this.status = s; }

    /** Bill for this order in cents. */
    public int getBillCents() { return item.getPriceCents(); }

    @Override
    public String toString() {
        return String.format("Order#%d %s [%s %d/%d]", id, item.getName(), status, prepProgress, item.getPrepUnits());
    }
}
