/**
 * NOTE:
 * This checker makes the following assumptions:
 *      - The player grid is a 2D array of integer values which correspond to a specific colour.
 *      - The colour-code used is as follows: filled=0, unknown=1, empty=2, yellow=3. This is subject to change.
 *      - The row and column clues are given as a list of lists of integers.
 *      - An example of a clue for a black and white nonogram: [5, 0] which means 5 cells in colour 'filled' (or black)
 *      - An example of a clue for a colourful nonogram: [5, 3] which means 5 cells in colour yellow.
 *      - An example of a longer clue for a black and white nonogram: [5, 0, 2, 0, 1, 0] which means 5, 2, 1 cells in the colour 'filled' (black).
 *      - An example of a longer clue for a colourful nonogram: [5, 3, 2, 0, 1, 3] which means 5 cells in yellow, 2 in black, 1 in yellow.
 *      - These clues will be changed the match the form in the JSON loader (or whatever nonogram definition we agree on).
 *      - The hard-coded grids and clues used to test this are blanks_smiler and colour_wink. This class needs more testing.
 *      - This code will hopefully be cleaned up a lot in the future.
 */

import java.util.*;

public class Checker {
    /**
    * The player grid and clues have been hard-coded for testing. The methods for these are at the bottom.
    */
    public static void main(String[] args) {
        int[][] blanksSmiler = createGrid("blanks_smiler");
        List<List<Integer>> rowCluesBlankSmiler = createRowClues("blanks_smiler");
        List<List<Integer>> columnCluesBlankSmiler = createColumnClues("blanks_smiler");
        checkNonogram(blanksSmiler, rowCluesBlankSmiler, columnCluesBlankSmiler);
 
        int[][] colourWink = createGrid("colour_wink");
        List<List<Integer>> rowCluesColourWink = createRowClues("colour_wink");
        List<List<Integer>> columnCluesColourWink = createColumnClues("colour_wink");
        checkNonogram(colourWink, rowCluesColourWink, columnCluesColourWink);
    }
 
    /**
    * Loops through the number of rows, and calls the checkRow() method for that given row, and for the corresponding clue. For testing, it
    * prints the result to the console, but this would be removed in the end.
    */
    public static void checkNonogram(int[][] grid, List<List<Integer>> rowClues, List<List<Integer>> columnClues) {
        boolean hasBeenWrong = false;
 
        // For each row in the 2D array grid, check the ith row against the ith clue.
        for (int i = 0; i < grid.length; i++) {
            boolean correctRow = checkRow(grid[i], rowClues.get(i));
            if (!correctRow) {
                hasBeenWrong = true;
                System.out.println("INCORRECT ROW " + (i+1) );
            }
            else if (i == grid.length - 1 && !hasBeenWrong) {
                System.out.println("CORRECT ROWS");
            }
            boolean correctColumn = checkColumn(grid, columnClues.get(i), i);
            if (!correctColumn) {
                hasBeenWrong = true;
                System.out.println("INCORRECT COLUMN " + (i+1));
            }
            else if (i == grid.length - 1 && !hasBeenWrong) {
                System.out.println("CORRECT COLUMNS");
            }
        }
     }
 
    /**
    * This checks a specific row abides with a specific clue. This method could be neatened up in the future and needs a bit more work.
    */
    public static boolean checkRow(int[] row, List<Integer> rowClue) {
        // For global access within the method
        int numCorrect = 0;         // Keeps track of the number of cells which are correct for a given clue.
        int positionInRow = 0;      // Keeps track of the position in the row, i.e. the cell number.
        int numSquares = 0;         // This is the count clue, i.e. how many squares are a given colour.
        int squareColour = 0;       // This is the square colour.
 
        // Loops through the size of the clue list. Some clue lists are short, such as [5, 0] which is 5 squares in the colour 0 (black).
        // Others are longer, such as [2,0, 3,3, 1,0] which is 2 squares in colour 0 (black), 3 squares in colour 3 (yellow), 1 square in colour 0 (black).
        for (int j = 0; j < rowClue.size(); j+=2) {
            numCorrect = 0;
            numSquares = rowClue.get(j);
            squareColour = rowClue.get(j+1);
 
            // Loops through each cell in the row, starting from 0.
            for (int i = positionInRow; i < row.length; i++) {
                // Checks if the number of squares matches the clue count, which signals that that part of the clue is complete.
                if (numCorrect == numSquares) {
                    if (row[i] != squareColour) { // The cell MUST be a different colour, otherwise the clue has not been satisfied (e.g. the clue is 4 but there are 5 filled in)
                        if (j != rowClue.size() - 2) {
                            break; // Break only if not on the last clue. Fixes 0, 0, 0, 2, 2, 0 and clue is 3. Allows for clues with multiple counts.
                        }
                    }
                    else {
                        return false;
                    }
                }
 
                // If the cell matches the square colour given in the clue, then it is correct and one is added to the counter.
                if (row[i] == squareColour) {
                    numCorrect++;
                }

                // The cell is incorrect if it is any colour other than the square colour, or if it is blank but the clue has not been finished
                // e.g. Clue was 5 black squares with no spaces but the player entered 2 black, one space, 2 black.
                else if (row[i] != 2 || (row[i] == 2 && numCorrect > 0 && numSquares != numCorrect)) {
                    return false;
                }
 
                // Go to the next position in the row.
                positionInRow++;
            }
        }
 
        // Once the whole row has been iterated through, if the number of cells are correct for the clue, true is returned.
        if (numCorrect == numSquares) {
            return true;
        }
        else {
            return false;
        }
    }

    /*
    - The whole 2D array must be passed in.
    - You need to loop through grid[a][b], where a is the row number, and b is the element in that row. For a column, b will remain fixed and a will change:
    - grid[0][0], grid[1][0], grid[2][0], grid[3][0], grid[4][0], grid[5][0] (this is one column).
    - We can create a general checkColumn method, which checks a particular column. This will be called inside a loop which iterates through the
    - number of columns, i. Since i is the column number, we can pass this in to the method, and this is going to be our b.
    - Within the checkColumn method, a loop will iterate over the number of rows, and this number is our a.
    - Lots of code repetition here and in checkRow - could try and combine.
    */
    public static boolean checkColumn(int[][] column, List<Integer> columnClue, int columnNumber) {
        int numCorrect = 0;         
        int positionInRow = 0;   
        int numSquares = 0;  
        int squareColour = 0;

        for (int j = 0; j < columnClue.size(); j+=2) {
            numCorrect = 0;
            numSquares = columnClue.get(j);
            squareColour = columnClue.get(j+1);

            for (int i = positionInRow; i < column.length; i++) {
                if (numCorrect == numSquares) {
                    if (column[i][columnNumber] != squareColour) {
                        if (j != columnClue.size() - 2) {
                            break;
                        }
                    }
                    else {
                        return false;
                    }
                }

                if (column[i][columnNumber] == squareColour) {
                    numCorrect++;
                }

                else if (column[i][columnNumber] != 2) {
                    return false;
                }

                positionInRow++;
            }
        }
        if (numCorrect == numSquares) {
            return true;
        }
        else {
            return false;
        }
    }
 
    // ******************************************************************************************************************************************
    // All methods below will most likely not be in the finished product, they are here to test the class and contain hard-coded grids and clues.
    // The grids below represent the players grid. These values can be changed to test the program to see if they match the clues.
    public static int[][] createGrid(String name) {
        int[][] grid = null;
        if (name.equals("blanks_smiler")) {
            grid = new int[][] {
                {2, 2, 0, 0, 0, 0, 0, 2, 2, 2},
                {2, 0, 0, 0, 0, 0, 0, 0, 2, 2},
                {0, 0, 2, 0, 0, 0, 2, 0, 0, 2},
                {0, 0, 2, 0, 0, 0, 2, 0, 0, 2},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                {0, 2, 0, 0, 0, 0, 0, 2, 0, 2},
                {0, 0, 2, 0, 0, 0, 2, 0, 0, 2},
                {2, 0, 0, 2, 2, 2, 0, 0, 2, 2},
                {2, 2, 0, 0, 0, 0, 0, 2, 2, 2},
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 2}
            };
        }
        else if (name.equals("colour_wink")) {
            // yellow 3, black 0, empty 2
            grid = new int[][] {
                {2, 2, 3, 3, 3, 3, 3, 2, 2},
                {2, 3, 3, 3, 3, 3, 3, 3, 2},
                {3, 3, 0, 3, 3, 3, 3, 3, 3},
                {3, 3, 0, 3, 3, 3, 0, 3, 3},
                {3, 3, 3, 3, 3, 3, 3, 3, 3},
                {3, 3, 3, 3, 3, 3, 3, 3, 3},
                {3, 3, 0, 3, 3, 3, 0, 3, 3},
                {2, 3, 3, 0, 0, 0, 3, 3, 2},
                {2, 2, 3, 3, 3, 3, 3, 2, 2}
            };
        }
        return grid;
    }
 
    public static List<List<Integer>> createRowClues(String name) {
        List<List<Integer>> rowClues = null;
        if (name.equals("blanks_smiler")) {
            rowClues = Arrays.asList(
                Arrays.asList(5, 0),                // 5 cells in colour 0 (black/filled)
                Arrays.asList(7, 0),
                Arrays.asList(2, 0, 3, 0, 2, 0),    // 2, 3, 2 cell configuration in colour 0
                Arrays.asList(2, 0, 3, 0, 2, 0),
                Arrays.asList(9, 0),
                Arrays.asList(1, 0, 5, 0, 1, 0),
                Arrays.asList(2, 0, 3, 0, 2, 0),
                Arrays.asList(2, 0, 2, 0),
                Arrays.asList(5, 0),
                Arrays.asList(0, 0)
            );
        }
        else if (name.equals("colour_wink")) {
            rowClues = Arrays.asList(
                Arrays.asList(5, 3),
                Arrays.asList(7, 3),
                Arrays.asList(2, 3, 1, 0, 6, 3),
                Arrays.asList(2, 3, 1, 0, 3, 3, 1, 0, 2, 3),
                Arrays.asList(9, 3),
                Arrays.asList(9, 3),
                Arrays.asList(2, 3, 1, 0, 3, 3, 1, 0, 2, 3),
                Arrays.asList(2, 3, 3, 0, 2, 3),
                Arrays.asList(5, 3)
            );
        }
        return rowClues;
    }
 
    public static List<List<Integer>> createColumnClues(String name) {
        List<List<Integer>> columnClues = null;
        if (name.equals("blanks_smiler")) {
            columnClues = Arrays.asList(
                Arrays.asList(5, 0),
                Arrays.asList(4, 0, 2, 0),
                Arrays.asList(2, 0, 2, 0, 2, 0),
                Arrays.asList(7, 0, 1, 0),
                Arrays.asList(7, 0, 1, 0),
                Arrays.asList(7, 0, 1, 0),
                Arrays.asList(2, 0, 2, 0, 2, 0),
                Arrays.asList(4, 0, 2, 0),
                Arrays.asList(5, 0),
                Arrays.asList(0, 0)
            );
        }
        else if (name.equals("colour_wink")) {
            columnClues = Arrays.asList(
                Arrays.asList(5, 3),
                Arrays.asList(7, 3),
                Arrays.asList(2, 3, 2, 0, 2, 3, 1, 0, 2, 3),
                Arrays.asList(7, 3, 1, 0, 1, 3),
                Arrays.asList(7, 3, 1, 0, 1, 3),
                Arrays.asList(7, 3, 1, 0, 1, 3),
                Arrays.asList(3, 3, 1, 0, 2, 3, 1, 0, 2, 3),
                Arrays.asList(7, 3),
                Arrays.asList(5, 3)
            );
        }
        return columnClues;
    }
 }