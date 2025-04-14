// Grid object 
// This can be used in two ways:
    // to store the solutions to the puzzles
    // to store the in progress puzzles


import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

public class Grid {
    public int rows;
    public int columns;
    public ArrayList<ArrayList<ArrayList<String>>> rowClues = new ArrayList<>();
    public ArrayList<ArrayList<ArrayList<String>>> columnClues = new ArrayList<>();
    public ArrayList<Move> moves = new ArrayList<>(); 
    public int[][] grid;
    String path;

    // this stores the solved clues of the puzzle
    public Grid(ArrayList<ArrayList<ArrayList<String>>> rows, ArrayList<ArrayList<ArrayList<String>>> columns, int r, int c) {
        this.rowClues = rows;
        this.columnClues = columns;
        this.rows = r;
        this.columns = c;
    }

    // this stores the puzzle in progress
    public Grid(int r, int c, String path) {
        this.rows = r;
        this.columns = c;
        this.path = path;
        // creates a blank grid that the will be updated throughout the gameplay and populates it with unknown values
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
            JsonObjectBuilder moveBuilder = Json.createObjectBuilder();
            JsonArrayBuilder moveArrayBuilder = Json.createArrayBuilder();
            for (Move m : this.moves) {
                moveBuilder.add("row", m.location[0]) 
                            .add("cols", m.location[1])
                            .add("oldColor", m.oldColor)
                            .add("newColor", m.newColor);
                JsonObject moveObject = moveBuilder.build();
                moveArrayBuilder.add(moveObject);
            }
            JsonArray moveArray = moveArrayBuilder.build();
            writer.writeArray(moveArray);
            writer.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // // when recalling an old grid, reading from a json file to update grid to the last move
    // public void loadMoves() {
        
    // }
}