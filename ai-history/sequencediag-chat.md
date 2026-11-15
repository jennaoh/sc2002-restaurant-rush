# SC2002 Restaurant Rush — AI Conversation History

## Sequence Diagram

**AI Tool:** Claude (Anthropic)

**Project:** SC2002 Restaurant Rush

**Purpose:** UML sequence diagram queries and review 

**Link 1:** https://claude.ai/share/d4156c2c-b44b-4d22-9545-0a379c47c68a

**Link 2:** https://claude.ai/share/129f945a-e58c-4859-ac15-379a8e27eefb

---

## Conversation 1 (Link 1) — Loop and Turn Representation

---

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

---

**Q2. Student:**

is the current one valid to be as a representative scenario for other turns

**AI:**

Partly. Turn 7 is a good representative of an arrival turn where the player faces a conflict, but it isn't representative of an arbitrary turn. Turn 7 is special in three ways.

**1. It's an arrival turn, and arrivals only happen on some turns.**
Your own note says arrivals occur on turn 1 and on turns 4, 7, 10, 13 and 16. In the loop body, `《create》VipCustomer` and `addWaitingCustomer` (messages 6-7) look unconditional. Wrap them in an `opt [isArrivalTurn(currentTurn)]` fragment. If regular and VIP arrivals differ, add an `alt` inside it (`[VIP turn] create VipCustomer` / `[else] create RegularCustomer`).

**2. Step 4 shows nothing happening.**
`updateEatingCustomers` and `processCashierPayment` are called, but in turn 7 nothing changes, as your note says. A reader never sees eating finishing, a customer becoming ready to pay, or revenue being added. Those are core behaviors, so add `opt` fragments such as `[customer finished eating] setStatus(READY_TO_PAY)` and `[cashier takes payment] ...`. You can leave them as "not taken in turn 7", like you did with the abandonment `opt`. Without them, the only way to show those behaviors is to draw turn 8 or later, which is the redundancy you're trying to avoid.

**3. Step 5 and the chef step are also turn-specific.**
- `displayDepartures` (messages 54-55) should sit in an `opt [abandonedList not empty]` rather than being skipped via a note.
- The chef only shows `WAIT`. Either add the "Prepare Order" branch as an `alt`, or add a note saying the other choices follow the same pattern as the waiter's.

**Smaller issues**
- Message 13 says `o3 = READY`, but your note says o2 is the ready order. That looks like a typo.
- The loop never shows `currentTurn++`, so the guard `[currentTurn <= 18]` has nothing that advances it. Add that message at the end of the body.
- Many labels use the literal `7` (`startTurn(7)`, `executeWaiterAction(SEAT,7)`). Use `currentTurn` there, and keep "example: turn 7" in the note.
- The top note says "shown below for turns 7 to 12", which contradicts the idea of one representative iteration. Reword it to match what you actually draw.

If you make those changes, one iteration plus the turn 7 example is enough, and you can drop the turn 8 section or replace it with a short note. I could only check the diagram's structure from the XML, not how it renders, so give the layout a visual pass after editing.

---

**Q3. Student:**

but i want to show the vip customer behaviour from arrival to payment, which is turn 8 to 11, how do i do that with turn 7 as the representative turn

**AI:**

Don't ask turn 7 to represent the VIP's whole journey. Split the job in two: the loop body shows what can happen in any turn, and a separate trace shows what happens to the VIP in turns 7 to 11.

**Layer 1: a generic loop body**

Make the loop body cover every step with `opt`/`alt` fragments, using `currentTurn` instead of literal numbers. This is what lets one iteration stand for all 18 turns. Each branch only needs to appear once:

- `opt [isArrivalTurn]` spawn and queue the customer
- `alt` waiter choice: seat / take order / serve / wait
- `alt` chef choice: prepare / wait
- `opt [customer finished eating]` set READY_TO_PAY
- `opt [cashier paid]` payment and revenue
- `opt [satisfaction == 0]` abandon

You can label the branches with the VIP's events (for example, "e.g. turn 8: seat vip") so readers can connect them to Layer 2.

**Layer 2: a VIP trace for turns 7 to 11**

Show only the messages that involve `vip`, one block per turn, rather than redrawing the full turn each time. There are three ways to do it, from cheapest to most formal:

1. **Reuse your dotted turn separators.** Use the dotted separator you already use between Choice A and B, labelled "Turn 8", "Turn 9", and so on. Under each, draw only the VIP-related messages (seat, take order, serve, eat, pay) and the vip's `setStatus(...)` calls. Add a note: "Other actions in each turn follow the loop body above."
2. **Use `ref` fragments.** Inside the loop, add `ref` boxes named "Turn 8: seat VIP", "Turn 9: take order" and so on, each pointing to a small sequence diagram drawn separately. This is the standard UML way to avoid repetition, and it works well if you want each turn kept small.
3. **Add a status table next to the trace.** A small table of turn, VIP status, satisfaction and action (WAITING_IN_QUEUE → SEATED → ORDERED → SERVED → READY_TO_PAY → PAID) lets you drop most of the messages for turns 8 to 11. I can't tell which turn each transition happens in under your rules, so fill that in from your code.

**A good supplement**

If the requirement is "show the VIP's behaviour from arrival to payment", a **state machine diagram** for `VipCustomer` shows it more directly than a sequence diagram: states for each status and transitions labelled with the triggering message. The sequence diagram then only has to show *who sends what* on each turn.

**Suggested approach**

Keep turn 7 as the detailed generic body (with the fixes I listed earlier), then use option 1 or 2 for turns 8 to 11, showing only the VIP-related messages. Drop the full redraw of turn 8 you started (messages 57 onward) in favour of that compact version.

Check your assignment wording. If it says to trace one customer across turns, a compact multi-turn trace like this is expected. If it just says "sequence diagram for the feature", the generic loop plus one trace is plenty.

---

**Q4. Student:**

here is what is required One complete scenario in which a manager choice changes an outcome.
Include: customer arrival and order; Waiter and Chef assignments; READY then SERVED; eating; payment; the result check; one Stage 2 or 3 behaviour (e.g. VIP pricing or Happy Hour).
Show messages among the CLI, controller, staff and domain objects, with relevant guards and loops.

**AI:**

That wording changes my advice. It asks for **one complete scenario**, so you don't need the generic loop body with `opt`/`alt` for every possible branch that I suggested earlier. Concrete turn numbers and values are what the assignment wants. The only generic parts are the game loop and the re-prompt loop.

**How your diagram scores against the requirements**

| Requirement | Status |
|---|---|
| Manager choice changes outcome | Turn 7 A/B exists, but the difference must be carried through to the final outcome |
| Arrival and order | Arrival is shown, but the VIP's order-taking is not (`takeOrder`, new Order, discounted price) |
| Waiter and Chef assignments | The waiter is shown, but the chef only does `WAIT`. It needs a "Prepare Order" for the VIP's order |
| READY then SERVED | Shown for o2 only, not for the VIP's order |
| Eating | Missing. Turn 7 shows `updateEatingCustomers` with no effect |
| Payment | Missing. It needs the cashier and the paid amount |
| Result check | Missing. Only `displayTurnSummary` exists, and nothing happens after the loop ends |
| Stage 2/3 behaviour | VIP creation is shown, but VIP pricing (10% discount) and the decay rate of 5 are not yet used |
| CLI, controller, staff, domain messages | Already covered well |

**What to do**

1. **Keep turn 7 as the decision point.** Your A/B `alt` is exactly "a manager choice changes an outcome."
2. **Continue only Choice A through turns 8 to 11, showing only the messages that matter.** Use your dotted separators labelled "Turn 8", "Turn 9" and so on, plus a note saying "other actions follow the same cycle." Cover, in your code's actual order:
   - the waiter seats the VIP
   - the waiter takes the order, creating the Order with VIP pricing
   - the chef prepares the order, then it becomes READY
   - the waiter serves it
   - eating finishes and the customer becomes ready to pay
   - the cashier processes payment with the 10% discount and revenue is added

   I can't tell which turn each step lands on under your rules, so take that from your code.
3. **Add the result check after the loop frame.** After the loop exits, show `GameController → Restaurant` calling something like `getResult()` and `ui.displayFinalResult(...)`, with an `alt` if there are win/lose thresholds. Turns 12 to 18 can be a note, such as "turns 12 to 18 omitted (same cycle)."
4. **Make the choice matter at the end.** Put a note at the result check comparing the outcomes. For example: "Choice A: revenue X, avg satisfaction Y; Choice B: revenue X', avg satisfaction Y'". Compute these from your code. Without this, the choice only visibly changes turn 7.
5. **Put guards on the repeated parts:** the per-customer loops in `updateEatingCustomers` and `applySatisfactionDecay`, and `opt` fragments for payment and abandonment.

If the diagram gets too large, move turns 8 to 11 into `ref` fragments, which also keeps each part readable.

---

**Q5. Student:**

since turn 7 choice A is chosen, turn 8: waiter seats the vip and c2 finishes eating, turn 9: order taken for vip, chef prepares in the same turn, c2 pays. Turn 10: vip order ready and served. Turn 11: vip finish eating. Turn 12: vip pays with vip pricing

**AI:**

That timeline fits the rules in your diagram, so you can draw it as five short blocks after turn 7. Each block shows only the messages that matter, under the dotted turn separators you already use. Step 1 of the turn flow (arrivals) can be skipped for turns 8 to 12, since turn 10 is the next arrival turn and the VIP is not involved. Say so in a note.

**Turn 8: seat VIP, c2 finishes eating**
- Waiter step: you already drew this (messages 58-66: `SEAT 3` → `seatCustomer(vip, t1)` → `waitingQueue.remove(vip)`). Keep it.
- Eating step: `updateEatingCustomers(8)` → `c2.isFinishedEating(8)` → `c2.setStatus(READY_TO_PAY)`, wrapped in a per-customer `loop` with an `opt [served before this turn]`. Your note on turn 7 already explains why c2 only finishes now.
- Payment step: nothing happens, because c2 only becomes ready this turn. That is why it pays in turn 9. Put a note on this.

**Turn 9: take VIP's order, chef prepares, c2 pays**
- Waiter: `TAKE_ORDER, 3` → `executeWaiterAction` → `new Order(salad)` for the VIP, created with the vip and the salad price. Show `vip.setStatus(ORDERED)`.
- Chef: `PREPARE, o3` → `executeChefAction` → `o3.setStatus(READY)`. Add a note: "salad needs 1 prep unit, so it is done this turn" (check the actual number in your code). Waiter before chef in the step order is what makes same-turn preparation possible.
- Cashier: `processCashierPayment()` → `c2.pay()` at **regular price ($18.00)**, `revenue += 18`, c2 status PAID, table 2 freed. Showing a regular payment here sets up the VIP contrast in turn 12.

**Turn 10: VIP order ready and served**
- Waiter: `SERVE, orderId=3` → `o3.isReady()` → `serveOrder(o3, 10)` → `o3.setStatus(SERVED)` and `vip.setStatus(SERVED)`, `setTurnServed(10)`. This is the same pattern as your Choice A messages 17-29, so you can copy them.

**Turn 11: VIP finishes eating**
- `updateEatingCustomers(11)` → `vip.setStatus(READY_TO_PAY)`. The condition `10 < 11` is true here, unlike turn 7.

**Turn 12: VIP pays with VIP pricing**
- Cashier: `processCashierPayment()` → `vip.calculatePrice(salad price)`, which applies the 10% discount, → `revenue += discounted amount`, VIP status PAID, table freed.
- Add a note with the numbers: "VIP pays [salad price] × 0.90 = $X", using your actual salad price. This is your Stage 2 behaviour, so give it a highlighted note.
- Optionally show the `satisfaction` value at payment, since it feeds the average in the result check.

**After the loop**
- Add a note: "Turns 13 to 18 omitted (same cycle; no VIP involvement)", if that's true in your code.
- Then add the result check as a separate block after the loop frame: `getFinalResult()` → `displayFinalResult(...)`, ideally with an `alt` for your win/lose threshold.

**Making the choice matter**
At the result check, add a note comparing outcomes:
- **Choice A (chosen):** c2 pays in turn 9 at satisfaction 76, and the VIP is seated in turn 8.
- **Choice B:** c2 drops to 68 and its payment is delayed, and the VIP is seated a turn earlier, so the VIP's later turns shift one turn earlier.

I can't tell from the diagram what final revenue or average satisfaction each path gives. Run both in your code and put the real numbers in the note, because that comparison is what shows the manager choice changing the outcome.

For layout, delete the half-drawn turn 8 header and messages from 57 onward, and redo turns 8 to 12 compactly. Keep one `loop`/`alt` frame only where something actually repeats (the per-customer loops in eating and decay). If the page gets too long, put each turn in a `ref` fragment.

---

**Q6. Student:**

but is 58-66 not a repeat of what is in turn 7 (while loop exists for all turns)

**AI:**

Partly, yes. Messages 58-66 are the same *kind* of message sequence as 30-38 in Choice B (`SEAT, 3` → `executeWaiterAction(SEAT)` → `findWaitingCustomerByid(3)` → `seatCustomer(vip, t1)` → `useTask` → `setStatus(SEATED)` → `waitingQueue.remove(vip)`). Drawing it twice adds nothing, and the loop fragment already implies "this runs again next turn."

But it isn't redundant in the way you might think, because of how the scenario works:

- **In turn 7 you drew both options as alternatives.** Choice A (serve) is the path you took. Choice B (seat the VIP) is the path you did *not* take, and it only appears as a branch inside the `alt`.
- **In turn 8 the VIP is seated for real**, on the path you chose. So the seating belongs to the main scenario, but it is identical in structure to the Choice B branch.

**Fix: don't redraw it. Reference it.**

Turn 8 has two options:

1. **Delete 57-66 and use a note.** Put a short note under a "Turn 8" separator: "Waiter seats vip: same message sequence as Choice B (messages 30-38)." Then draw only what is new in turn 8: `updateEatingCustomers(8)` → `c2.setStatus(READY_TO_PAY)`.
2. **Use a `ref` fragment.** Draw a `ref` box labelled "Seat VIP (messages 30-38)" in turn 8. This is the formal UML way to say "same interaction as before," and it keeps the main diagram short.

Either way, the message numbering should skip the repeated block rather than reusing it.

**Same rule for later turns**

Apply this test to everything in turns 9 to 12: if it's the same message pattern as something already drawn, reference it instead of redrawing it.

- Turn 10 `SERVE, o3` repeats Choice A messages 17-29, so use a note or `ref`.
- Turn 11 and turn 12 eating and payment are new, because turn 7 shows no effect there. Draw these.
- Turn 9 `TAKE_ORDER` and `PREPARE` are new, because turn 7 only shows `WAIT` for the chef. Draw these.

So what you draw in full is whatever appears for the first time in the scenario: the order being taken, the chef preparing, eating finishing, regular payment, and VIP payment. Anything already shown gets a one-line reference.

---

**Q7. Student:**

but 57-66 is what happen for every turn, there should be a currentTurn++ after each turn, how do i show that without the whole diagram being redundant

**AI:**

You're right: `startTurn`, the step banners, `displayTurnStart`, `displayTurnSummary` and `currentTurn++` happen every turn, so they belong in the loop body once and nowhere else.

**1. Show `currentTurn++` once, at the end of the loop body**
- Add a self-message on `gc:GameController` after `displayTurnSummary(r, 7)` (message 55), for example **`currentTurn++`** (same style as your message 3, `currentTurn = 1`).
- Keep the guard `[currentTurn <= MAX_TURNS (18)]` on the loop frame.
- Add a note beside it: "Loop repeats from startTurn(currentTurn). Turn 7 is drawn in full as the representative iteration."
- That one message and the frame tell the reader every turn follows the same skeleton.

**2. Delete the turn 8 iteration you started (messages 57 onward)**
The `startTurn(8)` message, the "Turn 8" banner and the repeated seating messages are the redundant part. The loop frame already covers them.

**3. Show only the scenario differences for turns 8 to 12**
These are the parts that aren't the same skeleton, and your requirement says the scenario needs them: order taken, chef preparing, eating, payment. Draw them without the surrounding turn skeleton:

- Use one dotted separator per turn, labelled "Turn 8", "Turn 9" and so on.
- Under each, draw only the scenario-specific messages. Do not draw `startTurn`, `displayTurnStart`, `displayTurnSummary`, or the arrival, kitchen and decay steps when nothing relevant happens in them.
- Add one note at the top of this section: "Each turn runs the same loop body as turn 7. Only the messages that differ for the VIP scenario are shown."

What to draw per turn:

| Turn | Draw | Skip |
|---|---|---|
| 8 | `c2` → READY_TO_PAY (eating step) | The VIP seating, which is identical to Choice B (use a note: "same as messages 30-38") |
| 9 | `TAKE_ORDER` for the VIP, chef `PREPARE`, cashier payment for c2 | Everything else |
| 10 | `SERVE o3` (note: "same as messages 17-29") | Everything else |
| 11 | VIP → READY_TO_PAY | Everything else |
| 12 | VIP payment with the 10% discount (highlighted note) | Everything else |

**4. Put the result check after the loop frame**
Turns 13 to 18 get a note ("same loop, no VIP involvement"), then the result check and the A vs B comparison.

**A more formal alternative: `ref` fragments**
Leave the loop body for turn 7 as it is, then add `ref` boxes inside the loop labelled "Turn 8: see Turn 8 diagram" through "Turn 12". Draw each turn's differences as a small separate sequence diagram. This is the standard UML way to avoid redrawing the skeleton, but it means more diagrams to maintain.

---

**Q8. Student:**

show it as a diagram

**AI:**

Here is the structure: the skeleton is drawn once, and only the scenario differences are drawn for turns 7 to 12.The diagram shows the three layers of your sequence diagram.

- **Loop skeleton (gray):** `startTurn`, steps 1 to 5, the turn summary and `currentTurn++` appear once inside the loop frame. That covers "this happens every turn", so turn 8's header and its repeated messages (57 onward) can be deleted.
- **Scenario blocks (amber and purple):** Turn 7 holds the manager choice as an `alt`. Turns 8 to 12 are drawn only with the messages that differ: seating, order and prepare, serve, finish eating, and the VIP payment. Amber marks the manager choice and the Stage 2 VIP pricing, which are the two things the assignment asks you to highlight.
- **After the loop:** the result check, plus the note comparing the outcome of choices A and B. That comparison is what shows the manager's choice changing the outcome.

In draw.io, each purple block becomes a dotted turn separator with a few messages under it, or a `ref` fragment if you want each turn kept compact. The messages inside each block are the ones I listed earlier.

---

**Q9. Student:**

UML sequence diagram

**AI:**

Here is the sequence diagram for the representative scenario. It shows the loop once, turn 7 with the manager choice, turns 8 to 12 with only their differing messages, and the result check after the loop.That's a simplified version for reference, and a few things differ from your draw.io file.

- **Left out:** the `Main`/`runGame()` startup, the step banners, the order and `c2` objects, the kitchen and decay steps, and the "invalid choice, re-prompt" loop. You can add the re-prompt loop back around the manager prompts if the marker wants it.
- **Message numbers and exact names:** I wrote these from your diagram, not from your code, so check names like `calculatePrice()`, `getResult()` and `displayFinalResult()` against your classes.
- **Choice B:** it appears only as a note, since the scenario continues down Choice A. If you want the outcome difference visible, add the real final revenue and average satisfaction for each choice to the result check, once you've run both paths.

---

**Q10. Student:**

Show only the scenario differences for turns 8 to 12
These are the parts that aren’t the same skeleton, and your requirement says the scenario needs them: order taken, chef preparing, eating, payment. Draw them without the surrounding turn skeleton:

* Use one dotted separator per turn, labelled “Turn 8”, “Turn 9” and so on.
* Under each, draw only the scenario-specific messages. Do not draw `startTurn`, `displayTurnStart`, `displayTurnSummary`, or the arrival, kitchen and decay steps when nothing relevant happens in them.
* Add one note at the top of this section: “Each turn runs the same loop body as turn 7. Only the messages that differ for the VIP scenario are shown.” // but if i only show scenario difference using every different message, do i need to draw the subsequent messages too?

**AI:**

Yes, but only for messages that are new. The rule is that each kind of message gets drawn in full the first time it appears in the diagram. After that, it collapses to one line plus a note.

"Differs" has two levels:

1. **New in kind.** The first time an interaction appears, draw its whole chain through to the state change. Drawing only the first message would leave the reader unable to see what the step does.
2. **Repeat of something already drawn.** Draw only the entry message and add a note such as "same chain as messages 17-29". Don't redraw the rest.

**What that means for turns 8 to 12**

| Turn | Interaction | Already drawn? | What to draw |
|---|---|---|---|
| 8 | Seat vip | Yes, as Choice B in turn 7 | One message, `executeWaiterAction(SEAT,8)`, plus a note "chain as in Choice B" |
| 8 | c2 finishes eating | No | Full chain: `updateEatingCustomers(8)`, then the per-customer check, then `c2.setStatus(READY_TO_PAY)` |
| 9 | Take order | No | Full chain: `executeWaiterAction(TAKE_ORDER,9)`, then `new Order(salad)`, then `vip.setStatus(ORDERED)`, then `useTask()` |
| 9 | Chef prepares | No (turn 7 only showed `WAIT`) | Full chain: `executeChefAction(PREPARE)`, then `o3.setStatus(READY)`, then `useTask()` |
| 9 | c2 pays | No | Full chain: `processCashierPayment()`, then `c2.pay()`, then revenue update, then `table.free()`, then `useTask()` |
| 10 | Serve vip's order | Yes, as Choice A in turn 7 | One message, `executeWaiterAction(SERVE,10)`, plus a note "chain as in Choice A (messages 17-29)" |
| 11 | vip finishes eating | Partly (the eating chain was drawn in turn 8) | One message, `updateEatingCustomers(11)`, plus `vip.setStatus(READY_TO_PAY)`. Skip the rest |
| 12 | vip pays with vip pricing | Chain shown in turn 9, but the price calculation is new | One message, `processCashierPayment()`, then draw `vip.calculatePrice()` in full, since the 10% discount is your Stage 2 behaviour |

I took the chains above from the structure of your diagram, so check the exact calls (`new Order`, `pay()`, `table.free()`) against your code.

**Two simplifications**

- **Return arrows.** Skip them unless they carry information, such as the `o2` returned from `serveOrder`. That shortens each chain.
- **Unchanged steps.** If a step does nothing in that turn (kitchen, decay, arrivals), draw nothing. Don't draw an empty call just to show it ran.

I'd also change the diagram I drew earlier, which abbreviated every chain to two or three messages, even the first-time ones. Take-order, prepare, c2's payment and the VIP price calculation should each get their full chain, and the repeats (seat, serve) should shrink to one message and a note. The diagram stays short and still gives the reader everything that is new.

---

**Q11. Student:**

how do i show currentTurn++ is part of the while loop in the diagram I uploaded

**AI:**

Draw it as a self-message on `gc:GameController`, placed as the last message inside the loop frame. The frame and its guard `[currentTurn <= MAX_TURNS (18)]` already imply "go back to the top", so you don't need a return arrow. A reader sees `currentTurn++` as the last step of the body, followed by the guard being checked again.

**Placement in your file**

- **Where:** immediately after message 56, the dashed return from `displayTurnSummary`, at about y=3070. This is the end of the turn 7 body and is still inside the loop frame (`loop`, x=-400 to 1840).
- **From and to:** the `gc` activation bar at x=270 (the same bar that message 3 `currentTurn = 1` uses).
- **Style:** copy message 3 or message 4 (`startTurn(7)`). They are self-messages with `sourcePoint` x=270, bend points at x=300, and `targetPoint` x=270, 30px lower. Set the label to **`57 currentTurn++`**.
- **Room:** your red "End of Turn 7" note starts at y=3100, so move that note and everything below it down by about 50px, or there won't be space for the message.

**Related clean-ups**

1. **Keep message 3 outside the loop.** `currentTurn = 1` is the initialisation, and it should sit above the loop frame as it already does. The increment goes at the end of the body inside it.
2. **Delete the turn 8 start** (message 57 `startTurn(8)` and the "Turn 8" banner). `currentTurn++` followed by the guard replaces it.
3. **Add a short note beside the self-message:** "loop returns to startTurn(currentTurn) while currentTurn <= 18". This makes the link to the loop explicit for a marker.
4. **Introduce the turn 8 to 12 blocks** with a one-line note below `currentTurn++`: "Later iterations: only scenario differences shown". Those blocks sit inside the same frame, so they read as the following iterations.
5. **Renumber the messages** after 57.

I worked this out from the coordinates in your XML, not from the rendered diagram. Look at the placement once in draw.io after you move the note.

---

**Q12. Student:**

so should it be before or after my end of turn 7 summary?

**AI:**

After the summary, and I'd put it just below the "End of Turn 7" note.

The order inside the loop body should be:

1. `displayTurnSummary(r,7)` (message 55) and its return (56)
2. The red "End of Turn 7" note
3. `currentTurn++`
4. The turn 8 separator and blocks

`displayTurnSummary` has to come first because it still reports turn 7. `currentTurn++` is the last step of the body, so nothing that belongs to turn 7 can come after it.

The note is an annotation, not a message, so its position doesn't change what the code does. It describes the state at the end of turn 7 (c2 and vip statuses and satisfaction), so it reads best before the counter changes. If `currentTurn++` sat above a note titled "End of Turn 7", the reader would see the counter at 8 next to a note about turn 7, which is a small inconsistency.

In your file, the note occupies about y=3100 to 3180, so put the self-message on the `gc` bar (x=270) at about y=3210. Move the first turn 8 separator and everything below it down accordingly.

If you'd rather keep the note as a banner at the very bottom of the turn, that also works. Just make sure `currentTurn++` is the last message drawn before the next turn's separator.

---

**Q13. Student:**

so do i need a new start turn every turn?

**AI:**

No. `startTurn(currentTurn)` is the first message of the loop body, so it appears once at the top of the loop frame, which you already have as message 4. The loop frame plus `currentTurn++` at the end tells the reader that it runs again every turn.

The turn 8 to 12 separators are labels, not new loop iterations, so they don't need their own `startTurn`. Under each separator, draw only the scenario-specific messages, as we discussed.

Two small adjustments:

- **Make message 4 generic.** Change `startTurn(7)` to `startTurn(currentTurn)` and add a note "example: currentTurn = 7". That way the single `startTurn` is clearly the one that the later turns reuse.
- **Delete message 57 `startTurn(8)`**, along with the "Turn 8" banner and the other half-drawn turn 8 messages. `currentTurn++` followed by the guard already means "start the next turn".

You only need a second `startTurn` if your marker specifically asks to see two full iterations, which this assignment doesn't.

---

**Q14. Student:**

what about the others that start turn call?

**AI:**

I assume you mean the other calls that run at the start of every turn: `spawnCustomer`, `addWaitingCustomer`, `clearNewlyReadyToPayThisTurn`, the three `resetTurn()` calls and `displayTurnStart` (messages 5 to 13). They get the same treatment as `startTurn`. Draw them once in the loop body for turn 7, and don't repeat them under the turn 8 to 12 separators.

The calls differ in how often they run, so they need slightly different handling:

| Calls | Runs when | How to draw |
|---|---|---|
| `clearNewlyReadyToPayThisTurn`, the three `resetTurn()` calls, `displayTurnStart` | Every turn | Once, unconditionally, as you have them now |
| `spawnCustomer`, `《create》VipCustomer`, `addWaitingCustomer` (messages 5 to 7) | Only on arrival turns (1, 4, 7, 10, 13 and 16) | Wrap them in an `opt [isArrivalTurn(currentTurn)]` fragment |

The `opt` matters because, as drawn, the arrival messages look like they happen every turn. The guard fixes that with no extra messages, and your existing note about the arrival rule can stay inside it.

For turns 8 to 12, draw none of these calls. The only exception would be turn 10, which is an arrival turn, but its new customer plays no part in the VIP scenario, so a single note will do: "Turn 10 also has an arrival (not shown, not involved)." That tells a marker you haven't missed it.

One other thing is easy to overlook. `displayTurnStart` returns the queue and table state, and that state changes every turn (the vip leaves the queue in turn 8, for example). You don't need to redraw it, since the return value is just the current state, but it does explain why a reader might wonder how the manager knows the vip is seated. A single sentence in your top note covers it: "Turn start steps run every turn as in turn 7; only scenario differences are shown below."

---

## Conversation 2 (Link 2) — UML Sequence diagram correctness review 

---

**Q1. Student:**

Uploads (completed) UML sequence diagram and stage 2 code in zip file.
check correctness of UML sequence diagram based on the code in zip file

**AI:**

I'll unpack the zip and read the sequence diagram, then compare the diagram's messages against the actual code.The PDF has no text layer, so I'll read it visually. Reading the diagram slices and the core code now.Now the code. I'll read the core classes in full.I've got the code and the overall diagram. A few arrows and message numbers are too small to be sure of, so I'll zoom into those areas.The diagram is mostly correct. The flow, guard conditions, arithmetic and lifeline targets match the code. I found one clear error, one inconsistent note, and a handful of smaller inaccuracies.

I checked the scenario numbers by hand, assuming the manager's choices are as drawn:
- Customer ids and menu picks match the code. Customer 2 gets Pasta ($18, 2 prep units) and the VIP gets Salad.
- Revenue adds up: 1500 + 1800 = 3300, and the VIP pays `round(900 × 0.9)` = 810, for a total of 4110.
- Satisfaction decay is right: c2 goes 76 → 68, and the VIP goes 100 → 95 → 90 → 85.
- The "paid the turn after becoming ready" rule and the turn-7 spawn guard both match the code.
- The victory check and final result match.

I could not verify the final figures of 5 paid customers, 7110 cents and 77.0 average satisfaction. They depend on plays for turns 10–18 that aren't drawn. They are plausible.

## Errors to fix

1. **Message 140 `setStatus(PAID)` is wrong (turn 12).** It points at the Chef lifeline, and number 139 is skipped. `Cashier.collectPayment` makes only these calls, in this order:
   - `o.setStatus(PAID)`
   - `c.setStatus(PAID)`
   - `t.clear()`
   - `c.calculateBill(o.getItem().getPriceCents())`

   The turn 9 version (messages 100–103) is correct. Delete 140 and renumber.

2. **The note on turns 13 to 18 is inconsistent.** It says those turns repeat "the same cycle for Customer 4 (regular, Turn 10), Customer 5 … Customer 6 …". Customer 4 arrives at turn 10, which is inside the part you drew, so it is not in turns 13–18. `spawnCustomer(10)` runs in the loop and adds a regular customer to the waiting queue, but turns 10–12 never show it. Customer 4 also loses 8 satisfaction each turn at turn 10, which `applySatisfactionDecay` would process. Fix the wording to "Customers 5 and 6 (turns 13, 16)". Add a note at turn 10 that Customer 4 is queued and not drawn.

3. **Numbering has a duplicate.** The call `displayTurnSummary(r,7)` and its return both carry the number 57. The return should be 58, and everything after it shifts by one.

## Smaller inaccuracies

4. **Turn 7, choice B decay order.** `applySatisfactionDecay` collects the waiting queue first, then tables in order. In choice B the VIP is at table 1 and c2 is at table 2, so the VIP is processed first (5, then 8). The diagram draws c2 first (messages 51 and 53).
5. **Turn 7 `updateEatingCustomers` loop, choice A.** Messages 47 and 48 draw both c2 and the VIP. In choice A the VIP is still in the queue, so only c2 is visited. Only B visits both.
6. **Turn 9 chef action is missing a step.** `executeChefAction(PREPARE)` first calls `findOrderById(3)` on the restaurant before calling the chef. The serve flows show this step (messages 20 and 114), but the chef flow omits it.
7. **Turn 9 `getOrderPreference` has no return.** The Salad item goes back to the menu's caller, but there is no return arrow from the VIP to the waiter. Message 79 ends at the VIP and the waiter never gets the item.
8. **`currentTurn = 1` (message 3) is misplaced.** The code sets it in the field initializer and constructor, not in `runGame()`. Move it to the `GameController` creation or note it.
9. **Staff reset (messages 9–11).** The code runs `for (Staff member : restaurant.getStaff()) member.resetTurn()`. The diagram omits the `getStaff()` call. It is better drawn as a loop than as three separate messages.
10. **The abandoned-list branch is only a note.** `displayDepartures` is guarded by `if (size > 0)`. Draw it as an `opt` frame instead of a note.
11. **Small notation points:**
    - Message 43 should read `ChefAction(WAIT,-1)`, with a capital C.
    - `evaluateVictoryConditions()` has no boolean return arrow before the `alt` frame.
    - Messages 149–154 sit inside that call, so nesting them in its activation would be more accurate.

I checked the other guard notes. `useTask()` is called only after the checks pass, and "task NOT consumed" is correct. The seat guard `!t1.isOccupied()` is also correct.

## Not an error
The diagram leaves out many calls the code makes: the UI's getters, `getWaiter()`/`getChef()`/`getCashier()`, and `getCustomer()` on the order. That is fine for a sequence diagram, but say so in a note.

---

**Q2. Student:**

how do i mention leaving out calls in the note

**AI:**

Add one short note at the top of your existing red intro note, or as its own note beside it. Here is wording you can paste:

> **Simplifications:** Only calls that change game state or affect the scenario outcome are shown. Internal lookups and display getters are omitted (for example `getStaff()`, `getWaiter()`/`getChef()`/`getCashier()`, `getCustomer()`, `getOrder()`, and the getters `ConsoleUI` calls on `Restaurant` to print the turn display and summary). Return values of simple getters are not drawn. Each loop is unrolled only for the customers involved in the scenario.

If you want it shorter:

> Trivial getters, helper lookups and `ConsoleUI` display queries are omitted for readability. Only state-changing calls and decision-relevant checks are shown.

Some tips:
- **Place it near the top**, in the same style as your existing red note, so readers see it before the first message.
- **Say what you kept**, not just what you dropped. The "state-changing calls and decision-relevant checks" phrase does this, and it makes the omissions look deliberate.
- **Don't use it to excuse real mistakes.** Messages 140, 57 and the Customer 4 note should still be fixed, because those are errors, not simplifications.
- **Keep one thing consistent**: if you say getters are omitted, don't draw some of them (like `getStatus()` at 47/48). Either drop those too, or word the note as "most getters omitted".

--- 

**Q3. Student:**

if i say most getters are omitted, does it affect the accuracy of the sequence diagram?

**AI:**

No, not in a way that counts as an error. A sequence diagram is allowed to abstract. It doesn't have to show every call, only the ones that matter for the scenario it describes. Saying "most getters are omitted" tells the reader the diagram is a deliberate simplification, so a missing getter isn't a mistake.

What affects accuracy is what you leave out, not whether you say so.

**Safe to omit:**
- Simple getters that only read data and don't change state or affect a decision, such as `getStaff()` or `getCustomer()`.
- Display queries `ConsoleUI` makes to print information.

**Keep these:**
- State changes, such as `setStatus`, `seatCustomer` and `useTask`.
- Checks that decide an outcome, such as `isReady()` and the guards you already note.
- Calls that show who collaborates with whom, such as `Restaurant → Waiter` or `Cashier → Customer.calculateBill`.

Two cautions:
- **"Most" is vague.** A marker may ask which ones you kept. A clearer wording is "getters that do not affect the outcome are omitted; getters that drive a decision (e.g. `getStatus()`, `isReady()`) are shown." That matches what you drew.
- **A getter that decides something should stay.** `getStatus()` in the decay loop and `isReady()` in `serveOrder` decide what happens, so keep them. `getStaff()` before `resetTurn()` doesn't, so it can go.

Your diagram already follows this rule, apart from the errors I listed earlier. With the note, it stays accurate.

---

**Q4. Student:**

Turn 9 `getOrderPreference` has no return. The Salad item goes back to the menu’s caller, but there is no return arrow from the VIP to the waiter. Message 79 ends at the VIP and the waiter never gets the item. should it return Salad to waiter?

**AI:**

Yes, the VIP should return Salad to the waiter.

In `Waiter.takeOrder`, the code is `MenuItem item = c.getOrderPreference(m);`. The waiter makes the call, so the result has to go back to the waiter. The waiter then uses it in `new Order(id, c, item)`, which is message 80.

The corrected sequence for that part:

| # | Message | From → To |
|---|---------|-----------|
| 77 | `getOrderPreference(menu)` | waiter → vip |
| 78 | `getItems().get(0)` | vip → menu |
| 79 | `Salad` (return) | menu → vip |
| 80 | `Salad ($9.00, 1 prep unit)` (return) | vip → waiter |
| 81 | `<<create>> Order(3, vip, Salad)` | waiter → o3 |

Everything after that shifts by one. Do the renumbering together with the fixes for messages 57 and 140, so you only renumber once.

If you want to keep it short, you can merge 79 and 80 into one return from the VIP to the waiter. Then drop the menu return, since `getItems().get(0)` is a plain list lookup. The version above is the more faithful one. It is also consistent with how you drew the other calls, where each call into an entity has a return.

---

**Q5. Student:**

`currentTurn = 1` (message 3) is misplaced. The code sets it in the field initializer and constructor, not in `runGame()`. Move it to the `GameController` creation or note it. How do i note this

**AI:**

You have two ways to do it. Option 1 is more accurate.

**Option 1 (recommended): put it in the constructor note and delete message 3**

Edit the yellow note beside message 1 so it reads:

> Main(): new Restaurant(), new ConsoleUI(), new GameController(restaurant, ui)
> GameController constructor: currentTurn = 1

Then delete message 3 and renumber. `runGame()` (message 2) goes straight into the loop. This matches the code, where `currentTurn = 1` is set in the constructor and in the field initializer.

**Option 2: keep message 3, but relabel it**

If you don't want to renumber, change message 3 from `currentTurn = 1` to a note placed on the GameController lifeline right after message 2:

> currentTurn already initialised to 1 in GameController constructor

This is accurate, but it leaves a note that does nothing in the flow, so Option 1 is cleaner.

**Don't** draw `<<create>> GameController` as a separate message unless you also add a GameController creation arrow from Main. Your diagram already shows GameController as a lifeline that exists from the start, so a note is enough.

You also have a similar case at message 147, `currentTurn = 19`. In the code, that value comes from `currentTurn++` at the end of turn 18, inside the loop in `runGame()`. Your diagram handles it correctly, so leave it.

---

**Q6. Student:**

4. Staff reset (messages 9–11). The code runs `for (Staff member : restaurant.getStaff()) member.resetTurn()`. The diagram omits the `getStaff()` call. It is better drawn as a loop than as three separate messages.
5. The abandoned-list branch is only a note. `displayDepartures` is guarded by `if (size > 0)`. Draw it as an `opt` frame instead of a note.// how should it look like
   
**AI:**

Both are drawn as UML combined fragments, so here is the layout for each.

## 4. Staff reset as a loop

```
gc:GameController                r:Restaurant        waiter / chef / cashier
      |  4a getStaff()                |                        |
      |------------------------------>|                        |
      |  4b staffList  (dashed return)|                        |
      |<- - - - - - - - - - - - - - - |                        |
      | ┌─loop──────────────────────────────────────────────┐  |
      | │[for each Staff member in staffList]               │  |
      | │   resetTurn()                                     │  |
      | │---------------------------------------------------│->|
      | └───────────────────────────────────────────────────┘  |
```

How to draw it in your tool:
- Add `getStaff()` from gc to r, with a dashed return carrying the list.
- Draw one loop frame with the guard `[for each Staff member]`.
- Inside it, draw one `resetTurn()` arrow. Because the three staff share the same method, you can either:
  - extend the arrow to the Waiter lifeline and add a note "same call sent to Chef and Cashier", or
  - keep three arrows inside the loop, one to each lifeline. This is the clearer option for your diagram since the staff lifelines are all visible.
- Renumber the messages: 9 = `getStaff()`, 10 = return, 11–13 = `resetTurn()`, and so on. Do this together with your other renumbering.

## 5. Abandoned departures as an `opt` frame

```
gc:GameController                                     ui:ConsoleUI
      |  abandonedCustomersList  (return from r)          |
      | ┌─opt───────────────────────────────────────────┐ |
      | │[abandonedCustomersList.size() > 0]            │ |
      | │   displayDepartures(abandonedCustomersList)   │ |
      | │-----------------------------------------------│>|
      | └───────────────────────────────────────────────┘ |
      |  displayTurnSummary(r, 7)                         |
      |-------------------------------------------------->|
```

How to draw it:
- Keep the return `abandonedCustomerList = []` from r to gc.
- After it, draw an `opt` frame on the gc and ui lifelines with the guard `[size > 0]`.
- Inside it, draw `displayDepartures(abandonedCustomersList)` from gc to ui. Add a dashed return if you want the "Press Enter" input to show.
- Keep your existing yellow note as a short comment next to the frame: "not entered in turn 7, list is empty".
- Draw `displayTurnSummary(r, 7)` after the frame, outside it.

The two `opt` frames you already have (the satisfaction check and this one) should look alike. The first one is inside the decay loop and this one is not, so don't nest them.

---

**Q7. Student:**

evaluateVictoryConditions() has no boolean return arrow before the alt frame.
Messages 149–154 sit inside that call, so nesting them in its activation would be more accurate. // elaborate

**AI:**

Both points are about how the self-call at message 148 is drawn.

## What the code does

```java
boolean won = evaluateVictoryConditions();   // in runGame()
ui.displayFinalResult(won);
```

and inside `evaluateVictoryConditions()`:

```java
restaurant.getPaidCustomerCount()       // 149/150
restaurant.getRevenueCents()            // 151/152
restaurant.getAveragePaidSatisfaction() // 153/154
return meetsPaid && meetsRevenue && meetsSatisfaction;
```

So the three queries to the restaurant happen during the call, and the method then returns a single boolean to `runGame()`. That boolean is what chooses between the two `displayFinalResult` branches.

## Problem 1: no return arrow

Message 148 is drawn as a self-call, but no return comes back. The `alt` frame then appears with a three-part condition, as if the controller recomputed it. In the code, the alt branch depends on `won`, which is the value the method returned.

**Fix:** after message 154, add a dashed return arrow from the end of the activation back to the caller:

```
155  won = true   (dashed return, gc → gc)
```

Use `won = true` for your scenario, since 5 ≥ 4, 7110 ≥ 4500 and 77.0 ≥ 60. Then the `alt` frame can use the simple guards `[won == true]` and `[else]`. If you want to keep the three-part condition, put it in a note on the evaluate call instead: "won = paid ≥ 4 AND revenue ≥ 4500 AND avg ≥ 60.0".

## Problem 2: messages 149–154 are outside the activation

In your diagram, 148 has a small activation box, and 149–154 are drawn below it on the plain lifeline. In UML, a call's activation bar covers everything that happens while the method runs. Messages 149–154 happen during `evaluateVictoryConditions()`, so they should start from inside that bar.

**Fix:** stretch the activation bar from message 148 down to the new return arrow, so 149–154 all leave from it:

```
gc:GameController                        r:Restaurant
   │ 148 evaluateVictoryConditions()
   ├─┐
   │▐│ 149 getPaidCustomerCount()  ─────────────▶│
   │▐│ 150 5                       ◀ ─ ─ ─ ─ ─ ─ │
   │▐│ 151 getRevenueCents()       ─────────────▶│
   │▐│ 152 7110                    ◀ ─ ─ ─ ─ ─ ─ │
   │▐│ 153 getAveragePaidSatisfaction() ────────▶│
   │▐│ 154 77.0                    ◀ ─ ─ ─ ─ ─ ─ │
   ├─┘ 155 won = true  (return)
   │
   │ ┌─alt─────────────────────────┐
   │ │ [won == true]               │
   │ │  156 displayFinalResult(true)  ──▶ ui
   │ │ - - - - - - - - - - - - - - │
   │ │ [else]                      │
   │ │  158 displayFinalResult(false) ─▶ ui
   │ └─────────────────────────────┘
```

Steps in your tool:
1. Extend the activation bar from 148 down to just after 154.
2. Add the dashed return arrow (155) at the bottom of the bar.
3. Change the `alt` guards to `[won == true]` and `[else]`.
4. Renumber 155 onward (you will already be renumbering for the other fixes).

Neither change alters the behaviour you drew. It only makes the diagram match the code's call structure, since the result is computed inside the method and handed back as a value.

---

**Q8. Student:**

but there is already an activation bar for rungame(), do i add on top of it?

**AI:**

Yes, you stack it on top of the `runGame()` bar. UML calls this a nested activation.

When an object calls its own method, the new activation bar is drawn slightly to the right of the existing one and overlaps it:

```
gc:GameController
   ║ runGame() activation (long bar, whole game)
   ║
   ║ 148 evaluateVictoryConditions()
   ║───┐
   ║  ▐▌  ← nested bar, offset a few pixels right
   ║  ▐▌ 149 getPaidCustomerCount() ──▶ r
   ║  ▐▌ 150 5  ◀ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─
   ║  ▐▌ ...
   ║  ▐▌ 154 77.0
   ║◀─┘  155 won = true (return)
   ║
   ║ alt frame ...
```

Practical points:
- **Offset it to the right** of the main bar by roughly half the bar width, so the two stay visible. If you draw it directly on top, it just looks like one thick bar.
- **Start it at the arrowhead of message 148.** The self-call arrow loops out from the main bar and points into the top of the new bar.
- **End it at the return arrow (155)**, which comes back to the main bar. The runGame() bar continues underneath.
- **The messages 149–154 start from the nested bar**, not from the main one. That is what shows they happen inside `evaluateVictoryConditions()`.
- **You already do this elsewhere.** Your other self-calls, such as `startTurn`, `spawnCustomer` and `handleWaiterAction`, use the same small offset bars. Copy their style so it stays consistent.

The same applies to `startTurn(currentTurn)` at message 4. Everything in that turn technically happens inside its activation, but with the loop frame around it you can leave that as it is.

---

**Q9. Student:**

Uploads updated sequence diagram.
check against the code in zip file for accuracy

**AI:**

I'll open the new PDF and compare it against the code I've already read.Tesseract is available, so I'll OCR the whole page to get every message label with its position. Then I'll look at the key regions by eye.The OCR shows that most of my earlier fixes are in the revised diagram. I'll look at the regions that are still uncertain: the chef flow, the decay loop, turn 12 payment, and the result step.The revised diagram now matches the code in nearly every respect, with two things left to fix. I checked all 162 messages against the source, including the arrow targets and message order. Only the sequence diagram is in this PDF, despite the "Class_Diagram" in the filename, so I checked nothing for a class diagram.

## Still to fix

1. **Message 94 `findOrderById(3)` is drawn in the wrong place (turn 9, chef action).**
   - It is a self-call on `GameController` and comes before `executeChefAction(PREPARE)` (message 95).
   - In the code, `findOrderById` is a private method of `Restaurant`, called inside `executeChefAction`.
   - Draw it as a self-call on `r`, after the `executeChefAction` arrow. Your turn 7 serve flow (messages 20–21) and turn 10 flow (118–119) already do this correctly.
   - The order should be 94 `executeChefAction(PREPARE)`, then 95 `findOrderById(3)` on `r`, then 96 `prepareOrder(o3)`. The numbers stay the same afterwards.

2. **The last red note on turns 13 to 18 still lists Customer 4.**
   - It says "Turns 13 to 18 repeat the same cycle for Customer 4 (regular, Turn 10), Customer 5 …". Customer 4 arrives at turn 10, not in 13–18.
   - Your turn 10 note about Customer 4 is good. Change this one to "Customers 4–6 (regular turn 10, critic turn 13, regular turn 16) follow the same cycle", or drop Customer 4 from it.

## Optional
The `alt` guard still spells out all three conditions. Since message 158 returns `won = True`, you can change the guards to `[won == true]` and `[else]`. The current guard isn't wrong.

## Fixed since the first version
- `currentTurn = 1` is now in the constructor note, and message 3 is gone.
- `getStaff()` and a loop over the resets are drawn.
- Choice B decay order is now VIP first (52 and 54), then c2 (53 and 55).
- The notes on messages 48 and 49 now say that choice A only visits c2 and choice B visits both.
- The `opt [size > 0]` frame wraps `displayDepartures`.
- Numbering is sequential with no duplicates.
- `getOrderPreference` now returns Salad to the waiter, via messages 80–83.
- The stray `setStatus(PAID)` to the Chef is gone.
- Messages 141–143 go to o3, vip and t1.
- `evaluateVictoryConditions()` has a nested activation and a `won = True` return (message 158).
- The simplification note at the top is added.
- `ChefAction(WAIT,-1)` is written correctly.

The scenario figures for turns 13–18 (5 paid customers, 7110 cents, 77.0 average) still depend on plays that aren't drawn. I can't check them against the code, but they are consistent with it.

---
