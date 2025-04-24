import java.util.ArrayList;

public class SolverMain {
    Grid puzzleGrid;
    Grid solverGrid;
    int rowLen, colLen;
    int rowNum, colNum;
    ArrayList<Arrangements> rowArrangements;
    ArrayList<Arrangements> colArrangements;
    // boolean color;

    public SolverMain(Grid puzzleGrid, boolean color) {
        this.puzzleGrid = puzzleGrid;
        this.solverGrid = new Grid(puzzleGrid.rows, puzzleGrid.columns, "Moves/solverMoves.json");
        this.rowLen = puzzleGrid.columns;
        this.rowNum = puzzleGrid.rows;
        this.colNum = puzzleGrid.columns;
        this.colLen = puzzleGrid.rows;
        // this.color = color;
    }

    public void solver() {
        // creates solverspace by creating an array of possible clue arrangements for the rows and columns
        // then, if there is only one possible arrangement for each row/col, this is drawn to the board
        colArrangements = new ArrayList<>();
        rowArrangements = new ArrayList<>();
        for (int i = 0; i < colNum; i++) {
            colArrangements.add(new Arrangements(puzzleGrid, colLen, puzzleGrid.columnClues.get(i)));
            if (colArrangements.get(i).arrangement.size() == 1) {
                drawNodeCol(colArrangements.get(i).arrangement.get(0), i);
            }   
        }
 
        for (int i = 0; i < rowNum; i++) {
            rowArrangements.add(new Arrangements(puzzleGrid, rowLen, puzzleGrid.rowClues.get(i)));
            if (rowArrangements.get(i).arrangement.size() == 1) {
                drawNodeRow(rowArrangements.get(i).arrangement.get(0), i);
            }   
        }
 

        solve();
    }


// solver
// recursive funct that will back track and then solve, need to implement
    public void solve() {
        // loop through the rows and columns and check their arrangements 
        for (int c = 0; c < colNum; c ++) {
            if (compatibleCol(colArrangements.get(c), c)) {
                for (int r = 0; r < rowNum; r ++) {
                    
                }    
            }
            else {
                // recurse and try a new col state
            }
        }
    }

// check line compatability

    // these methods go through a set of arrangements and check if they are compatible and if there is a correct arrangement, assign a node state
    public boolean compatibleRow(Arrangements a, int rowNum) {
        for (Node n : a.arrangement) {
            if (checkNodeRow(n, rowNum)) {
                a.solved = true;
                return true;
            }
        }
        a.solved = false;
        return false;
    }


    public boolean compatibleCol(Arrangements a, int colNum) {
        for (Node n : a.arrangement) {
            if (checkNodeCol(n, colNum)) {
                a.solved = true;
                return true;
            }
        }
        a.solved = false;
        return false;
    }

    // helper methods for the previous two
    public boolean checkNodeRow(Node n, int row) {
        for (int i = 0; i < n.places.size(); i ++) {
            for (int j = n.places.get(i); j < (n.places.get(i) + n.c.counts.get(i)); j++) {
                if (solverGrid.grid[j][row] != n.c.colour.get(i)) {
                    return false;
                }
            }
        }
        drawNodeRow(n, row);
        return true;
    }

    public boolean checkNodeCol(Node n, int column) {
        for (int i = 0; i < n.places.size(); i ++) {
            for (int j = n.places.get(i); j < (n.places.get(i) + n.c.counts.get(i)); j++) {
                if (solverGrid.grid[column][j] != n.c.colour.get(i)) {
                    return false;
                }
            }
        }
        drawNodeCol(n, column);
        return true;
    }







// draw a node (eg. one possible arrangement of clues)
    public void drawNodeRow(Node n, int row) {
        for (int i = 0; i < n.places.size(); i++) {
            updateRangeRow(n.places.get(i), n.places.get(i)+n.c.counts.get(i), n.c.colour.get(i), row);
        }
    }

    public void drawNodeCol(Node n, int col) {
        for (int i = 0; i < n.places.size(); i++) {
            updateRangeCol(n.places.get(i), n.places.get(i)+n.c.counts.get(i), n.c.colour.get(i), col);
        }
    }

    public void updateRangeRow(int start, int end, int color, int row) {
        for (int i = start; i < end; i++) {
            solverGrid.updateMove(row , i, color);
        }   
    }


    public void updateRangeCol(int start, int end, int color, int col) {
        for (int i = start; i < end; i++) {
            solverGrid.updateMove(i, col, color);
        }   
    }


    // for (int i = 0; i < rowNum; i ++) {
    //     for (int j = 0; j < colNum; j++) {
    //         System.out.print(solverGrid.grid[i][j]);
    //     }
    //     System.out.println();
    // }
    // // solverGrid.updateMove(1, 3, 3);
    // drawNodeCol(0, colLen, 3, 0);
    // updateRangeRow(0, rowLen, 4, 0);
    // System.out.println("-------------------------------------------");
    // for (int i = 0; i < rowNum; i ++) {
    //     for (int j = 0; j < colNum; j++) {
    //         System.out.print(solverGrid.grid[i][j]);
    //     }
    //     System.out.println();
    // }
    // System.out.println("-------------------------------------------");


    // // This method scans the grid and then optimizes it
    // public void optimization() {
    //     optimizeRow(0,0);
    //     optimizeCol(0,0);
    // }

    // public void optimizeRow(int sum, int place) {
    //     for (int i = 0; i < puzzleGrid.rowClues.size(); i++) {
    //         sum = place = 0;
    //         for (Integer j : puzzleGrid.rowClues.get(i).counts) {
    //             if (j == rows) {
    //                 updateRangeRow(0, rows, puzzleGrid.columnClues.get(i).colour.get(0), i);
    //             }
    //             if (j == rows - 1) {
    //                 updateRangeRow(1, rows - 1, puzzleGrid.columnClues.get(i).colour.get(0), i);
    //             }
    //             sum += j;
    //         }
    //         if (sum + puzzleGrid.rowClues.get(i).counts.size() - 1 == rows && color == false) {
    //             for (int j = 0; j < puzzleGrid.rowClues.get(i).counts.size(); j++) {
    //                 updateRangeRow(place, puzzleGrid.rowClues.get(i).counts.get(j) + place, puzzleGrid.rowClues.get(i).colour.get(j), i);
    //                 place += puzzleGrid.rowClues.get(i).counts.get(j) + 1;
    //             }
    //         }
    //         if (sum == rows && color == true) {
    //             for (int j = 0; j < puzzleGrid.rowClues.get(i).counts.size(); j++) {
    //                 updateRangeRow(place, puzzleGrid.rowClues.get(i).counts.get(j) + place, puzzleGrid.rowClues.get(i).colour.get(j), i);
    //                 place += puzzleGrid.rowClues.get(i).counts.get(j);
    //             }
    //         }
    //     }
    // }


    // public void optimizeCol(int sum, int place) {
    //     for (int i = 0; i < puzzleGrid.columnClues.size(); i++) {
    //         sum = place = 0;
    //         // place = 0;
    //         for (Integer j : puzzleGrid.columnClues.get(i).counts) {
    //             if (j == cols) {
    //                 updateRangeCol(0, cols, puzzleGrid.columnClues.get(i).colour.get(0), i);
    //             }
    //             if (j == cols - 1) {
    //                 updateRangeCol(1, cols - 1, puzzleGrid.columnClues.get(i).colour.get(0), i);
    //             }
    //             sum += j;
    //         }
    //         if (sum + puzzleGrid.columnClues.get(i).counts.size() - 1 == cols && color == false) {
    //             for (int j = 0; j < puzzleGrid.columnClues.get(i).counts.size(); j++) {
    //                 updateRangeCol(place, puzzleGrid.columnClues.get(i).counts.get(j) + place, puzzleGrid.columnClues.get(i).colour.get(j), i);
    //                 place += puzzleGrid.columnClues.get(i).counts.get(j) + 1;
    //             }
    //         }
    //         if (sum == cols && color == true) {
    //             for (int j = 0; j < puzzleGrid.columnClues.get(i).counts.size(); j++) {
    //                 updateRangeCol(place, puzzleGrid.columnClues.get(i).counts.get(j) + place, puzzleGrid.columnClues.get(i).colour.get(j), i);
    //                 place += puzzleGrid.columnClues.get(i).counts.get(j);
    //             }
    //         }
    //     }
    // }
    
}