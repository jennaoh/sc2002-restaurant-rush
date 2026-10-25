package entity;

/** Life cycle of an order, from placed to paid (or cancelled if the customer leaves). */
public enum OrderStatus {
    PLACED, IN_PROGRESS, READY, SERVED, PAID, CANCELLED
}
