# SC2002 Restaurant Rush — AI Conversation History

## Stage 3: Customer Order Location

**AI Tool:** Gemini 3.8 Flash

**Project:** SC2002 Restaurant Rush

**Stage:** 2/3

**Purpose:** Asking AI where customer order is decided and spawning logic as I was checking out stage 2 & 3 for the first time and wanted to check the customer types as at this point, we were not printing the customer type on screen.

---

## Conversation 1 — Customer Order Logic

**Student:**

Where is the customer order decided?

**Gemini:**

The customer's order item is decided dynamically through polymorphism in Customer.getOrderPreference(Menu menu) when a waiter takes their order.

Here is the exact flow:

1. Where it is triggered

In Waiter.takeOrder(Table t, Menu m, int id, HappyHour happyHour) (invoked from Restaurant.performAction(...)):

```java
MenuItem item = c.getOrderPreference(m);
```

2. How each customer type decides their dish

Each customer subclass overrides getOrderPreference(Menu m) with its own ordering logic:

-   RegularCustomer:
    -   Odd ID: Searches the menu for a ComboMenuItem.
    -   Even ID (or fallback): Calls Menu.selectItem(Customer c), picking deterministically by ID modulo menu item count (items.get(c.getId() % items.size())).
-   CriticCusto    -   mer:
    -   Always prefers "Burger", falling back to index 0 if not on the -   menu.
-   VipCus    -   tomer:
    -   Returns index 0 of the menu items (m.getItems().get(0)).

3. Order Finalization & Pricing

Once the item is chosen in Waiter.takeOrder(...):

    1. Customer discount is applied via c.calculateBill(item.getPriceCents()) (e.g., VIP discount).
    2. Happy hour discount is applied via happyHour.applyDiscount(...).
    3. The final Order object is instantiated with the locked price and assigned to the customer.

---

## Conversation 1 — Customer Order Logic

**Student:**

What decides the customer type spawn?

**Gemini:**

Customer spawning is decided deterministically based on the game turn number inside GameController.spawnCustomer(int turn).

Spawn Schedule & Logic

Customers arrive on turns 1, 4, 7, 10, 13, and 16:

```java
// GameController.java: lines 146-163
public void spawnCustomer(int turn) {
    boolean isArrivalTurn = turn == 1 || (turn >= 4 && turn <= 16 && (turn - 4) % 3 == 0);
    if (isArrivalTurn) {
        Customer customer;
        
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
```

Breakdown by Turn:

|Turn|Customer Type Spawned|
|----|----|
|Turn 1|RegularCustomer|
|Turn 4	|RegularCustomer|
|Turn 7	|VipCustomer|
|Turn 10|RegularCustomer|
|Turn 13|CriticCustomer|
|Turn 16|RegularCustomer|

---

