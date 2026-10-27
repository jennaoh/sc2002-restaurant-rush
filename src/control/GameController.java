package control;

import entity.*;
import boundary.ConsoleUI;

public class GameController {
    private Restaurant restaurant;
    private ConsoleUI ui;
    private int currentTurn = 1;
    private final int MAX_TURNS = 18;

    //winning conditions
    private static final int TARGET_PAID_CUSTOMERS = 4;
    private static final int TARGET_REVENUE_CENTS = 4500; // $45.00 stored in cents
    private static final double TARGET_MIN_AVG_SATISFACTION = 60.0;

    public GameController(Restaurant restaurant, ConsoleUI ui) {
        this.restaurant = restaurant;
        this.ui = ui;
        this.currentTurn = 1;
    }

    public void runGame() {
        while (currentTurn <= MAX_TURNS) {
            // todo turn logic

            currentTurn++;
        }

        boolean won = evaluateVictoryConditions();
        ui.displayFinalResult(won);
    }

    public void evaluateVictoryConditions() {
        boolean meetsPaidTarget = restaurant.getPaidCustomerCount() >= TARGET_PAID_CUSTOMERS;
        boolean meetsRevenueTarget = restaurant.getRevenueCents() >= TARGET_REVENUE_CENTS;
        boolean meetsSatisfactionTarget = restaurant.getAveragePaidSatisfaction() >= TARGET_MIN_AVG_SATISFACTION;

        return meetsPaidTarget && meetsRevenueTarget && meetsSatisfactionTarget;
    }
    
    //moved from restaurant.java, these should be handled by game controller
    // TURN MANAGEMENT
    //starts a new turn and resets each staff member's task limit
    //calling this repeatedly for the same turn does not reset staff tasks a second time
    public void startTurn(int turn) {
        if (turn < 1 || turn > MAX_TURNS) {
            throw new IllegalArgumentException(
                    "Turn must be between 1 and " + MAX_TURNS);
        }

        if (turn != currentTurn) {
            currentTurn = turn;
            newlyReadyToPayThisTurn.clear();
            
            for (Staff member : staff) {
                member.resetTurn();
            }
        }
    }

    //adds a customer on turns 1, 4, 7, 10, 13 and 16.
    //call this method every turn, even when no customer arrives
    public void spawnCustomer(int turn) {

        startTurn(turn);

        boolean isArrivalTurn =
                turn == 1 ||(turn >= 4 && turn <= 16 && (turn - 4) % 3==0);

        if (isArrivalTurn) {
            Customer customer = new Customer(nextCustomerId, turn);

            waitingQueue.offer(customer);
            nextCustomerId++;
        }
    }

}
