package entity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import boundary.WaiterAction;
import boundary.ChefAction;
import staff.Staff;
import staff.Waiter;
import staff.Chef;
import staff.Cashier;

//manages the restaurant's game state and business rules
public class Restaurant {

    private static final int SATISFACTION_LOSS = 8;

    private int revenueCents = 0;
    private int unhappyDepartures = 0;

    private final List<Table> tables = new ArrayList<>();
    private final Queue<Customer> waitingQueue = new ArrayDeque<>();
    private final List<Order> orders = new ArrayList<>();
    private final List<Customer> paidCustomers = new ArrayList<>();

    private final Menu menu = new Menu();
    private final List<Staff> staff = new ArrayList<>();

    private int nextOrderId = 1;

    // customers who became READY_TO_PAY during this turn, they must not pay until a
    // subsequent turn
    private final Set<Integer> newlyReadyToPayThisTurn = new HashSet<>();

    // constructor
    public Restaurant() {

        // create two tables
        tables.add(new Table(1));
        tables.add(new Table(2));

        // create one staff member of each type.
        staff.add(new Waiter());
        staff.add(new Chef());
        staff.add(new Cashier());
    }

    // WAITER ACTIONS
    // executes one Waiter action
    public void executeWaiterAction(WaiterAction action, int turn) {
        // startTurn(turn);

        if (action == null) {
            throw new IllegalArgumentException("Waiter action cannot be null.");
        }

        String type = action.getType().name();
        int targetId = action.getTargetId();

        Waiter waiter = getWaiter();

        // waiting does not change the restaurant state
        if (type.equals("WAIT")) {
            return;
        }

        switch (type) {

            case "SEAT": {
                // target -1 means the customer at the front of the waiting queue
                Customer customer;

                if (targetId == -1) {
                    customer = waitingQueue.peek();
                } else {
                    customer = findWaitingCustomerById(targetId);
                }

                if (customer == null) {
                    throw new IllegalArgumentException("That customer is not in the waiting queue.");
                }

                Table freeTable = findFreeTable();

                if (freeTable == null) {
                    throw new IllegalStateException("There are no free tables.");
                }

                // waiter validates the action and uses one task
                waiter.seatCustomer(customer, freeTable);

                // remove the seated customer from the queue
                waitingQueue.remove(customer);
                break;
            }

            case "TAKE_ORDER": {
                Table table = findTableByNumber(targetId);

                if (table == null) {
                    throw new IllegalArgumentException("Table " + targetId + " does not exist.");
                }

                Order order = waiter.takeOrder(table, menu, nextOrderId);

                order.getCustomer().assignOrder(order);
                orders.add(order);

                nextOrderId++;
                break;
            }

            case "SERVE": {

                Order order = findOrderById(targetId);

                if (order == null) {
                    throw new IllegalArgumentException("Order " + targetId + " does not exist.");
                }

                // Waiter checks that the order is already READY
                // the controller must execute this action before the Chef action during each
                // turn
                waiter.serveOrder(order, turn);
                break;
            }

            default:
                throw new IllegalArgumentException("Unknown Waiter action: " + type);
        }
    }

    // CHEF ACTIONS
    public void executeChefAction(ChefAction action) {

        if (action == null) {
            throw new IllegalArgumentException("Chef action cannot be null.");
        }

        String type = action.getType().name();

        if (type.equals("WAIT")) {
            return;
        }
        switch (type) {

            case "PREPARE": {

                Order order = findOrderById(action.getOrderId());

                if (order == null) {
                    throw new IllegalArgumentException(
                            "Order " + action.getOrderId() + " does not exist.");
                }

                // chef advances the selected order by exactly one preparation unit
                // this also allows the Chef to work on an order taken by the Waiter earlier in
                // the same turn
                getChef().prepareOrder(order);
                break;
            }

            default:
                throw new IllegalArgumentException(
                        "Unknown Chef action: " + type);
        }
    }

    // EATING AND PAYMENT
    // customers served on an earlier turn finish one full turn of eating and become
    // READY_TO_PAY
    // this method records customers who became READY_TO_PAY, during this turn, so
    // they cannot pay immediately
    public void updateEatingCustomers(int turn) {

        // startTurn(turn);

        for (Table table : tables) {

            Customer customer = table.getCustomer();

            if (customer == null) {
                continue;
            }

            if (customer.getStatus() == CustomerStatus.SERVED
                    && customer.getTurnServed() >= 0
                    && customer.getTurnServed() < turn) {

                customer.setStatus(CustomerStatus.READY_TO_PAY);

                newlyReadyToPayThisTurn.add(customer.getId());
            }
        }
    }

    // automatically processes at most one eligible payment, the oldest eligible
    // customer pays first
    public Customer processCashierPayment() {

        Cashier cashier = getCashier();

        if (!cashier.hasTaskLeft()) {
            return null;
        }

        Customer oldestCustomer = null;

        // find the oldest eligible customer at a table
        for (Table table : tables) {

            Customer customer = table.getCustomer();

            if (customer == null) {
                continue;
            }

            if (customer.getStatus() != CustomerStatus.READY_TO_PAY) {
                continue;
            }

            // a customer who became ready this turn cannot pay yet
            if (newlyReadyToPayThisTurn.contains(customer.getId())) {
                continue;
            }

            if (oldestCustomer == null
                    || customer.getArrivalTurn() < oldestCustomer.getArrivalTurn()
                    || (customer.getArrivalTurn() == oldestCustomer.getArrivalTurn()
                            && customer.getId() < oldestCustomer.getId())) {

                oldestCustomer = customer;
            }
        }

        // no eligible payment this turn
        if (oldestCustomer == null) {
            return null;
        }

        Order order = oldestCustomer.getOrder();
        Table table = findTableForCustomer(oldestCustomer);

        if (order == null || table == null) {
            throw new IllegalStateException(
                    "Customer, order or table state is inconsistent.");
        }

        // cashier marks the customer and order as paid and frees the table
        int amountPaid = cashier.collectPayment(
                oldestCustomer, order, table);

        revenueCents += amountPaid;

        paidCustomers.add(oldestCustomer);

        return oldestCustomer;
    }

    // SATISFACTION AND ABANDONMENT
    // reduces satisfaction by 8 for each customer who is still waiting, seated or
    // ordered but has not been served
    // customers reaching zero satisfaction abandon the restaurant, their unfinished
    // orders are cancelled and their tables freed
    public List<Customer> applySatisfactionDecay() {

        List<Customer> candidates = new ArrayList<>();
        Set<Integer> includedIds = new HashSet<>();

        // include customers in the waiting queue
        for (Customer customer : waitingQueue) {
            if (includedIds.add(customer.getId())) {
                candidates.add(customer);
            }
        }

        // include customers occupying tables
        for (Table table : tables) {

            Customer customer = table.getCustomer();

            if (customer != null
                    && includedIds.add(customer.getId())) {

                candidates.add(customer);
            }
        }

        List<Customer> abandonedCustomers = new ArrayList<>();

        for (Customer customer : candidates) {

            CustomerStatus status = customer.getStatus();

            boolean hasNotBeenServed = status == CustomerStatus.WAITING_IN_QUEUE
                    || status == CustomerStatus.SEATED
                    || status == CustomerStatus.ORDERED;

            if (!hasNotBeenServed) {
                continue;
            }

            customer.decreaseSatisfaction(SATISFACTION_LOSS);

            if (customer.getSatisfaction() == 0) {

                abandonCustomer(customer);
                abandonedCustomers.add(customer);
            }
        }

        return abandonedCustomers;
    }

    // handles a customer who reaches zero satisfaction
    private void abandonCustomer(Customer customer) {

        // remove the customer from the queue, if in queue
        waitingQueue.remove(customer);

        // if seated, free the customer's table
        Table table = findTableForCustomer(customer);

        if (table != null) {
            table.clear();
        }

        // cancel any order that has not been paid
        Order order = customer.getOrder();

        if (order != null
                && order.getStatus() != OrderStatus.PAID) {

            order.setStatus(OrderStatus.CANCELLED);
        }

        customer.setStatus(CustomerStatus.ABANDONED);

        unhappyDepartures++;
    }

    // HELPER METHODS
    private Customer findWaitingCustomerById(int customerId) {
        for (Customer customer : waitingQueue) {
            if (customer.getId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    private Table findFreeTable() {
        for (Table table : tables) {
            if (!table.isOccupied()) {
                return table;
            }
        }
        return null;
    }

    private Table findTableByNumber(int tableNumber) {
        for (Table table : tables) {
            if (table.getTableNumber() == tableNumber) {
                return table;
            }
        }
        return null;
    }

    private Table findTableForCustomer(Customer customer) {

        for (Table table : tables) {

            if (table.getCustomer() == customer) {
                return table;
            }
        }
        return null;
    }

    private Order findOrderById(int orderId) {

        for (Order order : orders) {

            if (order.getId() == orderId) {
                return order;
            }
        }
        return null;
    }

    // find the required staff member from the staff list
    private <T extends Staff> T findStaff(Class<T> staffType) {

        for (Staff member : staff) {

            if (staffType.isInstance(member)) {
                return staffType.cast(member);
            }
        }

        throw new IllegalStateException("Staff member not found: " + staffType.getSimpleName());
    }

    public void addWaitingCustomer(Customer customer) {
        waitingQueue.offer(customer);
    }

    // to be called by game controller to clear this list
    public void clearNewlyReadyToPayThisTurn() {
        newlyReadyToPayThisTurn.clear();
    }

    // GETTERS
    public Waiter getWaiter() {
        return findStaff(Waiter.class);
    }

    public Chef getChef() {
        return findStaff(Chef.class);
    }

    public Cashier getCashier() {
        return findStaff(Cashier.class);
    }

    public int getRevenueCents() {
        return revenueCents;
    }

    public double getRevenueDollars() {
        return revenueCents / 100.0;
    }

    public int getUnhappyDepartures() {
        return unhappyDepartures;
    }

    public int getPaidCustomerCount() {
        return paidCustomers.size();
    }

    // counts orders that have been served, including orders whose customers have
    // subsequently paid
    public int getServedCount() {
        int count = 0;
        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.SERVED || order.getStatus() == OrderStatus.PAID) {
                count++;
            }
        }
        return count;
    }

    // returns the mean satisfaction recorded at payment, NaN when no customers have
    // paid
    public double getAveragePaidSatisfaction() {
        if (paidCustomers.isEmpty()) {
            return Double.NaN;
        }

        int totalSatisfaction = 0;
        for (Customer customer : paidCustomers) {
            totalSatisfaction += customer.getSatisfaction();
        }
        return (double) totalSatisfaction / paidCustomers.size();
    }

    public Menu getMenu() {
        return menu;
    }

    public List<Table> getTables() {
        return Collections.unmodifiableList(
                new ArrayList<>(tables));
    }

    public List<Table> getOccupiedTables() {
        List<Table> allTables = getTables();
        List<Table> occupiedTables = new ArrayList<>();
        for (Table table : allTables) {
            if (table.isOccupied()) {
                occupiedTables.add(table);
            }
        }
        return occupiedTables;
    }

    // test if this works
    public List<Customer> getAllCustomers() {
        List<Customer> allCustomers = new ArrayList<>();
        allCustomers.addAll(waitingQueue);
        List<Table> occupiedTables = getOccupiedTables();
        for (Table table : occupiedTables) {
            allCustomers.add(table.getCustomer());
        }
        return allCustomers;
    }

    public Queue<Customer> getWaitingQueue() {
        return new ArrayDeque<>(waitingQueue);
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(
                new ArrayList<>(orders));
    }

    public List<Customer> getPaidCustomers() {
        return Collections.unmodifiableList(
                new ArrayList<>(paidCustomers));
    }

    public List<Staff> getStaff() {
        return Collections.unmodifiableList(
                new ArrayList<>(staff));
    }
}
