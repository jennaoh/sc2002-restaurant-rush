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
| NAME | LINK_HERE |

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
