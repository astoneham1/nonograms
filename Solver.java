import java.util.ArrayList;
import java.util.HashMap;

import java.util.stream.Collectors;

public class Solver {
    
    Grid solverGrid;
    Grid puzzleGrid;
    ArrayList<Integer> rowClueSums, colClueSums, solverRowSum, solverColSum, colors;
    //for readability sake I made these variables, will prob delete later
    int row = 0;
    int col = 0;

    // if i have time, i want to try to create a solver that works based on row and column solves as a more efficient methhod for 
    // HashMap <Integer, Integer> columnSums = new HashMap<>();
    // HashMap <Integer, Integer> rowSums = new HashMap<>();

    public Solver (Grid puzzleGrid) {
        this.solverGrid = new Grid(puzzleGrid.rows, puzzleGrid.columns, "Moves/solverMoves.json");
        this.puzzleGrid = puzzleGrid;
        // get the possible colors and store the in the colors arraylist
        row = puzzleGrid.rows;
        col = puzzleGrid.columns;
        rowClueSums = puzzleGrid.rowClues.stream().map(x -> x.getCounts().stream().reduce(0, (a, b) -> a+b)).collect(Collectors.toCollection(ArrayList::new));
        colClueSums = puzzleGrid.columnClues.stream().map(x -> x.getCounts().stream().reduce(0, (a, b) -> a+b)).collect(Collectors.toCollection(ArrayList::new));
    }

    public void solve() {
        // if placement of num == rowsum || same for col
            // 
    }



    // while (! Checker.checkNonogram(solverGrid.grid, puzzleGrid.rowClues, puzzleGrid.columnClues).isEmpty()) {
    //     // rowClueSums = puzzleGrid.rowClues.stream().map(x -> x.getCounts().stream().reduce(0, (a, b) -> a+b)).collect(Collectors.toCollection(ArrayList::new));
    //     // colClueSums = puzzleGrid.columnClues.stream().map(x -> x.getCounts().stream().reduce(0, (a, b) -> a+b)).collect(Collectors.toCollection(ArrayList::new));

    // }


}