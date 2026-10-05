import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class GameConsole extends BorderPane {
    public GameStats gameStats;
    public GameBoard gameBoard;
    public EndScreen endScreen;
    private StackPane stackPane;
    public GameConsole() {
        //instantiate game objects
        this.gameStats = new GameStats();
        this.gameBoard = new GameBoard(this,gameStats);
        this.endScreen = new EndScreen(this);
        this.stackPane = new StackPane();
        stackPane.getChildren().add(gameBoard);

        // Add the panes to the correct zones.
        this.setCenter(stackPane);
        this.setTop(gameStats);

    }

    public void gameOver(){
        // Display Game Over screen
        stackPane.getChildren().add(endScreen);

    }

    public void restart(){
        // Remove old instances and create new ones
        this.gameStats = new GameStats();
        this.gameBoard = new GameBoard(this,gameStats);
        this.endScreen = new EndScreen(this);
        stackPane.getChildren().clear();
        stackPane.getChildren().add(gameBoard);
        this.setCenter(stackPane);
        this.setTop(gameStats);
    }
}
