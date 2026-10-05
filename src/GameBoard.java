import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class GameBoard extends GridPane {
    public Tile[][] microGrid = new Tile[18][14];
    public Tile[] mines = new Tile[40];
    public int maxMines = 40;
    private GameStats gs;
    private GameConsole gc;
    public GameBoard(GameConsole gc,GameStats gs){
        this.gs = gs;
        this.gc = gc;
        clickHandler click = new clickHandler();
        this.setGridLinesVisible(true);
        boolean p = false; // Used for alternating colors
        // Fill the board with tiles
        for (int i = 0; i < microGrid.length; i++){
            for (int j = 0; j<microGrid[i].length; j++){

                Tile newT = new Tile(gc,this);
                this.add(newT,i,j);
                microGrid[i][j] = newT;
                if (!p){
                    newT.setStyle("-fx-background-color: #6bbd2e");
                    p = true;
                } else {
                    newT.setStyle("-fx-background-color: LAWNGREEN");
                    p = false;
                }
                newT.setOnMouseClicked(click);
            }
            if (!p) p = true; else p = false;
        }
        setMines();
        setAdjacent();
    }

    private boolean inArray(Tile[] array, Tile item){
        // Find if any item exists in an array
        for (int i =0; i<array.length; i++){
            if (array[i] == null) continue;
            if (item == null) return false;
            if (array[i].equals(item)) return true;
        }
        return false;

    }

    private void setMines(){
        int mineCount = 0;
        Random random = new Random();
        while (mineCount != maxMines){ // Since we get random Ints each loop and then reject duplicates a while loop will work best
            int ranRow = random.nextInt(0,14);
            int ranCol = random.nextInt(0,18);
            if (inArray(mines,microGrid[ranCol][ranRow])) continue;
            microGrid[ranCol][ranRow].setIsMine();
            mines[mineCount] = microGrid[ranCol][ranRow];
            mineCount++;
            //if (mineCount == 15) return;
        }
    }

    private void setAdjacent(){
        // Tell every tile to find how many mine blocks they are adjacent to
        ArrayList<Tile> emptyTiles = new ArrayList<>(); // Using ArrayList instead of array because empty tile amount varies
        for(int i =0; i < microGrid.length; i++){ // O(n) column
            for(int j = 0; j < microGrid[j].length; j++){ // O(n^2) row
                Tile obj = microGrid[i][j];


                if(obj.isMine) continue; // If tile is a mine then no need check for nearby mines

                // Check every possible adjacent tile and see if its a mine
                if (i - 1 > -1){
                    if (microGrid[i-1][j].isMine) obj.touchingMines++;
                    if (j - 1 > -1 && microGrid[i-1][j-1].isMine) obj.touchingMines++;
                    if (j + 1 < microGrid[i].length && microGrid[i-1][j+1].isMine) obj.touchingMines++;
                }
                if (i + 1 < microGrid.length){
                    if (microGrid[i+1][j].isMine) obj.touchingMines++;
                    if (j - 1 > -1 && microGrid[i+1][j-1].isMine) obj.touchingMines++;
                    if (j + 1 < microGrid[i].length && microGrid[i+1][j+1].isMine) obj.touchingMines++;
                }
                if (j - 1 > -1 && microGrid[i][j-1].isMine) obj.touchingMines++;
                if (j + 1 < microGrid[i].length && microGrid[i][j+1].isMine) obj.touchingMines++;

                if (obj.touchingMines == 0) emptyTiles.add(obj); // If there are 0 mines nearby add to reveal list

            }
        }

        // after board is revealed show one 0 tile and its adjacent tiles
        Random random = new Random();
        int ranTile = random.nextInt(0,emptyTiles.size());
        emptyTiles.get(ranTile).revealTile();
        revealAdjacent(emptyTiles.get(ranTile));
    }

    private int[] getTilePosition(Tile t){
        // Get a tiles position within the microGrid 2d array
        int[] pos = new int[2];
        pos[0] = -1;
        pos[1] = -1;
        for(int i =0; i < microGrid.length; i++) { // O(n) column
            for (int j = 0; j < microGrid[j].length; j++) { // O(n^2) row
                if (t.equals(microGrid[i][j])){
                    pos[0] = i;
                    pos[1] = j;
                    return pos;
                }
            }
        }
        return pos;
    }


    private void revealAdjacent(Tile tile){
        // Reveal adjacent tiles
        int[] pos = getTilePosition(tile);
        int i = pos[0];
        int j = pos[1];
        if (i - 1 > -1){
            if (!microGrid[i-1][j].isMine) microGrid[i-1][j].revealTile();
            if (j - 1 > -1 && !microGrid[i-1][j-1].isMine) microGrid[i-1][j-1].revealTile();
            if (j + 1 < microGrid[i].length && !microGrid[i-1][j+1].isMine) microGrid[i-1][j+1].revealTile();
        }
        if (i + 1 < microGrid.length){
            if (!microGrid[i+1][j].isMine) microGrid[i+1][j].revealTile();
            if (j - 1 > -1 && !microGrid[i+1][j-1].isMine) microGrid[i+1][j-1].revealTile();
            if (j + 1 < microGrid[i].length && !microGrid[i+1][j+1].isMine) microGrid[i+1][j+1].revealTile();
        }
        if (j - 1 > -1 && !microGrid[i][j-1].isMine) microGrid[i][j-1].revealTile();;
        if (j + 1 < microGrid[i].length && !microGrid[i][j+1].isMine) microGrid[i][j+1].revealTile();

    }
    public class clickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e){
            Tile groundTile = (Tile) e.getSource();
            if (e.isShiftDown()){ // Shift + Click will trigger flag placement
                if (!groundTile.flagged && !groundTile.revealed){ // Add flag if one isnt there
                    groundTile.setText("🚩");
                    groundTile.flagged = true;
                    gs.incrementFlag(-1);
                } else { // Remove flag if it is present
                    groundTile.setText("");
                    groundTile.flagged = false;
                    gs.incrementFlag(1);
                }

            }else { // Regular mouse click
                if (groundTile.flagged) return; // Flagged tiles reject clicks
                if (groundTile.touchingMines == 0) revealAdjacent(groundTile); // Tiles with no adjacent mines reveal tiles in its radius
                groundTile.revealTile(); // tell tile to show itself
                if (groundTile.isMine){
                    for (int i = 0; i < mines.length; i++){
                        mines[i].revealTile(); // Reveal position of all the mines
                    }

                    gc.gameOver(); // Show endScreen
                }

            }
        }
    }
}
