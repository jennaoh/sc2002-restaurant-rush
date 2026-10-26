package control;

import entity.*;

public class GameController {
    private Restaurant restaurant;
    private int currentTurn = 1;
    private final int TOTAL_TURNS = 18;

    public GameController(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.currentTurn = 1;
    }

    public void runGame() {
        while (currentTurn <= TOTAL_TURNS) {
            // todo turn logic
            // console ui for printing & user input
            currentTurn++;
        }
    }
}
