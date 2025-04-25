import java.util.ArrayList;
import java.util.Arrays;

public class SolverMain {
    Grid puzzleGrid;
    Grid solverGrid;
    int rowLen, colLen;
    int rowNum, colNum;
    ArrayList<Arrangements> rowArrangements;
    ArrayList<Arrangements> colArrangements;
    // boolean color;

    public SolverMain(Grid puzzleGrid) {
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
            colArrangements.add(new Arrangements(colLen, puzzleGrid.columnClues.get(i)));
            if (colArrangements.get(i).arrangement.size() == 1) {
                drawNodeCol(colArrangements.get(i).arrangement.get(0), i);
                colArrangements.get(i).solved = true; 
            } 
        }
        for (int i = 0; i < rowNum; i++) {
            rowArrangements.add(new Arrangements(rowLen, puzzleGrid.rowClues.get(i)));
            if (rowArrangements.get(i).arrangement.size() == 1) {
                drawNodeRow(rowArrangements.get(i).arrangement.get(0), i);
                rowArrangements.get(i).solved = true;
            }   
        }
        // optimization();
        solve();
    }


// solver
// recursive funct that will back track and then solve, need to implement
    public void solve() {
        while (!checkSolved()) {
            for (int i = 0; i < rowArrangements.size(); i++) {
                compatibleRow(rowArrangements.get(i), i);
            }
            for (int j = 0; j < colArrangements.size(); j++) { //kills my boy 1, 6
                compatibleCol(colArrangements.get(j), j);
            }
            // for (int i = 0; i < rowLen; i++) {
            //     for (int j = 0; j < colLen; j++) {
            //         System.out.print(solverGrid.grid[i][j]);
            //     }
            //     System.out.println();
            // }
            System.out.println("----------------------------------------------");
            for (Arrangements arrangements : colArrangements) {
                for (Node n : arrangements.arrangement) {
                    System.out.print(n.places);
                }
                System.out.println();
            }
        }

    }


// check solved
    public boolean checkSolved() {
        for (int i = 0; i < rowArrangements.size(); i++) {
            if (compatibleRow(rowArrangements.get(i), i) == false) {
                return false;
            }
        }
        for (int j = 0; j < colArrangements.size(); j++) {
            if (compatibleCol(colArrangements.get(j), j) == false) {
                return false;
            }
        }
        return true;
    }

// check line compatability
    // these methods go through a set of arrangements and check if they are compatible and if there is a correct arrangement, assign a node state
    public boolean compatibleRow(Arrangements a, int rowNum) {
        int i = 0;
        if (a.solved == true) {
            return true;
        }
        ArrayList<Node> tempArray = new ArrayList<>();
        while (i < a.arrangement.size()) {
            if (checkNodeRow(a.arrangement.get(i), rowNum) == true) {
                tempArray.add(a.arrangement.get(i));
            }
            i++;
        }
        a.arrangement = new ArrayList<>(tempArray);
        if (a.arrangement.size() == 1) {
            a.solved = true;
            drawNodeRow(a.arrangement.get(0), rowNum);
            return true;
        }
        a.solved = false;
        return false;
    }


    public boolean compatibleCol(Arrangements a, int colNum) {
        int i = 0;
        if (a.solved == true) {
            return true;
        }
        ArrayList<Node> tempArray = new ArrayList<>();
        while (i < a.arrangement.size()) {
            if (checkNodeCol(a.arrangement.get(i), colNum) == true) {
                tempArray.add(a.arrangement.get(i));
            }
            i++;
        }
        a.arrangement = new ArrayList<>(tempArray);
        if (a.arrangement.size() == 1) {
            a.solved = true;
            drawNodeCol(a.arrangement.get(0), colNum);
            return true;
        }
        a.solved = false;
        return false;
    }

    // helper methods for the previous two
    public boolean checkNodeRow(Node n, int row) {
        int[] testArr = renderRow(n, rowLen);
        for (int i = 0; i < rowLen; i++) {
            if (solverGrid.grid[row][i] == 1 && testArr[i] != 0) {
                return false;
            }
            if (!(solverGrid.grid[row][i] == 1 || solverGrid.grid[row][i] == 0) && testArr[i] != solverGrid.grid[row][i]) {
                return false;
            }
        }
        return true;
    }

    public boolean checkNodeCol(Node n, int column) {
        int[] testArr = renderRow(n, colLen);
        for (int i = 0; i < colLen; i++) {
            if (solverGrid.grid[i][column] == 1 && testArr[i] != 0) {
                return false;
            }
            if (!(solverGrid.grid[i][column] == 1 || solverGrid.grid[i][column] == 0) && testArr[i] != solverGrid.grid[i][column]) {
                return false;
            }
        }
        return true;
    }

    public int[] renderRow(Node n, int lineLength) {
        int[] render = new int[lineLength];
        for (int i = 0; i < lineLength; i++) {
            render[i] = 0;
        }
        for (int i = 0; i < n.places.size(); i++) {
            for (int j = n.places.get(i); j < n.places.get(i) + n.c.counts.get(i); j++) {
                render[j] = n.c.colour.get(i);
            }
        }
        return render;
    }







// draw a node (eg. one possible arrangement of clues)
    public void drawNodeRow(Node n, int row) {
        updateRangeRow(0, rowLen, 1, row);
        for (int i = 0; i < n.places.size(); i++) {
            updateRangeRow(n.places.get(i), n.places.get(i)+n.c.counts.get(i), n.c.colour.get(i), row);
        }
    }

    public void drawNodeCol(Node n, int col) {
        updateRangeCol(0, colLen, 1, col);
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

      // This method scans the grid and then optimizes it
    public void optimization() {
        optimizeRow();
        optimizeCol();
    }

    public void optimizeRow() {
        for (int i = 0; i < puzzleGrid.rowClues.size(); i++) {
            for (Integer j : puzzleGrid.rowClues.get(i).counts) {
                if (j == rowLen) {
                    updateRangeRow(0, rowLen, puzzleGrid.columnClues.get(i).colour.get(0), i);
                }
                if (rowLen%2 == 0 && j > rowLen / 2) {
                    updateRangeRow(rowLen - j, rowLen - (rowLen - j), puzzleGrid.columnClues.get(i).colour.get(0), i);
                }
                // if (rowLen%2 != 0 && j > rowLen / 2) {
                //     updateRangeRow(rowLen - j, rowLen - (rowLen - j), puzzleGrid.columnClues.get(i).colour.get(0), i);
                // }
            }
        }
    }


    public void optimizeCol() {
        for (int i = 0; i < puzzleGrid.columnClues.size(); i++) {
            for (Integer j : puzzleGrid.columnClues.get(i).counts) {
                if (j == colLen) {
                    updateRangeCol(0, colLen, puzzleGrid.columnClues.get(i).colour.get(0), i);
                }
                if (colLen%2 == 0 && j > colLen / 2) {
                    updateRangeRow(colLen - j, colLen - (colLen - j), puzzleGrid.columnClues.get(i).colour.get(0), i);
                }
                // if (colLen%2 != 0 && j > colLen / 2) {
                //     updateRangeRow(colLen - j, colLen - (colLen - j), puzzleGrid.columnClues.get(i).colour.get(0), i);
                // }
            }
        }
    }
    
}
