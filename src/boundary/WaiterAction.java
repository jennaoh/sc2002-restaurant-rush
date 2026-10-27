package src.boundary;

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
    private int tableId;

    // Constructor
    public WaiterAction(Type type, int targetId, int tableId) {
        this.type = type;
        this.targetId = targetId;
        this.tableId = tableId;
    }

    // Getters
    public Type getType() {
        return type;
    }

    public int getTargetId() {
        return targetId;
    }

    public int getTableId() {
        return tableId;
    }
}
