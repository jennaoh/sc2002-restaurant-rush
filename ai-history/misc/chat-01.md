# SC2002 Restaurant Rush: AI Conversation History

## Project Planning, Git Tagging, UML Review and Report Structure

**AI Tool:** Gemini (Google)  
**Project:** SC2002 Restaurant Rush  
**Attached Source:** SC2002_Restaurant_Rush_Group_Assignment brief (Word document)  
**Purpose:** Task allocation, Git workflow, UML class diagram review, report structure and submission packaging

---

## Conversation 1: Group Task Allocation

**Student:**

This is the group project of SC2002. A total of 4 members in my group. How can we allocate the task?

**AI:**

Recommended dividing work by architectural layer, feature ownership and submission deliverables rather than just splitting code.

```text
Member 1: Systems Architecture, Turn Engine & Core Mechanics (Group Lead)
Member 2: Customer Hierarchy & Extension Policies (Stage 2)
Member 3: Menu System, Stage 3 Features & Pricing Policies
Member 4: User Interface, Validation, Testing & Media Lead
```

Key responsibilities suggested:

- **Member 1:** Turn controller / game loop, state transitions (READY -> SERVED -> READY_TO_PAY -> PAID), base entities (Table, Waiter, Chef, Cashier), Git tagging, UML Sequence Diagram, SOLID design discussion.
- **Member 2:** Abstract Customer class, RegularCustomer / VIPCustomer / CriticCustomer, customer-specific behaviour without `instanceof`, UML Class Diagram, Stage 2 functional evidence.
- **Member 3:** Menu and item system (prices in integer cents), ComboMeal, Host role, HappyHour policy, seeded food selection rules, Victory and Defeat test runs.
- **Member 4:** CLI / view boundary, input validation, 15-minute video, final submission package.

Also suggested a five-phase timeline (Architecture and Stage 1, Polymorphism and Stage 2, Stage 3 and refactoring, UML and documentation, video and final packaging) and a split of the video segments so every member explains a part they understand.

---

## Conversation 2: How Git Tagging Works for Stage 1 to 3

**Student:**

How does the tagging in stage 1-3 work? How to perform this?

**AI:**

Explained that a tag is a permanent reference to a specific commit, and that `stage-1`, `stage-2` and `stage-3` should point to the commits where each stage was completed.

Key rule: creating a tag locally does not upload it, and tags must be pushed explicitly.

```bash
git add .
git commit -m "Complete Stage 1 implementation and test runs"

git tag -a stage-1 -m "Stage 1: Basic restaurant operation"
git tag -a stage-2 -m "Stage 2: Customer behavior extensions"
git tag -a stage-3 -m "Stage 3: New role and menu policy"

git push origin stage-1
git push origin --tags

git tag
git show stage-1
```

Also described creating tags through the GitHub web UI and tagging retrospectively:

```bash
git log --oneline
git tag -a stage-1 <commit-hash> -m "Stage 1 completion"
git push origin stage-1
```

---

## Conversation 3: Stage 1 Class Diagram Review

**Student:**

*(Attached an image of the Stage 1 class diagram.)*

Is the Stage 1 class diagram we did accurate? How can we improve it?

**AI:**

Said the diagram had a solid structural foundation but was not fully accurate. Main points:

- The final class diagram must match the Stage 3 code, so Stage 2 and Stage 3 classes need to be added.
- Inheritance arrows must point from subclass to superclass.
- Remove duplicate or floating classes (Table, Pasta) and placeholder text such as `+ field: type`.
- Store money as integer cents instead of `float`.
- Connect enums (CustomerState, OrderStatus) with dependency lines.
- Add missing relationships (Order and MenuItem, Staff and Table / Order / Customer, Restaurant and its collections).
- Annotate encapsulation, polymorphism and composition, and add multiplicities.

---

## Conversation 4: Revised Diagram Review (Round 2)

**Student:**

*(Attached the revised diagram.)*

Is it better now?

**AI:**

Confirmed the diagram was a large improvement: inheritance arrows were fixed, money was stored as `priceCents` / `revenueCents`, placeholders were gone and enums were wired in (CustomerStatus, OrderStatus).

Remaining issues:

1. Stage 2 and Stage 3 features were still missing (VIP, Critic, ComboMeal, Host, HappyHour).
2. OO annotation callouts (polymorphism, encapsulation, composition) were missing.
3. Minor fixes: Table to Customer multiplicity (0..1), Restaurant to Staff multiplicity (1..*), generalization arrows for Pasta, Burger and Salad.

---

## Conversation 5: Revised Diagram Review (Round 3)

**Student:**

*(Attached the updated diagram.)*

Now?

**AI:**

Said the diagram was cleaner and used proper UML conventions, with well-organised members, signatures, constructors, boundary DTOs (WaiterAction, ChefAction) and enum dependencies.

Still required for full marks:

- Add the Stage 2 and Stage 3 classes.
- Add Draw.io note callouts for polymorphism, composition and encapsulation.
- Draw generalization arrows from Waiter, Chef and Cashier to Staff, and name the Table to Customer association (for example `seats`).

---

## Conversation 6: Stage 3 Class Diagram Review

**Student:**

*(Attached the Stage 3 class diagram.)*

This is the Stage 3 class diagram.

**AI:**

Confirmed the diagram now included the Stage 2 and Stage 3 entities:

- RegularCustomer, VIPCustomer, CriticCustomer inheriting from Customer
- Host alongside Waiter, Chef and Cashier under Staff
- ComboMenuItem inheriting from MenuItem
- HappyHour connected to Restaurant
- HostAction, WaiterAction and ChefAction connected to ConsoleUI

Final polishing suggested:

- Add three callout notes (polymorphism, composition, encapsulation).
- Add multiplicity `1` on the Table side of Table to Customer.
- Add a composition diamond from ComboMenuItem to MenuItem with multiplicity 2.

---

## Conversation 7: Report Structure

**Student:**

I am working on the report now. How should the report structure be?

**AI:**

Proposed a structure aligned with the 100-mark rubric:

```text
1. Title and Team Declarations Page
2. System Overview and Game Architecture (Boundary-Control-Entity)
3. Class Diagram and Object-Oriented Design (25 marks)
4. Sequence Diagram and Interaction Scenario (20 marks)
5. Design Considerations and SOLID Principles (15 marks)
   5.1 Key abstractions and responsibility table (SRP)
   5.2 SOLID analysis
   5.3 Architectural trade-offs and considered alternatives
6. Incremental Development and Stage Transitions (20 marks)
7. Functional Evidence and Test Runs
8. Group Reflections and Video Index
```

---

## Conversation 8: Declaration Forms

**Student:**

Do we need to include the declaration form in the report?

**AI:**

Advised not to embed the declaration forms in the PDF report. According to Section 6.1 of the brief, the signed Declaration of Original Work and each member's Declaration on Use of GAI Assistance are separate files inside the submission zip.

```text
LABGroup_AssignmentGrp.zip
|-- Source Code/
|-- README.md
|-- Report.pdf
|-- Video.mp4 (or a working download link in README)
`-- Declarations/
    |-- Declaration_Original_Work.pdf
    |-- Declaration_GAI_Member1.pdf
    |-- Declaration_GAI_Member2.pdf
    |-- Declaration_GAI_Member3.pdf
    `-- Declaration_GAI_Member4.pdf
```

The report's title page should only contain a short note confirming the signed forms are included in the zip.

---

## Conversation 9: Running the GitHub Code in VS Code

**Student:**

How to execute code from GitHub on VSC?

**AI:**

Prerequisites: JDK 17 or 21, Git, and the "Extension Pack for Java" in VS Code.

Steps:

1. Copy the repository HTTPS URL, then use `Git: Clone` from the Command Palette.
2. Open the cloned folder.
3. Run `Main.java` with the Run button, or from the terminal:

```bash
javac -d bin src/*.java
java -cp bin Main
```

To test a specific stage:

```bash
git fetch --tags
git checkout stage-1
git checkout main
```

---

## Conversation 10: Tagging Stage 3

**Student:**

Our project is done but haven't tagged stage 3 yet. How to tag it?

**AI:**

```bash
git add .
git commit -m "Complete Stage 3 implementation"
git push origin main

git tag -a stage-3 -m "Stage 3: New role and menu policy"
git push origin stage-3
```

Then run `git tag` locally and check the GitHub Tags / Releases page to verify that `stage-3` points to the latest commit.

---

## Conversation 11: Uploading the AI Conversation to GitHub

**Student:**

How to upload AI conversation to GitHub?

**AI:**

Method 1 (Markdown, best for readability): copy the conversation, create a folder such as `ai-logs/`, save it as a `.md` file (for example `Member1_GAI_Log.md`) and push.

```bash
git add ai-logs/
git commit -m "Add GAI conversation logs"
git push origin main
```

Method 2 (PDF): save the chat as a PDF with Ctrl+P / Cmd+P, then use **Add file -> Upload files** on GitHub.

Reminders: pushing log files to `main` does not move the existing stage tags, and the uploaded logs should match each member's Declaration on Use of GAI Assistance.

---
