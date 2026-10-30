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
        spawnCustomer(turn); // call spawn customer, will spawn customer on certain turns
        restaurant.clearNewlyReadyToPayThisTurn();
        ui.displayTurnStart(restaurant, currentTurn);

        // Reset task counter for each staff member at the start of each turn
        for (Staff member : restaurant.getStaff()) {
            member.resetTurn();
        }
        // Manager choices: waiter and chef actions
        handleWaiterAction(turn);
        handleChefAction();

        // Eating and Payment
        restaurant.updateEatingCustomers(turn);
        restaurant.processCashierPayment();
        List<Customer> abandonedCustomersList = restaurant.applySatisfactionDecay();
        // Check for any abandoned customers and inform the user on the UI
        if (abandonedCustomersList.size() > 0) {
            ui.displayDepartures(abandonedCustomersList);
        }
        ui.displayTurnSummary(restaurant, turn);
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

    // adds a customer on turns 1, 4, 7, 10, 13 and 16.
    // call this method every turn, even when no customer arrives
    public void spawnCustomer(int turn) {
        boolean isArrivalTurn = turn == 1 || (turn >= 4 && turn <= 16 && (turn - 4) % 3 == 0);
        if (isArrivalTurn) {
            Customer customer = new Customer(nextCustomerId, turn);

            restaurant.addWaitingCustomer(customer);
            nextCustomerId++;
        }
    }

}
