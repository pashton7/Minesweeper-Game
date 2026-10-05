import javafx.scene.Group;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Tile extends Pane {
    private GameConsole gc;
    private GameBoard gb;
    public int touchingMines;
    public boolean isMine;
    public boolean flagged;
    public boolean revealed;
    private Text txtObj;
    public Tile(GameConsole gc, GameBoard gb){
        // Create and style the Tile object
        this.gc = gc;
        this.gb = gb;

        Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT));
        this.setBorder(border);
        this.setPrefSize(20,20);
        txtObj = new Text("");
        Group textGroup = new Group(txtObj);
        this.getChildren().add(textGroup);
        txtObj.setLayoutY(15);
        txtObj.setLayoutX(5);

        // Initialize variables
        this.flagged = false;
        touchingMines = 0;
        isMine = false;
        revealed = false;

    }

    public void setIsMine(){ // Set tile status
        isMine = true;
    }

    public void setText(String txt){ // Set tile text
        this.txtObj.setText(txt);
    }

    public void revealTile(){ // Visual tile reveal
        if (revealed) return; // No need to reveal if its already been revealed
        this.revealed = true;
        if (this.isMine){ // Revealing a mine tile
            this.setStyle("-fx-background-color: Red");
            this.setText("💣");
            //gc.gameOver();

        } else { // Revealing a normal tile
            if (this.getStyle().equals("-fx-background-color: LAWNGREEN")){ // Keep to alternating tile colors
                this.setStyle("-fx-background-color: #c88c5d");
            } else {
                this.setStyle("-fx-background-color: #f19954");
            }

            this.setText(Integer.toString(this.touchingMines));
        }
    }


}
