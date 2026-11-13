# Index
1. [Overview](https://github.com/jennaoh/sc2002-restaurant-rush/edit/main/README.md#overview)
   - [Stage Transitions](https://github.com/jennaoh/sc2002-restaurant-rush/edit/main/README.md#stage-transitions)
     - [Stage 1 -> 2](https://github.com/jennaoh/sc2002-restaurant-rush/edit/main/README.md#stage-1---stage-2)
     - [Stage 2 -> 3](https://github.com/jennaoh/sc2002-restaurant-rush/edit/main/README.md#stage-2---stage-3)
2. [How to Compile & Run](https://github.com/jennaoh/sc2002-restaurant-rush/edit/main/README.md#how-to-compile--run)

# Overview
Restaurant Rush game developed in Java. It is split into three stages.

| Stage / Git-Tag | Description |
| ------------- | ------------- |
| [stage-1](https://github.com/jennaoh/sc2002-restaurant-rush/releases/tag/stage-1)  | Basic game operation, implementing regular customers, menu, waiter, chef, cashier, full turn sequence, win and losing conditions|
| [stage-2](https://github.com/jennaoh/sc2002-restaurant-rush/releases/tag/stage-2) | Introduces new customer types and behaviour, adding VIP & critic customers  |
| [stage-3](https://github.com/jennaoh/sc2002-restaurant-rush/releases/tag/stage-3)  | Introduces host role, combo meals & happy hour feature|

## Stage Transitions
### Stage 1 -> Stage 2
[GitHub Compare Link](https://github.com/jennaoh/sc2002-restaurant-rush/compare/stage-1...stage-2)

**Changes:**
text

### Stage 2 -> Stage 3
[GitHub Compare Link](https://github.com/jennaoh/sc2002-restaurant-rush/compare/stage-2...stage-3)

**Changes:**
text

# How to Compile & Run
```bash
# Compiles all and outputs compiled .class into out folder
javac -d out -sourcepath src src/Main.java
# Run the game from Main
java -cp out Main
```
