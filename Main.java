import entity.Restaurant;
import control.GameController;

/// import consoleui also here

// MainApp, starts the game.
public class Main {
    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();
        // ConsoleUI ui = new ConsoleUI(controller);
        GameController controller = new GameController(restaurant);
        controller.runGame();
    }
}
