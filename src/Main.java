import entity.Restaurant;
import control.GameController;
import boundary.ConsoleUI;

// MainApp, starts the game.
public class Main {
    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();
        ConsoleUI ui = new ConsoleUI();
        GameController controller = new GameController(restaurant, ui);
        controller.runGame();
    }
}
