import java.util.ArrayList;

public class Solver {

    Grid puzzleGrid;
    Grid solverGrid;
    int width;


    public Solver(Grid puzzleGrid) {
        this.puzzleGrid = puzzleGrid;
        this.solverGrid = new Grid(puzzleGrid.rows, puzzleGrid.columns, "Moves/solverMoves.json");
        this.width = puzzleGrid.rows;
    }

    public void solve() {
        optimization(puzzleGrid.rowClues, "row");
        optimization(puzzleGrid.columnClues, "column");
    }

    // goes through the table once and modifies it by changing the cell values for  
    public void optimization(ArrayList<Clue> clueList, String type) {
        int clueSum = 0;
        for(int i = 0; i < clueList.size(); i++) {
            if (clueList.get(i).counts.size() == 1) {
                // if the clue dictates that the whole row is filled with a certain color, then that row is filled
                if (clueList.get(i).counts.get(0) == puzzleGrid.rows && type.equals("row") || clueList.get(i).counts.get(0) == puzzleGrid.columns && type.equals("column")) {
                    if (type.equals("row")) {
                        updateRangeRow(0, puzzleGrid.rows, clueList.get(i).colour.get(0), i);  
                    }
                    else {
                        updateRangeCol(0, puzzleGrid.columns, clueList.get(i).colour.get(0), i);
                    }
                }
                // if the clue is one off the whole row, this fills in the middle of each row and columns
                if (clueList.get(i).counts.get(0) == puzzleGrid.rows -1 && type.equals("row") || clueList.get(i).counts.get(0) == puzzleGrid.columns - 1 && type.equals("column") ) {
                    if (type.equals("row")) {
                        updateRangeRow(1, puzzleGrid.rows - 1, clueList.get(i).colour.get(0), i);  
                        // for (int j = 1; j < width - 1; j++) {
                            // solverGrid.updateMove(i, j, clueList.get(i).colour.get(0));
                        // }        
                    }
                    else {
                        updateRangeCol(1, puzzleGrid.columns - 1, clueList.get(i).colour.get(0), i);
                        // for (int j = 1; j < width - 1; j++) {
                            // solverGrid.updateMove(j, i, clueList.get(i).colour.get(0));
                        // }
                    }    
                }
            } 
            else {
                for(Integer j: clueList.get(i).counts) {
                    clueSum += j;
                }
                if (clueSum + clueList.get(i).counts.size() - 1 == width) {
                    int currentPlace = 0;
                    if (type.equals("rows")) {
                        for(Integer k: clueList.get(i).counts) {
                            updateRangeRow(currentPlace, currentPlace + k, clueList.get(i).colour.get(k-1), i);
                            currentPlace = currentPlace + 1 + k;
                        }    
                    }
                    else {
                        for(Integer k: clueList.get(i).counts) {
                            updateRangeCol(currentPlace, currentPlace + k, clueList.get(i).colour.get(k-1), i);
                            currentPlace = currentPlace + 1 + k;
                        }    
                    }
                }
            }

        }
    }

    public void updateFullLine() {

    }

    public void updateRangeRow(int start, int end, int color, int row) {
        for (int i = start; i < end; i++) {
            solverGrid.updateMove(row, i, color);
        }   
    }


    public void updateRangeCol(int start, int end, int color, int col) {
        for (int i = start; i < end; i++) {
            solverGrid.updateMove(i, col, color);
        }   
    }
}






// public static void convertToClues(Grid g) {
//     for (int i = 0; i < g.rows; i ++) {
//         int currentCount = 0;
//         ArrayList<Integer> color = new ArrayList<>();
//         ArrayList<Integer> count = new ArrayList<>();
//         for (int j = 0; j < g.columns; j++) {
//             if (g.grid[i][j] != 0) {
//                 currentCount++;
//                 if ((j!= g.columns - 1  && g.grid[i][j] != g.grid[i][j+1]) || (j == g.columns-1 && g.grid[i][j] != g.grid[i][j-1])) {
//                     count.add(currentCount);
//                     color.add(g.grid[i][j]);
//                     currentCount = 0;
//                 }
//             }
//         }
//         Clue c = new Clue(count, color);
//         g.rowClues.add(c);
//     }
//     for (int i = 0; i < g.columns; i ++) {
//         int currentCount = 0;
//         ArrayList<Integer> color = new ArrayList<>();
//         ArrayList<Integer> count = new ArrayList<>();
//         for (int j = 0; j < g.rows; j++) {
//             if (g.grid[j][i] != 0) {
//                 currentCount++;
//                 if ((j!= g.columns - 1  && g.grid[j][i] != g.grid[j+1][i]) || (j == g.columns-1 && g.grid[j][i] != g.grid[j-1][i])) {
//                     count.add(currentCount);
//                     color.add(g.grid[j][i]);
//                     currentCount = 0;
//                 }
//             }
//         }
//         Clue c = new Clue(count, color);
//         g.columnClues.add(c);
//     }
// }











































// import java.util.ArrayList;
// import java.util.HashMap;

// import java.util.stream.Collectors;

// public class Solver {
    
//     Grid solverGrid;
//     Grid puzzleGrid;
//     ArrayList<Clues> rowClues, colClues;
//     ArrayList<Integer> rowClueSums, colClueSums, solverRowSum, solverColSum, colors;
//     //for readability sake I made these variables, will prob delete later
//     int row = 0;
//     int col = 0;

//     // if i have time, i want to try to create a solver that works based on row and column solves as a more efficient methhod for 
//     // HashMap <Integer, Integer> columnSums = new HashMap<>();
//     // HashMap <Integer, Integer> rowSums = new HashMap<>();

//     public Solver (Grid puzzleGrid) {
//         this.solverGrid = new Grid(puzzleGrid.rows, puzzleGrid.columns, "Moves/solverMoves.json");
//         this.puzzleGrid = puzzleGrid;
//         // get the possible colors and store the in the colors arraylist
//         row = puzzleGrid.rows;
//         col = puzzleGrid.columns;
//         rowClues = puzzleGrid.rowsClues;
//         colClues = puzzleGrid.columnClues;
//         rowClueSums = puzzleGrid.rowClues.stream().map(x -> x.getCounts().stream().reduce(0, (a, b) -> a+b)).collect(Collectors.toCollection(ArrayList::new));
//         colClueSums = puzzleGrid.columnClues.stream().map(x -> x.getCounts().stream().reduce(0, (a, b) -> a+b)).collect(Collectors.toCollection(ArrayList::new));
//     }

//     public void solve() {
//         for (int j = 0; j < row; j++) {
//             if (rowClueSums.get(j) == row) {
//                 for (int i = 0; I < col; i++) {
//                     solverGrid.updateMove(row, col, col);
//                 }
//             }
//         }
//     }


//     // while (! Checker.checkNonogram(solverGrid.grid, puzzleGrid.rowClues, puzzleGrid.columnClues).isEmpty()) {
//     //     // rowClueSums = puzzleGrid.rowClues.stream().map(x -> x.getCounts().stream().reduce(0, (a, b) -> a+b)).collect(Collectors.toCollection(ArrayList::new));
//     //     // colClueSums = puzzleGrid.columnClues.stream().map(x -> x.getCounts().stream().reduce(0, (a, b) -> a+b)).collect(Collectors.toCollection(ArrayList::new));

//     // }


// }