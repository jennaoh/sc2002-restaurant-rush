package boundary;
public class HostAction {
    
    // Types of action the host can perform
    public enum Type {
        SEAT,
        WAIT
    }

    private Type type;
    private int targetId;

    // Constructor
    public HostAction(Type type, int targetId) {
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
