package staff;

/** Shared by Waiter, Chef and Cashier: a name and a limited number of tasks per turn. */
public abstract class Staff {
    private final String name;
    private final int tasksPerTurn;
    private int tasksLeft;

    protected Staff(String name, int tasksPerTurn) {
        this.name = name;
        this.tasksPerTurn = tasksPerTurn;
        this.tasksLeft = tasksPerTurn;
    }

    public String getName() { return name; }
    public int getTasksPerTurn() { return tasksPerTurn; }
    public boolean hasTaskLeft() { return tasksLeft > 0; }

    /** Call at the start of every turn. */
    public void resetTurn() { tasksLeft = tasksPerTurn; }

    /** Uses up one task. Call only after the action has been checked as valid. */
    protected void useTask() {
        if (tasksLeft <= 0) {
            throw new IllegalStateException(name + " has no task left this turn.");
        }
        tasksLeft--;
    }
}
