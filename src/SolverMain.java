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
        // System.out.println("ROWS");
        for (int i = 0; i < rowArrangements.size(); i++) {
            System.out.print(i);
            for (Node n: rowArrangements.get(i).arrangement) {
                System.out.print(n.places);
            } 
            System.out.println();
        }
        // System.out.println("----------------------------------------------------------");
        // System.out.println("COLS");
        for (int i = 0; i < colArrangements.size(); i++) {
            // System.out.print(i);
            // for (Node n: colArrangements.get(i).arrangement) {
            //     System.out.print(n.places);
            // } 
            // System.out.println();
        }
        solve();
    }


// solver
// recursive funct that will back track and then solve, need to implement
    public void solve() {
        // while (!checkSolved()) {
            for (int i = 0; i < rowArrangements.size(); i++) {
                compatibleRow(rowArrangements.get(i), i);
            }
            for (int j = 0; j < colArrangements.size(); j++) {
                // compatibleRow(rowArrangements.get(i), i);
                compatibleCol(colArrangements.get(j), j);
            }
            // System.out.println("----------------------------------------------------------");
            for (int j = 0; j < colArrangements.size(); j++) {
                compatibleCol(colArrangements.get(j), j);
            }
            for (int i = 0; i < rowArrangements.size(); i++) {
                compatibleRow(rowArrangements.get(i), i);
            }
            // System.out.println("----------------------------------------------------------");
            for (int j = 0; j < colArrangements.size(); j++) {
                compatibleCol(colArrangements.get(j), j);
            }
            for (int i = 0; i < rowArrangements.size(); i++) {
                compatibleRow(rowArrangements.get(i), i);
            }
            // System.out.println("----------------------------------------------------------");
            for (int j = 0; j < colArrangements.size(); j++) {
                compatibleCol(colArrangements.get(j), j);
            }
            for (int i = 0; i < rowArrangements.size(); i++) {
                compatibleRow(rowArrangements.get(i), i);
            }
        // }
        // if (!checkSolved()) {
        //     solve();
        // }
    }


// check solved
    public boolean checkSolved() {
        for (int i = 0; i < rowArrangements.size(); i++) {
            if (compatibleRow(rowArrangements.get(i), i) == false) {
                return false;
            }
        }
        for (int j = 0; j < colArrangements.size(); j++) {
            if (compatibleCol(colArrangements.get(j), j)) {
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
        while (i < a.arrangement.size()) {
            if (checkNodeRow(a.arrangement.get(i), rowNum) == false) {
                a.arrangement.remove(i);
            }
            i++;
        }
        // for (Node n: a.arrangement) {
        //     System.out.print(n.places);
        // }
        // System.out.println();
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
        while (i < a.arrangement.size()) {
            if (checkNodeCol(a.arrangement.get(i), colNum) == false) {
                a.arrangement.remove(i);
            }
            i++;
        }
        // System.out.println(a.arrangement.size());
        // for (Node n: a.arrangement) {
        //     System.out.print(n.places);
        // }
        // System.out.println();
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
            if (testArr[i] != solverGrid.grid[row][i] && solverGrid.grid[row][i] != 0 && solverGrid.grid[row][i] != 1) {
                return false;
            }
            if (testArr[i] == 0 && solverGrid.grid[row][i] != 0  && solverGrid.grid[row][i] != 1) {
                return false;
            }
        }
        return true;
    }

    public boolean checkNodeCol(Node n, int column) {
        int[] testArr = renderRow(n, colLen);
        for (int i = 0; i < colLen; i++) {
            if (testArr[i] != solverGrid.grid[i][column] && solverGrid.grid[i][column] != 0 && solverGrid.grid[i][column] != 1) {
                return false;
            }
            if (testArr[i] == 0 && solverGrid.grid[i][column] != 0  && solverGrid.grid[i][column] != 1) {
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

    
}






            // for (int i = 0; i < n.places.size(); i ++) {
        //     // checks that there are no incorrect colors in the colored sections
        //     for (int j = n.places.get(i); j < (n.places.get(i) + n.c.counts.get(i)); j++) {
        //         if (solverGrid.grid[row][j] != n.c.colour.get(i) && solverGrid.grid[row][j] != 0) {
        //             return false;
        //         }
        //     }
        //     // checks that the noncolored sections are blank
        //     if (i == 0) {
        //         // checks that if this is the first clue, then the preceding cells are whitespaces
        //         for (int j = 0; j < n.places.get(i); j++) {
        //             if (solverGrid.grid[row][j] != 0 || solverGrid.grid[row][j] != 1) {
        //                 return false;
        //             }
        //         }
        //     }
        //     else if (i < n.places.size() - 1) {
        //         // checks whitespace between the end of the clue and the beginning of the next
        //         for (int j = n.places.get(i) + n.c.counts.get(i); j < n.places.get(i + 1); j++) {
        //             if (solverGrid.grid[row][j] != 0 || solverGrid.grid[row][j] != 1) {
        //                 return false;
        //             }
        //         }
        //     }
        //     else {
        //         // checks for whitespaces between the end 
        //         for (int j = n.places.get(i) + n.c.counts.get(i); j < rowLen; j++) {
        //             if (solverGrid.grid[row][j] != 0 || solverGrid.grid[row][j] != 1) {
        //                 return false;
        //             }
        //         }
        //     }
        // }
        // drawNodeRow(n, row);

                // checks for blank space before the first element
            // if (i == 0) {
            //     System.out.println("we're ere");
            //     for (int j = 0; j < n.places.get(i); i++) {
            //         if (solverGrid.grid[j][column] != 1 && solverGrid.grid[j][column] != 0) {
            //             return false;
            //         }
            //     }    
            // }
            // // checks that the middle spaces are correctly populated
            // for (int j = n.places.get(i); j < (n.places.get(i) + n.c.counts.get(i)); j++) {
            //     System.out.println("we're lol");
            //     if (solverGrid.grid[j][column] != n.c.colour.get(i) && solverGrid.grid[j][column] != 0) {
            //         return false;
            //     }
            // }
            // // checks, if before the end, that the spaces prior to the next clue are blank
            // if (i < n.places.size() - 1) {
            //     System.out.println("we're haha");
            //     for (int j = n.places.get(i) + n.c.counts.get(i) + 1; j < n.places.get(i+1); j++) {
            //         if (solverGrid.grid[j][column] != 1 && solverGrid.grid[j][column] != 0) {
            //             return false;
            //         }
            //     }
            // } 
            // // checks if, the end, that there are blanks before the end
            // if (i == n.places.size() - 1) {
            //     System.out.println("we're hereee");
            //     for (int j = n.places.get(i) + n.c.counts.get(i) + 1; j < rowLen; j++) {
            //         // System.out.println(solverGrid.grid[j][column]);
            //         if (solverGrid.grid[j][column] != 1 && solverGrid.grid[j][column] != 0) {
            //             return false;
            //         }
            //     }
            // }




            // // checks that the noncolored sections are blank
            // if (i == 0 && n.places.size() != 1) {
            //     // checks that if this is the first clue, then the preceding cells are whitespaces
            //     for (int j = 0; j < n.places.get(i); j++) {
            //         if (solverGrid.grid[j][column] != 0 || solverGrid.grid[j][column] != 1) {
            //             return false;
            //         }
            //     }
            // }
            // else if (i < n.places.size() - 1) {
            //     // checks whitespace between the end of the clue and the beginning of the next
            //     for (int j = n.places.get(i) + n.c.counts.get(i); j < n.places.get(i + 1); j++) {
            //         if (solverGrid.grid[j][column] != 0 || solverGrid.grid[j][column] != 1) {
            //             return false;
            //         }
            //     }
            // }
            // else if (i == 0 && n.places.size() == 1) {
            //     System.out.print("single cell triggered");
            //     // checks that if this is the first clue, then the preceding cells are whitespaces
            //     for (int j = 0; j <= n.places.get(i); j++) {
            //         if (solverGrid.grid[j][column] != 0 || solverGrid.grid[j][column] != 1) {
            //             return false;
            //         }
            //     }
            //     for (int j = n.places.get(i) + n.c.counts.get(i) + 1; j < column; j++) {
            //         if (solverGrid.grid[j][column] != 0 || solverGrid.grid[j][column] != 1) {
            //             return false;
            //         }
            //     }
            // }
            // else {
            //     // checks for whitespaces between the end 
            //     for (int j = n.places.get(i) + n.c.counts.get(i); j < column; j++) {
            //         if (solverGrid.grid[j][column] != 0 || solverGrid.grid[j][column] != 1) {
            //             return false;
            //         }
            //     }
            // }


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


            // // loop through the rows and columns and check their arrangements 
        // for (int c = 0; c < colNum; c ++) {
        //     System.out.println("Column" + c + "----------------------------------------------");
        //     compatibleCol(colArrangements.get(c), c);
        //     // if (compatibleCol(colArrangements.get(c), c)) {
        // //         for (int r = 0; r < rowNum; r ++) {

        // //         }    
        // }
        // for (int r = 0; r < rowNum; r ++) {
        //     System.out.println("Row" + r + "----------------------------------------------");
        //     compatibleRow(rowArrangements.get(r), r);
        //     // if (compatibleCol(colArrangements.get(c), c)) {
        // //         for (int r = 0; r < rowNum; r ++) {

        // //         }    
        // }
        // // for (int r = 0; r < rowNum; r ++) {
        // //     System.out.println("Row" + c + "----------------------------------------------");
        // // }
        // //     else {
        // //         // recurse and try a new col state
        // //     }
        // // }