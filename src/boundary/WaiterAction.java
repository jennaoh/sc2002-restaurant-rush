package boundary;

public class WaiterAction {

    // Types of actions the waiter can perform
    public enum Type {
        WAIT,
        SEAT,
        TAKE_ORDER,
        SERVE
    }

    private Type type;
    private int targetId;

    // Constructor
    public WaiterAction(Type type, int targetId) {
        this.type = type;
        this.targetId = targetId;
    }

    // Getters
    public Type getType() {
        return type;
    }

    public int getTargetId() {
        return targetId;
    }
}
