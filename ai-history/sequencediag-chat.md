# SC2002 Restaurant Rush — AI Conversation History

## Sequence Diagram

**AI Tool:** Claude (Anthropic)

**Project:** SC2002 Restaurant Rush

**Purpose:** UML sequence diagram queries and review 

**Link 1:** https://claude.ai/share/d4156c2c-b44b-4d22-9545-0a379c47c68a

**Link 2:** https://claude.ai/share/129f945a-e58c-4859-ac15-379a8e27eefb

---

## Conversation 1 (Link 1) — Loop and Turn Representation

**Q1. Student:**
Uploaded the draw.io sequence diagram (XML) -- initial draft 
if i have a loop in UML diagram for while turn<=18, do i have to repeat the steps for every turn in UML sequence diagram?

**AI:**
No, you don't need to repeat the steps for every turn. That's the whole point of a loop fragment: it shows the steps **once**, and the guard condition (`[currentTurn <= MAX_TURNS (18)]`) tells the reader "this body runs again for each turn while the condition holds."

Your diagram already does this well. The loop frame contains one generic iteration (Turn 7 as the example), and the note at the top says which turns are shown.

**What's good practice**

- Draw the loop body once, using generic names where you can (`startTurn(currentTurn)` rather than `startTurn(7)`).
- Put the guard condition on the frame, as you've done.
- Use a note to say what's being illustrated if you pick a concrete example turn.
- Show only the variations that matter, using `alt`, `opt`, or `break` fragments inside the loop.

**Two things you could tighten in your diagram**

1. **Generic vs. concrete labels.** Your messages use `startTurn(7)`, `spawnCustomer(7)`, `executeWaiterAction(SEAT,7)` and so on. Using `currentTurn` (or `t`) would make it read as "any turn," with a note saying "example: turn 7." Concrete values are fine if you state that clearly, which you mostly do.

2. **Turn 8 and beyond.** You've started drawing Turn 8 (messages 57 onward). That's the part that is redundant. Since the loop frame already implies repetition, you only need to draw a second iteration if it shows something *different* from the first, such as the vip being seated and `c2` finishing eating, which is a genuinely different path. If the later turns just repeat the same message sequence, stop and rely on the loop. If your assignment requires showing the consequences of Choice A vs. B, a short note summarizing the outcome ("Turns 8 to 18 follow the same cycle; c2 finishes eating, vip is seated…") is usually better than redrawing everything.

**Rule of thumb:** a sequence diagram shows *one representative scenario*, not a full execution trace. You draw extra iterations only when the behavior differs, and otherwise the loop fragment plus a note covers it.

One caution: check your own course or assignment requirements. Some instructors ask for a specific scenario traced across several turns, in which case drawing a couple of iterations is expected. If they didn't say that, one iteration inside the loop is the standard answer.
