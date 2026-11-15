# SC2002 Restaurant Rush — Full AI Conversation History

**Course:** SC2002 Object-Oriented Design & Programming  
**Project:** Restaurant Rush (Stage 3)  
**Student:** Oh Sing Ee Jenna (Hu Xinyi) (`U2523652A`)  
**AI Tool:** ChatGPT (OpenAI)  
**Scope:** Stage 3 Implementation (`Host`, `ComboMenuItem`, `HappyHour`), Compilation Debugging, and UML Design Class Diagram (DCD) Alignment

---

## Prompt 1 — `Restaurant.java` Compilation Error

### Student Prompt
whyis there error in restaurant js

```text
7968d4a\bin' 'Main' 
Exception in thread "main" java.lang.Error: Unresolved compilation problems: 
        The method getItem(String) is undefined for the type Menu
        The method getItem(String) is undefined for the type Menu
        The method addItem(ComboMenuItem) is undefined for the type Menu
        at entity.Restaurant.<init>(Restaurant.java:56)
        at Main.main(Main.java:8)
```

**`Menu.java`:**
```java
package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The restaurant menu: Salad, Burger and Pasta.
 *
 * Food choice rule (documented as required by the brief): deterministic.
 * The customer with id n orders items.get(n % 3), so ids 0,1,2,3... give
 * Salad, Burger, Pasta, Salad... The same run always gives the same orders.
 */
public class Menu {
    private final List<MenuItem> items = new ArrayList<>();

    public Menu() {
        items.add(new MenuItem("Salad", 900, 1));
        items.add(new MenuItem("Burger", 1500, 1));
        items.add(new MenuItem("Pasta", 1800, 2));
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /** Picks the dish this customer orders, using the deterministic rule above. */
    public MenuItem selectItem(Customer c) {
        return items.get(c.getId() % items.size());
    }
}
```

**`Restaurant.java`:**
```java
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
import boundary.HostAction;
import staff.Staff;
import staff.Waiter;
import staff.Chef;
import staff.Cashier;
import staff.Host;

// manages the restaurant's game state and business rules
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
    // customers who became READY_TO_PAY during this turn, they must not pay until a subsequent turn
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
        staff.add(new Host());

        MenuItem salad = menu.getItem("Salad");
        MenuItem pasta = menu.getItem("Pasta");
        if (salad != null && pasta != null) {
            menu.addItem(new ComboMenuItem("Salad+Pasta Combo", salad, pasta));
        }
    }
    // ... (remaining Restaurant methods)
}
```

### ChatGPT Response
Your error happens because `Restaurant.java` is calling methods that do not exist in your `Menu.java` class. This is a Java compilation error, not a JavaScript error.

#### 1. What is causing the error?
In `Restaurant.java`, lines 56–59, you have:
```java
MenuItem salad = menu.getItem("Salad");
MenuItem pasta = menu.getItem("Pasta");
if (salad != null && pasta != null) {
    menu.addItem(new ComboMenuItem("Salad+Pasta Combo", salad, pasta));
}
```
But your `Menu.java` currently only provides `getItems()` and `selectItem(Customer c)`. It does not provide `getItem(String)` or `addItem(ComboMenuItem)`. That explains all three compilation errors.

#### 2. How to fix it
Add the following code inside the `Menu` class, before its final closing `}`:
```java
/** Finds a menu item by its name. */
public MenuItem getItem(String name) {
    for (MenuItem item : items) {
        if (item.getName().equalsIgnoreCase(name)) {
            return item;
        }
    }
    return null;
}

/** Adds a new item to the menu. */
public void addItem(MenuItem item) {
    if (item == null) {
        throw new IllegalArgumentException("Menu item cannot be null.");
    }
    items.add(item);
}
```
This assumes your `MenuItem` class has a `getName()` method and your `ComboMenuItem` class extends `MenuItem`.

#### 3. One more issue: Your food selection logic
Your current code is:
```java
public MenuItem selectItem(Customer c) {
    return items.get(c.getId() % items.size());
}
```
Originally, you had three dishes (Salad, Burger, Pasta). After adding the combo, your menu will contain four dishes. Because your selection uses `items.size()`, customers will now automatically cycle through all four dishes, including the combo.
* If you want to preserve the original three-dish selection rule, change it to `items.get(c.getId() % 3)`.
* If you want customers to be able to order the combo, keep your original `items.size()` implementation and update the documentation describing the selection rule.

#### 4. What to do next
1. Save `Menu.java`.
2. Confirm `ComboMenuItem` extends `MenuItem`.
3. Rebuild your Java project and run `Main.java` again.

---

## Prompt 2 — Following the Design Class Diagram

### Student Prompt
but i have to follow my class diagram  
*[Attached Screenshot of Stage 3 Design Class Diagram showing `Restaurant`, `HappyHour`, and `Menu`]*

### ChatGPT Response
Understood. Since you must follow your Design Class Diagram (DCD), we should fix `Restaurant.java` rather than add methods to `Menu.java` that are not shown in your diagram.

#### 1. What your class diagram specifies
Your `Menu` class has only three public methods:
* `public Menu()`
* `public MenuItem selectItem(Customer c)`
* `public List<MenuItem> getItems()`

There is no `getItem()` or `addItem()` in the diagram. Therefore, my previous suggestion to add those methods would introduce a mismatch with your DCD.

#### 2. Correct fix in `Restaurant.java`
Delete the combo initialization block in the `Restaurant()` constructor so it becomes:
```java
public Restaurant() {
    // Create two tables
    tables.add(new Table(1));
    tables.add(new Table(2));
    // Create one staff member of each type
    staff.add(new Waiter());
    staff.add(new Chef());
    staff.add(new Cashier());
    staff.add(new Host());
}
```
This removes all three reported compilation errors without changing your `Menu` class.

#### 3. What about `ComboMenuItem`?
Your class diagram shows that `Menu` contains a collection of `MenuItem` objects. If `ComboMenuItem` extends `MenuItem`, you can still include combos by adding them directly inside the existing `Menu()` constructor, without introducing new public methods. However, your current `Menu.java` has only three dishes (Salad, Burger, Pasta).

#### 4. Other DCD mismatches I noticed
Comparing your uploaded `Restaurant.java` with the diagram:

| Element | Class diagram | Current Java |
| :--- | :--- | :--- |
| `getItem()` / `addItem()` | Not specified | Called in `Restaurant` |
| `executeHostAction()` | Returns `void` | Returns `Order` |
| `happyHour` | Required attribute | Not present in uploaded code |
| `getHappyHour()` | Required | Not present |
| `activateHappyHour()` | Required | Not present |
| `applyHappyHourRecovery()` | Required | Not present |
| `endTurnHappyHour()` | Required | Not present |

For your final SDLC deliverable, the DCD must represent the actual implemented code. If your diagram is the required specification, update the implementation to match it; if it is outdated, revise the diagram to reflect the final implementation.

---

## Prompt 3 — Updating the DCD Instead of Removing Features

### Student Prompt
i can also change the DCD

### ChatGPT Response
In that case, I recommend updating the DCD to match your actual Java implementation, rather than deleting the `ComboMenuItem` functionality just to match the old diagram. For your final SDLC submission, the DCD should reflect the final source code.

#### 1. Fix `Menu.java`
Keep your existing code and add `getItem(String name)` and `addItem(MenuItem item)`. Your existing `Restaurant.java` constructor can then remain unchanged.

#### 2. Update the `Menu` class in your DCD
Replace the current `Menu` class box with:
```text
«entity»
Menu
-----------------------------------
- items: List<MenuItem>
-----------------------------------
+ Menu()
+ selectItem(c: Customer): MenuItem
+ getItems(): List<MenuItem>
+ getItem(name: String): MenuItem
+ addItem(item: MenuItem): void
```
Your existing `Restaurant` to `Menu` composition relationship remains valid.

#### 3. Verify `ComboMenuItem`
Your DCD should also include `ComboMenuItem` if it is part of your implemented code. If it extends `MenuItem`, represent the relationship using a generalization arrow (solid line with a hollow triangle pointing to `MenuItem`). Copy the actual attributes and methods from `ComboMenuItem.java`.

#### 4. Other updates to your DCD
Your uploaded `Restaurant.java` also shows a mismatch with `executeHostAction()`:

| DCD | Source code |
| :--- | :--- |
| `+ executeHostAction(action: HostAction): void` | `+ executeHostAction(action: HostAction): Order` |

Update the DCD return type to `Order` if that is the final method signature, and verify Happy Hour methods before retaining them in the final diagram.

---

## Prompt 4 — Implementing `ComboMeal` (Odd Customer IDs Take Combo)

### Student Prompt
i am doing stage 3. i am done with host, doing combo meal now(odd customerid take combo):

**`Menu.java`:**
```java
package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu {
    private final List<MenuItem> items = new ArrayList<>();

    public Menu() {
        items.add(new MenuItem("Salad", 900, 1));
        items.add(new MenuItem("Burger", 1500, 1));
        items.add(new MenuItem("Pasta", 1800, 2));
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /** Picks the dish this customer orders, using the deterministic rule above. */
    public MenuItem selectItem(Customer c) {
        return items.get(c.getId() % items.size());
    }

    public MenuItem getItem(String name) {
        for (MenuItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public void addItem(MenuItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Menu item cannot be null.");
        }
        items.add(item);
    }
}
```

**`RegularCustomer.java`:**
```java
package entity;

public class RegularCustomer extends Customer {
    private static final int REGULAR_DECAY_RATE = 8;

    public RegularCustomer(int id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    @Override
    public int getDecayRate() {
        return REGULAR_DECAY_RATE;
    }

    @Override
    public int calculateBill(int basePriceCents) {
        return basePriceCents;
    }

    @Override
    public String getCustomerType() {
        return "Regular";
    }

    @Override
    public MenuItem getOrderPreference(Menu m) {
        // Documented Rule: Customers with odd IDs prefer the ComboMeal
        if (getId() % 2 != 0) {
            for (MenuItem item : m.getItems()) {
                if (item instanceof ComboMenuItem) {
                    return item;
                }
            }
        }
        // Even IDs (or fallback if no combo exists): follow regular rotation
        return m.selectItem(this);
    }
}
```

*[Also pasted `Restaurant.java` and attached three screenshots of the Stage 3 Design Class Diagram covering `Restaurant`, `HappyHour`, `Menu`, `Cashier`, `Host`, `ConsoleUI`, `HostAction`, `WaiterAction`, `ChefAction`, `MenuItem`, and `ComboMenuItem`]*

### ChatGPT Response
Your Stage 3 implementation is on the right track. I've checked your code, DCD, and SC2002 assignment requirements.
* Odd customer IDs (`1, 3, 5...`) order the Salad + Pasta Combo (`$24.30`, `3` preparation units).
* Even customer IDs (`2, 4, 6...`) order a regular menu item using the deterministic rotation.

#### 1. Your current implementation
* **`Menu.java`:** Correct. Your `getItem()` and `addItem()` methods match your updated DCD.
* **`RegularCustomer.java`:** Logic correct. Your odd-ID check and `instanceof ComboMenuItem` correctly implement the preference for regular customers.
* **`Restaurant.java`:** Correct setup. Your constructor retrieves Salad and Pasta and creates a `ComboMenuItem`.
* **`Waiter.java`:** Needs verification to ensure `Waiter` calls `customer.getOrderPreference(menu)`.

#### 2. Most important: Check `Waiter.java`
Inside `Waiter.takeOrder()`, you should have:
```java
Customer customer = table.getCustomer();
MenuItem selectedItem = customer.getOrderPreference(menu);
```
If your `Waiter` currently uses `menu.selectItem(customer)`, replace that line so polymorphism delegates food selection to the customer object rather than checking customer types.

#### 3. Verify `ComboMenuItem.java`
Your combo calculations should produce:
* **Combo Price:** `$24.30` (`($9 + $18) × 90%`)
* **Preparation:** `3 units` (`Salad 1 + Pasta 2`)

Since prices are stored as integer cents:
```java
int combinedPrice = item1.getPriceCents() + item2.getPriceCents();
int comboPrice = (combinedPrice * 90 + 50) / 100;
int comboPrepUnits = item1.getPrepUnits() + item2.getPrepUnits();
```

#### 4. Test your odd/even rule
Assuming all customers are `RegularCustomer` instances, your expected selection is:

| Customer ID | Food ordered | Price |
| :--- | :--- | :--- |
| 1 | Salad + Pasta Combo | $24.30 |
| 2 | Pasta | $18.00 |
| 3 | Salad + Pasta Combo | $24.30 |
| 4 | Burger | $15.00 |
| 5 | Salad + Pasta Combo | $24.30 |
| 6 | Salad | $9.00 |

Note: Your current odd-ID rule applies only to `RegularCustomer`. VIP and Critic customers follow their own `getOrderPreference()` implementations.

#### 5. Final DCD changes
* **`Restaurant`:** Change `executeHostAction(action: HostAction)` return type from `void` to `Order`.
* **`RegularCustomer`:** Ensure `getOrderPreference(m: Menu)` appears under `RegularCustomer` and other implementing customer classes.

---

## Prompt 5 — Checking `Waiter.java` Polymorphism

### Student Prompt
current waiter

```java
package staff;

import entity.*;

/**
 * Seats customers, takes orders and serves READY dishes: one task per turn.
 * Every method checks first and throws IllegalStateException with a message
 * if the choice is invalid. The task is used up only after the checks pass,
 * so a failed choice does not cost the manager the task.
 */
public class Waiter extends Staff {
    @Override
    public String getAvailableTaskDescription() {
        return hasTaskLeft() ? "1 task available (Seat / Take Order / Serve)" : "Task completed";
    }

    public Waiter() {
        super("Waiter", 1);
    }

    public void seatCustomer(Customer c, Table t) {
        if (!hasTaskLeft()) throw new IllegalStateException("Waiter has already acted this turn.");
        if (c.getStatus() != CustomerStatus.WAITING_IN_QUEUE)
            throw new IllegalStateException("That customer is not waiting in the queue.");
        if (t.isOccupied())
            throw new IllegalStateException("Table " + t.getTableNumber() + " is occupied.");
        useTask();
        t.seatCustomer(c);
        c.setStatus(CustomerStatus.SEATED);
    }

    public Order takeOrder(Table t, Menu m, int id) {
        if (!hasTaskLeft()) throw new IllegalStateException("Waiter has already acted this turn.");
        Customer c = t.getCustomer();
        if (c == null || c.getStatus() != CustomerStatus.SEATED)
            throw new IllegalStateException("There is no seated customer at that table waiting to order.");
        useTask();
        // stage 2
        MenuItem item = c.getOrderPreference(m);
        Order o = new Order(id, c, item);
        c.setStatus(CustomerStatus.ORDERED);
        return o;
    }

    /** Serves an order that was already READY before the Waiter acted this turn. */
    public void serveOrder(Order o, int turn) {
        if (!hasTaskLeft()) throw new IllegalStateException("Waiter has already acted this turn.");
        if (!o.isReady())
            throw new IllegalStateException(o.getItem().getName() + " is not READY yet.");
        useTask();
        o.setStatus(OrderStatus.SERVED);
        o.getCustomer().setStatus(CustomerStatus.SERVED);
        o.getCustomer().setTurnServed(turn);
    }
}
```

### ChatGPT Response
Your `Waiter.java` is already correctly implemented for Stage 3 ComboMeal. You don't need to modify it.
The key line is:
```java
MenuItem item = c.getOrderPreference(m);
```
This uses polymorphism to let each customer decide what to order without the `Waiter` needing to know whether the item is a regular dish or a combo.

#### One small improvement
For stronger error handling, move `useTask()` after selecting the item:
```java
MenuItem item = c.getOrderPreference(m);
if (item == null) {
    throw new IllegalStateException("Customer has no valid menu preference.");
}
useTask();
Order o = new Order(id, c, item);
c.setStatus(CustomerStatus.ORDERED);
return o;
```
This ensures an unexpected failure to select food does not consume the Waiter's task.

---

## Prompt 6 — `TAKE_ORDER` Compilation Errors in `Restaurant.java`

### Student Prompt
```text
Exception in thread "main" java.lang.Error: Unresolved compilation problems: 
        The method getTableNumber() is undefined for the type WaiterAction
        Duplicate local variable order
        at entity.Restaurant.executeWaiterAction(Restaurant.java:133)
        at control.GameController.handleWaiterAction(GameController.java:100)
        at control.GameController.startTurn(GameController.java:79)
        at control.GameController.runGame(GameController.java:42)
        at Main.main(Main.java:11)
```

*(Pasted updated `Restaurant.java` with Happy Hour integration)*

### ChatGPT Response
You have 2 compilation errors in `Restaurant.java`, both caused by the new `TAKE_ORDER` code you added for Happy Hour.

#### 1. Error: `getTableNumber()` is undefined
Your `WaiterAction` class uses `getTargetId()`, which you already stored in `int targetId = action.getTargetId();`. Replace `action.getTableNumber()` with `targetId`.

#### 2. Error: Duplicate local variable `order`
Your `TAKE_ORDER` case does not have its own `{ }` braces, while your `SERVE` case also declares an `Order order` variable inside the same `switch` scope.

#### 3. Corrected code — replace your entire `TAKE_ORDER` block
```java
case "TAKE_ORDER": {
    Table table = findTableByNumber(targetId);
    if (table == null) {
        throw new IllegalArgumentException(
            "Table " + targetId + " does not exist."
        );
    }
    // Take order and apply Happy Hour discount
    Order order = waiter.takeOrder(
        table, menu, nextOrderId, happyHour
    );
    // Assign order to customer
    order.getCustomer().assignOrder(order);
    // Store order in restaurant
    orders.add(order);
    nextOrderId++;
    return order;
}
```
This fixes both errors and restores `assignOrder(order)`.

#### 4. DCD consistency
For your Stage 3 diagram, verify:
* `Restaurant`: `+ executeWaiterAction(action: WaiterAction, turn: int): Order`
* `Waiter`: `+ takeOrder(t: Table, m: Menu, id: int, happyHour: HappyHour): Order`
* `WaiterAction`: `+ getTargetId(): int`

---

## Prompt 7 — `ConsoleUI` and `GameController` DCD Review

### Student Prompt
*(Pasted full `ConsoleUI.java` and `GameController.java` alongside their DCD text blocks)*

do i need to change my class diagram

### ChatGPT Response
Yes. You need to make a few small changes to your DCD because your current `ConsoleUI` and `GameController` implementations have different methods from those shown in your diagram.

#### 1. `ConsoleUI` — one method to correct
Your DCD currently contains `+ promptHappyHour(res: Restaurant): boolean`, but your Java implementation is `public boolean promptActivateHappyHour()`. Change your DCD to:
```text
+ promptActivateHappyHour(): boolean
```

#### 2. `GameController` — two method corrections

| Current DCD | Required change |
| :--- | :--- |
| `- handleHostAction(turn: int): void` | `- handleHostAction(): void` |
| `- handleHappyHourChoice(): void` | Remove from DCD (handled inside `startTurn()`) |
| `- handleWaiterAction(turn: int): void` | Keep |
| `- handleChefAction(): void` | Keep |

#### 3. Do you need to change the relationships?
No major relationship changes are needed. Your existing associations (`GameController -> Restaurant`, `GameController -> ConsoleUI`) and creation/usage dependencies remain appropriate.

#### 4. Two small code issues worth correcting in `ConsoleUI.java`
* In `promptWaiterAction()`, change `displayError("Please choose 1 or 2.");` to `displayError("Please choose 1 to 4.");`.
* In `promptHostAction()`, change `displayError("Please choose 1 to 4.");` to `displayError("Please choose 1 or 2.");`.

---

## Prompt 8 — `HostAction`, `ComboMenuItem`, `Customer`, and `HappyHour` DCD Review

### Student Prompt
*(Pasted `HostAction.java`, `ComboMenuItem.java`, `Customer.java`, and `HappyHour.java` alongside their DCD text blocks)*

### ChatGPT Response
I've compared your four Java classes against your Design Class Diagram (DCD). Three classes need small DCD corrections; `HappyHour` already matches.

#### 1. Summary of changes

| Class | DCD status | Required change |
| :--- | :--- | :--- |
| `HostAction` | Matches | No changes |
| `ComboMenuItem` | Mismatch | Remove two inherited methods |
| `Customer` | Mismatch | Rename one method |
| `HappyHour` | Matches | No changes |

#### 2. `ComboMenuItem` — remove two methods
Remove `+ getPriceCents(): int` and `+ getPrepUnits(): int` from `ComboMenuItem` in the DCD because they are not overridden in `ComboMenuItem.java`—they are inherited from `MenuItem`. Keep the generalization arrow pointing to `MenuItem`.

#### 3. `Customer` — rename recovery method
Change `+ recoverSatisfaction(pts: int): void` in the DCD to:
```text
+ increaseSatisfaction(pts: int): void
```
Keep `decreaseSatisfaction(pts: int)` as well.

#### 4. `HostAction` and `HappyHour`
Both classes already match the code you provided.

#### 5. Implementation detail to verify
Remember that VIP discounts must be applied before Happy Hour, with prices locked when an order is taken in `Waiter.takeOrder()`:
```java
int price = c.calculateBill(item.getPriceCents());
price = happyHour.applyDiscount(price);
```

---

## Prompt 9 — `Menu`, `Order`, `Restaurant`, and `RegularCustomer` DCD Review

### Student Prompt
*(Pasted `Menu.java`, `Order.java`, `Restaurant.java`, and DCD blocks for `MenuItem`, `Order`, `Restaurant`, and `RegularCustomer`)*

### ChatGPT Response
You need to update the `Order` and `Restaurant` diagrams. Your `MenuItem` and `RegularCustomer` diagrams appear consistent with the code shared previously.

#### 1. `Order` — remove one method
Remove `- isDone(): boolean` from the `Order` DCD because this method does not exist in `Order.java`. Keep `billCents: int` and `Order(id: int, customer: Customer, item: MenuItem, billCents: int)`.

#### 2. `Restaurant` — five DCD corrections

| Current DCD | Change |
| :--- | :--- |
| `+ executeHostAction(action: HostAction): void` | Change return type to `Order` |
| `+ activateHappyHour(): void` | Remove |
| `+ applyHappyHourRecovery(): void` | Rename to `applyHappyHourSatisfactionRecovery()` |
| `+ endTurnHappyHour(): void` | Remove |
| `+ getPaidCustomers(): List<Customer>` | Add |

#### 3. `Menu` — keep the two new methods
Ensure `+ getItem(name: String): MenuItem` and `+ addItem(item: MenuItem): void` appear in your DCD, and update the class-level Javadoc comment to reflect the 4-item menu and odd-ID combo rule.

#### 4. Integration check
Verify that `Waiter.takeOrder()` computes the locked bill (`c.calculateBill(...)` followed by `happyHour.applyDiscount(...)`) and `Cashier.collectPayment()` returns `order.getBillCents()`.

---

## Prompt 10 — Staff Classes & `RegularCustomer` DCD Verification

### Student Prompt
*(Pasted `RegularCustomer.java`, `Cashier.java`, `Host.java`, `Waiter.java`, and DCD blocks for all four classes)*

### ChatGPT Response
All four DCD classes match your Java implementations. You don't need to change their methods or signatures.

#### 1. DCD verification

| Class | Status | Findings |
| :--- | :--- | :--- |
| `RegularCustomer` | Matches | Constructor, four overridden methods, and decay constant match |
| `Cashier` | Matches | Constructor, task description, and payment method match |
| `Waiter` | Matches | Constructor and all four methods match (`hh` can be renamed to `happyHour` for exact parameter name match) |
| `Host` | Matches | Constructor, task description, and seating method match |

#### 2. Stage 3 functionality review
* **ComboMeal selection:** `MenuItem item = c.getOrderPreference(m);` delegates food selection polymorphically to the customer.
* **Happy Hour pricing:** Customer discount is applied first (`c.calculateBill(...)`), Happy Hour discount second (`happyHour.applyDiscount(...)`), and the locked bill is saved in `Order`.
* **Cashier payment:** `Cashier` charges `o.getBillCents()` rather than recalculating the bill at payment time.

#### 3. One small `Waiter` improvement
Move `useTask()` in `Waiter.takeOrder()` to immediately before `c.setStatus(CustomerStatus.ORDERED);` and `return o;` so a failed item selection or bill calculation cannot consume the Waiter's task.

---

## Prompt 11 — Final `Waiter.java` Verification

### Student Prompt
correct now?

```java
package staff;

import entity.*;

/**
 * Seats customers, takes orders and serves READY dishes: one task per turn.
 */
public class Waiter extends Staff {
    @Override
    public String getAvailableTaskDescription() {
        return hasTaskLeft() ? "1 task available (Seat / Take Order / Serve)" : "Task completed";
    }

    public Waiter() { 
        super("Waiter", 1); 
    }

    public void seatCustomer(Customer c, Table t) {
        if (!hasTaskLeft()) throw new IllegalStateException("Waiter has already acted this turn.");
        if (c.getStatus() != CustomerStatus.WAITING_IN_QUEUE)
            throw new IllegalStateException("That customer is not waiting in the queue.");
        if (t.isOccupied())
            throw new IllegalStateException("Table " + t.getTableNumber() + " is occupied.");
        useTask();
        t.seatCustomer(c);
        c.setStatus(CustomerStatus.SEATED);
    }

    public Order takeOrder(Table t, Menu m, int id, HappyHour happyHour) {
        if (!hasTaskLeft()) throw new IllegalStateException("Waiter has already acted this turn.");
        Customer c = t.getCustomer();
        if (c == null || c.getStatus() != CustomerStatus.SEATED)
            throw new IllegalStateException("There is no seated customer at that table waiting to order.");
        MenuItem item = c.getOrderPreference(m);
        int priceAfterCustomer = c.calculateBill(item.getPriceCents());
        int lockedPrice = happyHour.applyDiscount(priceAfterCustomer);
        Order o = new Order(id, c, item, lockedPrice);
        useTask();
        c.setStatus(CustomerStatus.ORDERED);
        return o;
    }

    /** Serves an order that was already READY before the Waiter acted this turn. */
    public void serveOrder(Order o, int turn) {
        if (!hasTaskLeft()) throw new IllegalStateException("Waiter has already acted this turn.");
        if (!o.isReady())
            throw new IllegalStateException(o.getItem().getName() + " is not READY yet.");
        useTask();
        o.setStatus(OrderStatus.SERVED);
        o.getCustomer().setStatus(CustomerStatus.SERVED);
        o.getCustomer().setTurnServed(turn);
    }
}
```

### ChatGPT Response
Yes. Your updated `Waiter.java` is now correct for Stage 3, based on the other classes you've shared. You have also fixed the earlier issue where `useTask()` was called before the order was successfully created.

#### Code review

| Component | Status | Explanation |
| :--- | :--- | :--- |
| `seatCustomer()` | Correct | Validates customer and table before consuming task |
| `takeOrder()` | Correct | Uses polymorphism to select food |
| `ComboMeal` | Correct | Calls `c.getOrderPreference(m)` |
| `VIP discount` | Correct | Applies customer-specific discount first |
| `Happy Hour` | Correct | Applies 20% discount after customer discount |
| `Locked price` | Correct | Stores final price in `Order.billCents` |
| `Task consumption` | Correct | Consumes task after order creation |
| `serveOrder()` | Correct | Checks `READY` status before serving |

#### DCD verification
Your current DCD entry remains correct:
```text
+ takeOrder(t: Table, m: Menu, id: int, happyHour: HappyHour): Order
```

#### Conclusion
Your `Waiter` implementation is ready for integration testing. The next step is running the game to verify that odd-ID regular customers receive the `$24.30` combo, that the Chef requires three preparation units, and that Happy Hour discounts remain locked until payment.
