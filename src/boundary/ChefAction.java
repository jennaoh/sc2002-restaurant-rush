package boundary;

public class ChefAction {

    // Types of actions the chef can perform
    public enum Type {
        WAIT,
        PREPARE
    }

    private Type type;
    private int orderId;

    // Constructor
    public ChefAction(Type type, int orderId) {
        this.type = type;
        this.orderId = orderId;
    }

    // Getters
    public Type getType() {
        return type;
    }

    public int getOrderId() {
        return orderId;
    }
}
