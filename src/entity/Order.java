package entity;

public class Order {
    private final int id;
    private final Customer customer;
    private final MenuItem item;
    private final int billCents; // Locked bill at time of order
    private int prepProgress;
    private OrderStatus status;

    public Order(int id, Customer customer, MenuItem item, int billCents) {
        this.id = id;
        this.customer = customer;
        this.item = item;
        this.billCents = billCents;
        this.prepProgress = 0;
        this.status = OrderStatus.PLACED;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public MenuItem getItem() {
        return item;
    }

    public int getBillCents() {
        return billCents;
    }

    public int getPrepProgress() {
        return prepProgress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void advancePrep() {
        if (prepProgress < item.getPrepUnits()) {
            prepProgress++;
        }
        if (prepProgress == item.getPrepUnits()) {
            status = OrderStatus.READY;
        }
    }

    public boolean isReady() {
        return status == OrderStatus.READY;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order#" + id + " " + item.getName() + " [" + status + " " + prepProgress + "/" + item.getPrepUnits() + "]";
    }
}