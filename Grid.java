import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

public class Grid {
    public int rows;
    public int columns;
    public ArrayList<Clue> rowClues = new ArrayList<>();
    public ArrayList<Clue> columnClues = new ArrayList<>();
    public ArrayList<Move> moves = new ArrayList<>(); 
    public int[][] grid;
    // int currentMove = 0;
    String path;

    // this constructor can be used for the solved clues of the puzzle
    public Grid(ArrayList<Clue> rows, ArrayList<Clue> columns, int r, int c) {
        this.rowClues = rows;
        this.columnClues = columns;
        this.rows = r;
        this.columns = c;
    }

    // this constructor can be used to store the puzzle in progress
    public Grid(int r, int c, String path) {
        this.rows = r;
        this.columns = c;
        this.path = path;
        // creates a blank grid that the will be updated throughout the gameplay and populates it with "unknown" values
        this.grid = new int[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                grid[i][j] = 0;
            }
        }
    }


    // updates the move on the integer grid and records the move update in the json file
    public void updateMove(int row, int col, int color) {
        // first update the arraylist of moves
        Move m = new Move(row, col, grid[row][col], color);

        /*
        Checks the list of all of the moves. Gets the location of the current cell (the one passed into this method) and compares it to the
        location of the Move object in the list. If these are the same, and the colours are the same (i.e. pressing a pink cell 20 times with the pink colour),
        and it is on the end of the list, then we remove this Move. If the move list is now empty, OR if the current cell's colour (the one passed into 
        this method) is exactly equal to the previous cell's old colour (so filling in a pink cell pink again), the oldColor is set to 0, so when undo is
        pressed, the cell will return to 'UNKNOWN'. Otherwise (so filling in a blue cell with pink), the current cells oldColor is set to the previous one's
        newColor (so undo works as intended).
        */
        for (int i = 0; i < this.moves.size(); i++) {
            int[] location = new int[] {row, col};
            if (Arrays.equals(location, this.moves.get(i).location) && color == this.moves.get(i).newColor && i == this.moves.size() - 1) {
                this.moves.remove(this.moves.get(i));
                if (this.moves.size() == 0 || (color == this.moves.get(this.moves.size() - 1).newColor)) {
                    m.oldColor = 0;
                }
                else {
                    m.oldColor = this.moves.get(this.moves.size() - 1).newColor;
                }
                break;
            }
        }

        this.moves.add(m);
        // then update the physcial grid
        this.grid[row][col] = color;
    }

    // saves the moves that are made and stored in the array into a json file
    public void saveMoves() {
        File f = new File(path);
        try {
            FileWriter w = new FileWriter(f);
            JsonWriter writer = Json.createWriter(w);
            JsonBuilderFactory factory = Json.createBuilderFactory(null);
            JsonArrayBuilder moveArrayBuilder = Json.createArrayBuilder();
            int i = 0;
            for (Move m : this.moves) {
                i++;
                JsonObject moveObject = factory.createObjectBuilder()
                            // .add(String.valueOf(i), factory.createObjectBuilder()
                            .add("row", m.location[0]) 
                            .add("cols", m.location[1])
                            .add("oldColor", m.oldColor)
                            .add("newColor", m.newColor)
                            .build();
                moveArrayBuilder.add(moveObject);
            }
            JsonArray moveArray = moveArrayBuilder.build();
            JsonObject moves = factory.createObjectBuilder()
                                .add("Moves", moveArray)
                                .build();
            writer.writeObject(moves);
            writer.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // when recalling an old grid, reading from a json file to update grid to the last move
    public void loadMoves(String newPath) {
        try {
            JsonReader reader = Json.createReader(new FileReader(newPath));
            JsonObject file = reader.readObject();
            JsonArray array = file.getJsonArray("Moves");
            for (JsonObject r : array.getValuesAs(JsonObject.class)) {
                if (r.getInt("row") < this.rows && r.getInt("cols") < this.columns) {
                    Move m = new Move(r.getInt("row"), r.getInt("cols"), r.getInt("oldColor"), r.getInt("newColor"));
                    this.grid[r.getInt("row")][r.getInt("cols")] = r.getInt("newColor");
                    this.moves.add(m);
                }
            }
            reader.close();
            //ensures that the file storing moves for the grid has the most updated moves
            saveMoves();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void undoMoves() {
        if (moves.size() > 0) {
            grid[this.moves.get(moves.size()-1).location[0]][this.moves.get(moves.size()-1).location[1]] = this.moves.get(moves.size() - 1).oldColor;
            this.moves.remove(moves.size()-1);
        }
        saveMoves();
    }

    public void clearAllMoves() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                grid[i][j] = 0;
            }
        }
        moves = new ArrayList<>();
        saveMoves();
    }
}

