package control;

import entity.*;
import staff.Staff;
import boundary.*;

import java.util.List;

public class GameController {
    private Restaurant restaurant;
    private ConsoleUI ui;

    private int currentTurn = 1;
    private final int MAX_TURNS = 18;
    private int nextCustomerId = 1;

    // winning conditions
    // (default set to 4 paid customers, $45 revenue and 60 avg satisfaction as
    // defined in requirements)
    private static final int TARGET_PAID_CUSTOMERS = 4;
    private static final int TARGET_REVENUE_CENTS = 4500; // $45.00 stored in cents
    private static final double TARGET_MIN_AVG_SATISFACTION = 60.0;

    // constructor
    public GameController(Restaurant restaurant, ConsoleUI ui) {
        this.restaurant = restaurant;
        this.ui = ui;
        this.currentTurn = 1;
    }

    // checks if the winning conditions have been met
    public boolean evaluateVictoryConditions() {
        boolean meetsPaidTarget = restaurant.getPaidCustomerCount() >= TARGET_PAID_CUSTOMERS;
        boolean meetsRevenueTarget = restaurant.getRevenueCents() >= TARGET_REVENUE_CENTS;
        boolean meetsSatisfactionTarget = restaurant.getAveragePaidSatisfaction() >= TARGET_MIN_AVG_SATISFACTION;

        return meetsPaidTarget && meetsRevenueTarget && meetsSatisfactionTarget;
    }

    public void runGame() {
        while (currentTurn <= MAX_TURNS) {
            startTurn(currentTurn);
            currentTurn++;
        }

        boolean won = evaluateVictoryConditions();
        ui.displayFinalResult(won);
    }

    // moved from restaurant.java, these should be handled by game controller
    // TURN MANAGEMENT
    // starts a new turn and resets each staff member's task limit
    // calling this repeatedly for the same turn does not reset staff tasks a second
    // time
    public void startTurn(int turn) {
        spawnCustomer(turn);
        restaurant.clearNewlyReadyToPayThisTurn();

        // 1. HAPPY HOUR PROMPT & ACTIVATION (at start of turn)
        if (restaurant.getHappyHour().canActivate()) {
            if (ui.promptActivateHappyHour()) {
                restaurant.getHappyHour().activate();
            }
        }

        // 2. HAPPY HOUR RECOVERY (+15 satisfaction for unserved waiting customers)
        if (restaurant.getHappyHour().isActive()) {
            restaurant.applyHappyHourSatisfactionRecovery();
        }

        // Reset staff task counters before displaying start-of-turn info
        for (Staff member : restaurant.getStaff()) {
            member.resetTurn();
        }

        ui.displayTurnStart(restaurant, turn);

        handleHostAction();
        handleWaiterAction(turn);
        handleChefAction();

        // Eating and Payment
        restaurant.updateEatingCustomers(turn);
        restaurant.processCashierPayment();
        List<Customer> abandonedCustomersList = restaurant.applySatisfactionDecay();
        if (abandonedCustomersList.size() > 0) {
            ui.displayDepartures(abandonedCustomersList);
        }
        ui.displayTurnSummary(restaurant, turn);

        // 3. DECREMENT HAPPY HOUR AT TURN END
        restaurant.getHappyHour().decrementTurn();
    }

    private void handleWaiterAction(int turn) {
        boolean actionCompleted = false;
        while (!actionCompleted) {
            try {
                WaiterAction action = ui.promptWaiterAction(restaurant);
                Order order = restaurant.executeWaiterAction(action, turn);
                // print order taken or order served information to the user
                if (order != null) {
                    if (action.getType() == WaiterAction.Type.TAKE_ORDER) {
                        ui.displayOrderTaken(order);
                    } else if (action.getType() == WaiterAction.Type.SERVE) {
                        ui.displayOrderServed(order);
                    }
                }
                actionCompleted = true; // Succeeded, exit loop
            } catch (IllegalArgumentException | IllegalStateException e) {
                ui.displayError(e.getMessage());
                // Loops back and re-prompts the user
            }
        }
    }

    private void handleChefAction() {
        boolean actionCompleted = false;
        while (!actionCompleted) {
            try {
                ChefAction action = ui.promptChefAction(restaurant);
                restaurant.executeChefAction(action);
                actionCompleted = true; // Succeeded, exit loop
            } catch (IllegalArgumentException | IllegalStateException e) {
                ui.displayError(e.getMessage());
                // Loops back and re-prompts the user
            }
        }
    }

    private void handleHostAction() {
        boolean actionCompleted = false;
        while (!actionCompleted) {
            try {
                HostAction action = ui.promptHostAction(restaurant);
                restaurant.executeHostAction(action);
                actionCompleted = true;
            } catch (IllegalArgumentException | IllegalStateException e) {
                ui.displayError(e.getMessage());
            }
        }
    }

    // adds a customer on turns 1, 4, 7, 10, 13 and 16.
    // Base run includes 4 Regular, 1 VIP (Turn 7), and 1 Critic (Turn 13)
    public void spawnCustomer(int turn) {
        boolean isArrivalTurn = turn == 1 || (turn >= 4 && turn <= 16 && (turn - 4) % 3 == 0);
        if (isArrivalTurn) {
            Customer customer;
            
            // Replaces two regular arrivals with at least one VIP and one Critic
            if (turn == 7) {
                customer = new VipCustomer(nextCustomerId, turn);
            } else if (turn == 13) {
                customer = new CriticCustomer(nextCustomerId, turn);
            } else {
                customer = new RegularCustomer(nextCustomerId, turn);
            }

            restaurant.addWaitingCustomer(customer);
            nextCustomerId++;
        }
    }

}
