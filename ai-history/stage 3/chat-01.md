# SC2002 Restaurant Rush — AI Conversation History

## Stage 3: Host, ComboMeal, Happy Hour and UML Design Review

**AI Tool:** ChatGPT (OpenAI)  
**Project:** SC2002 Restaurant Rush  
**Stage:** Stage 3  
**Purpose:** Java debugging, implementation review, and UML Design Class Diagram verification

---

## Conversation 1 — Restaurant.java Compilation Error

**Student:**

Why is there an error in Restaurant.java?

Error:
```text
The method getItem(String) is undefined for the type Menu
The method getItem(String) is undefined for the type Menu
The method addItem(ComboMenuItem) is undefined for the type Menu
```

The Menu class currently contains getItems() and selectItem(Customer c), but no getItem() or addItem() methods.

**ChatGPT:**

The errors occur because Restaurant.java calls methods that are not defined in Menu.java.

Suggested adding:
```java
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
```

Also explained that adding a combo changes the number of menu items from three to four, affecting deterministic selection.

---

## Conversation 2 — Following the Design Class Diagram

**Student:**

But I have to follow my class diagram.

**ChatGPT:**

Initially suggested removing the combo initialization from Restaurant.java to preserve the existing DCD, because getItem() and addItem() were not shown in Menu.

Also identified potential mismatches between Restaurant.java and the DCD.

**Student:**

I can also change the DCD.

**ChatGPT:**

Recommended keeping ComboMenuItem functionality and updating the DCD to reflect the actual implementation.

The Menu class diagram should include:
```text
+ Menu()
+ selectItem(c: Customer): MenuItem
+ getItems(): List<MenuItem>
+ getItem(name: String): MenuItem
+ addItem(item: MenuItem): void
```

ComboMenuItem should inherit from MenuItem, and Restaurant's executeHostAction() return type should match the implemented code.

---

## Conversation 3 — ComboMeal Selection

**Student:**

I am doing Stage 3. I have completed Host and am implementing ComboMeal. Odd customer IDs should take the combo.

Shared Menu.java and RegularCustomer.java.

**ChatGPT:**

Reviewed the odd-ID selection rule and confirmed that RegularCustomer.getOrderPreference(Menu) searches for ComboMenuItem.

Explained the expected combo calculation:
```text
Salad = $9.00, 1 preparation unit
Pasta = $18.00, 2 preparation units

Combo price = ($9.00 + $18.00) × 0.90 = $24.30
Preparation units = 1 + 2 = 3
```

Recommended checking whether Waiter.takeOrder() calls Customer.getOrderPreference() rather than Menu.selectItem() directly.

Noted that the odd-ID rule applies only to RegularCustomer unless VIP and Critic implement the same rule.

---

## Conversation 4 — Waiter Polymorphism

**Student:**

Shared the current Waiter.java implementation.

**ChatGPT:**

Confirmed that:
```java
MenuItem item = c.getOrderPreference(m);
```

correctly uses polymorphism.

Recommended moving useTask() after food selection and order creation so an invalid operation does not consume the Waiter's task.

---

## Conversation 5 — TAKE_ORDER Compilation Errors

**Student:**

Received:
```text
The method getTableNumber() is undefined for the type WaiterAction
Duplicate local variable order
```

**ChatGPT:**

Explained that WaiterAction provides getTargetId(), not getTableNumber().

Also explained that the TAKE_ORDER switch case lacked braces, causing a duplicate local variable declaration.

Suggested:
```java
case "TAKE_ORDER": {
    Table table = findTableByNumber(targetId);

    if (table == null) {
        throw new IllegalArgumentException(
            "Table " + targetId + " does not exist."
        );
    }

    Order order = waiter.takeOrder(
        table, menu, nextOrderId, happyHour
    );

    order.getCustomer().assignOrder(order);
    orders.add(order);
    nextOrderId++;

    return order;
}
```

---

## Conversation 6 — ConsoleUI and GameController DCD

**Student:**

Shared ConsoleUI.java, GameController.java and their DCD entries. Asked whether the class diagram needed changes.

**ChatGPT:**

Identified three corrections:

```text
ConsoleUI:
+ promptActivateHappyHour(): boolean

GameController:
- handleHostAction(): void
```

Remove the nonexistent:
```text
- handleHappyHourChoice(): void
```

Also recommended correcting the invalid-choice messages in the Waiter and Host menus.

---

## Conversation 7 — HostAction, ComboMenuItem, Customer and HappyHour

**Student:**

Shared implementations and DCD entries for HostAction, ComboMenuItem, Customer and HappyHour.

**ChatGPT:**

Confirmed HostAction and HappyHour matched their DCD definitions.

Recommended removing getPriceCents() and getPrepUnits() from the ComboMenuItem DCD because those methods are inherited from MenuItem.

Recommended changing:
```text
+ recoverSatisfaction(pts: int): void
```
to:
```text
+ increaseSatisfaction(pts: int): void
```

Explained that VIP discounts must be applied before Happy Hour discounts.

---

## Conversation 8 — Menu, Order and Restaurant DCD

**Student:**

Shared Menu.java, Order.java, and DCD entries for MenuItem, Order, Restaurant and RegularCustomer.

**ChatGPT:**

Recommended removing isDone() from the Order DCD because it was absent from the implementation.

Recommended updating Restaurant's DCD:
```text
+ executeHostAction(action: HostAction): Order
+ getHappyHour(): HappyHour
+ applyHappyHourSatisfactionRecovery(): void
+ getPaidCustomers(): List<Customer>
```

Recommended removing nonexistent activateHappyHour() and endTurnHappyHour() methods from Restaurant's DCD.

Explained that Order.billCents stores the final locked bill and Cashier should collect this value rather than recalculating the price.

---

## Conversation 9 — Staff Class Review

**Student:**

Shared RegularCustomer.java, Cashier.java, Host.java and Waiter.java, together with their DCD entries.

**ChatGPT:**

Confirmed the class method signatures were consistent with the provided diagrams.

Verified the intended pricing sequence:
```java
int priceAfterCustomer = c.calculateBill(item.getPriceCents());
int lockedPrice = happyHour.applyDiscount(priceAfterCustomer);
Order o = new Order(id, c, item, lockedPrice);
```

Confirmed Cashier uses:
```java
return o.getBillCents();
```

Recommended moving useTask() to after successful order creation.

---

## Conversation 10 — Final Waiter Verification

**Student:**

Shared the updated Waiter.java and asked whether it was correct.

**ChatGPT:**

Confirmed that Waiter.takeOrder() now creates the order before consuming the task.

Verified that the method:
- Delegates menu selection to Customer.getOrderPreference().
- Applies customer-specific discounts first.
- Applies Happy Hour discount second.
- Stores the locked price in Order.
- Consumes the Waiter's task after order creation.

Suggested an optional null check for the selected MenuItem.

Recommended integration tests for combo selection, three-unit preparation, and locked Happy Hour pricing.

---
