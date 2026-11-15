# Index
1. [Overview](https://github.com/jennaoh/sc2002-restaurant-rush/tree/main#overview)
   - [Stage Transitions](https://github.com/jennaoh/sc2002-restaurant-rush/tree/main#stage-transitions)
     - [Stage 1 -> 2](https://github.com/jennaoh/sc2002-restaurant-rush/tree/main#stage-1---stage-2)
     - [Stage 2 -> 3](https://github.com/jennaoh/sc2002-restaurant-rush/tree/main#stage-2---stage-3)
2. [How to Compile & Run](https://github.com/jennaoh/sc2002-restaurant-rush/tree/main#how-to-compile--run)

# Overview
Restaurant Rush game developed in Java. It is split into three stages.

# Repository
**GitHub Repository:** https://github.com/jennaoh/sc2002-restaurant-rush
## AI Conversation Histories
Each member's AI-assisted development history is stored in their respective GitHub branch.
| Member | AI History |
|---|---|
| Jenna | https://github.com/jennaoh/sc2002-restaurant-rush/blob/jenna-stage3-docs/ai-history/stage%203/chat-01.md |
| Haziq | https://github.com/jennaoh/sc2002-restaurant-rush/blob/hz-branch/ai-history/misc/chat-01.md <br> https://github.com/jennaoh/sc2002-restaurant-rush/blob/hz-branch/ai-history/stage-3/chat-01.md |
| Hui Shin | https://github.com/jennaoh/sc2002-restaurant-rush/blob/huishin-branch/ai-history/misc/chat-01.md |
| Rachel | https://github.com/jennaoh/sc2002-restaurant-rush/blob/e600b5b454f72c90799641d59f06bf0f69d3c4c9/ai-history/sequencediag-chat.md |

## Presentation Video

**Video Link:** INSERT_LINK

| Timestamp | Content | Presenter |
|---|---|---|
| 00:00–02:00 | Game overview and three stages | Haziq |
| 02:00–06:00 | Gameplay demonstration and outcomes | Jenna |
| 06:00–10:00 | Class diagrams and design evolution | Hui Shin |
| 10:00–13:00 | Sequence diagram and selected use case | Rachel |
| 13:00–15:00 | Individual contributions and reflections | All members |

| Stage / Git-Tag | Description |
| ------------- | ------------- |
| [stage-1](https://github.com/jennaoh/sc2002-restaurant-rush/releases/tag/stage-1)  | Basic game operation, implementing regular customers, menu, waiter, chef, cashier, full turn sequence, win and losing conditions|
| [stage-2](https://github.com/jennaoh/sc2002-restaurant-rush/releases/tag/stage-2) | Introduces new customer types and behaviour, adding VIP & critic customers  |
| [stage-3](https://github.com/jennaoh/sc2002-restaurant-rush/releases/tag/stage-3)  | Introduces host role, combo meals & happy hour feature|

## Stage Transitions
### Stage 1 -> Stage 2
[GitHub Compare Link](https://github.com/jennaoh/sc2002-restaurant-rush/compare/stage-1...stage-2)

### Changes & Reasoning:

**Added Files/Classes:**
- CriticCustomer.java
- RegularCustomer.java
- VipCustomer.java

The above classes was added to accommodate the new customer types (VIP & Critic)

**Classes Changed:**
- ConsoleUI.java
   - Edited to print customer type to player
- GameController.java
   - Edited spawnCustomer function to spawn the new customer types
- Customer.java
   - Changed to an abstract class and become a blueprint for the new customer type classes
- Restaurant.java
   - Removed SATISFACTION_LOSS variable which is now handled by each customer type getDecayRate() method
- Table.java
   - Edited toString() method to include customer type
- Cashier.java
   - Changed to use customer's calculateBill(basePriceCents) method as VIP customer will have a discount
- Waiter.java
   - Changed to use customer's getOrderPreference() method as customers(critics) have order preferences

No classes/files were deleted. Any other classes not mentioned above remain unchanged from Stage 1.

-----------

### Stage 2 -> Stage 3
[GitHub Compare Link](https://github.com/jennaoh/sc2002-restaurant-rush/compare/stage-2...stage-3)

### Changes & Reasoning:
**Added Files/Classes:**

- HostAction.java
- ComboMenuItem.java
- HappyHour.java
- Host.java

The above classes was added to accommodate the new host role, new combo menu item and happy hour feature.

The files below were added purely for GitHub purpose and has no relation to the game.
- .gitignore
- README.md

**Classes Changed:**
- ConsoleUI.java
   - Added Happy Hour active indicator
   - Added promptHostAction() method to ask user for host action
   - Added promptActivateHappyHour() method to ask user whether they want to activate Happy Hour or not
- GameController.java
   - Add Happy Hour logic (e.g: calling ConsoleUI to print it, activating it inside Restaurant class)
   - Add handleHostAction() method to call ConsoleUI's promptHostAction() and handle any invalid inputs
- Customer.java
   - Add increaseSatisfaction() method as Happy Hour will increase customer satisfaction
- Menu.java
   - Add getItem() and addItem() methods to facilitate creation of Combo Meal
- Order.java
   - Formatting changes
   - Add billCents variable to lock bill at time of order
- RegularCustomer.java
   - Add rule that regular customers with odd IDs will order the new Combo Meal
- Restaurant.java
   - Add new Combo Meal to the Menu
   - Add methods for Happy Hour; getHappyHour() & applyHappyHourSatisfactionRecovery()
   - Add methods for new Host staff; getHost() & executeHostAction(HostAction action)
- Waiter.java
   - Add logic to apply VIP customer discount first before Happy Hour discount
- Cashier.java
   - Reverted back to returning order's getBillCents() instead of using customer's calculateBill in Stage 2 as the price is now calculated by the Waiter class which takes into consideration VIP discount and Happy Hour discount. Waiter then creates a order with the calculated price so order getBillCents() will already have the price after discounts.

The classes below was marked as changed on GitHub's compare probably due to how we committed separate commits on Stage 2 & 3 to print customer type for the user.
In actual fact, there is no change as Stage 2 should already have had these changes made.
- Table.java
   - Edited toString() method to include customer type
- VipCustomer.java
   - Changed "Vip" to "VIP" in getCustomerType()

# How to Compile & Run
```bash
# Compiles all and outputs compiled .class into out folder
javac -d out -sourcepath src src/Main.java
# Run the game from Main
java -cp out Main
```
