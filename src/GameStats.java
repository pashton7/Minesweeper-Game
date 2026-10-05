import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class GameStats extends Pane {
    public int flagCount = 0;
    public Text txtObj;
    public GameStats(){
        this.setPrefSize(40,20);
        flagCount = 40;
        Text displayTxt = new Text("Flags: ");
        displayTxt.setLayoutY(15);
        displayTxt.setLayoutX(5);
        txtObj = new Text( Integer.toString(flagCount));
        txtObj.setLayoutY(15);
        txtObj.setLayoutX(displayTxt.getLayoutX() + 40);
        this.getChildren().addAll(txtObj,displayTxt);
    }

    public void incrementFlag(int i){
        flagCount += i;
        txtObj.setText(Integer.toString(flagCount));
    }
}
