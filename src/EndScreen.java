import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class EndScreen extends VBox {
    public int flagCount = 0;
    private Text txtObj;
    private Button restart;
    private GameConsole gameConsole;
    private GameBoard gameBoard;
    private GameStats gameStats;
    public EndScreen(GameConsole gameConsole){
        this.gameConsole = gameConsole;
        flagCount = 40;

        txtObj = new Text("Game Over!");
        txtObj.setFill(Color.WHITE);
        txtObj.setStroke(Color.BLACK);
        txtObj.setStrokeWidth(1);
        txtObj.setFont(new Font(40));
        restart = new Button("Restart");
        this.setAlignment(Pos.CENTER);
        restart.setOnAction(new clickHandler());
        this.getChildren().addAll(txtObj,restart);
    }

    private class clickHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e){
            // Tell game handler to restart
            gameConsole.restart();
        }
    }
}
