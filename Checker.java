/**
 * NOTES:
 *  - Currently does not compile as it calls methods from Grid.java that do not exist yet.
 *  - For testing, checkNonogram takes in a 2D int array directly since Grid.java does not have a constructor definition to pass in an already
 *    created 2D int array. The final class will pass in two Grid objects, the loaded one with the clues/constraints and the players one.
 *  - The length of the counts and colours lists must be the same, otherwise the checker will not work.
 *    E.g. Clue is 5, 3, 2 (and all the colours are 0), then the lists would be:
 *         counts = (5, 3, 2)
 *         colours = (0, 0, 0)      opposed to      colours = (0)
 */
import java.util.ArrayList;
import java.util.Arrays;

public class Checker {
    /**
     * The player grid and clues have been hard-coded for testing. The methods for these are at the bottom.
     * @param args
     */
    public static void main(String[] args) {
        Checker c = new Checker();
        int[][] blanksSmiler = c.createGrid("blanks_smiler");
        ArrayList<Clue> rowCluesBlankSmiler = c.createRowClues("blanks_smiler");
        ArrayList<Clue> columnCluesBlankSmiler = c.createColumnClues("blanks_smiler");
        ArrayList<ArrayList<Integer>> blankSmilerCheck = c.checkNonogram(blanksSmiler, rowCluesBlankSmiler, columnCluesBlankSmiler);
        
        // Print information.
        String message1 = c.getMessage(blankSmilerCheck, rowCluesBlankSmiler.size(), columnCluesBlankSmiler.size());
        System.out.println(message1 + "\n");

        int[][] colourWink = c.createGrid("colour_wink");
        ArrayList<Clue> rowCluesColourWink = c.createRowClues("colour_wink");
        ArrayList<Clue> columnCluesColourWink = c.createColumnClues("colour_wink");
        ArrayList<ArrayList<Integer>> colourWinkCheck = c.checkNonogram(colourWink, rowCluesColourWink, columnCluesColourWink);
        
        //Print information.
        String message2 = c.getMessage(colourWinkCheck, rowCluesColourWink.size(), columnCluesColourWink.size());
        System.out.println(message2);
    }

    /**
     * Loops through the number of rows, and calls the checkRow() method for that given row, and for the corresponding clue.
     * @param grid              The player's nonogram.
     * @param loadedGrid        List of row clues.
     * @param columnClues       List of column clues.
     * @return                  List of lists of coordinates where the incorrect cell is located.
     */
    public ArrayList<ArrayList<Integer>> checkNonogram(int[][] grid, ArrayList<Clue> rowClues, ArrayList<Clue> columnClues) {
        // Where incorrect rows and columns are kept.
        ArrayList<Integer> incorrectRows = new ArrayList<Integer>();
        ArrayList<Integer> incorrectColumns = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> incorrectRowsAndColumns = new ArrayList<ArrayList<Integer>>(); // this is what gets returned

        // For each row in the 2D array grid, check the ith row against the ith clue.
        for (int i = 0; i < grid.length; i++) {
            boolean correctRow = checkLine(grid[i], rowClues.get(i));
            if (!correctRow) {
                incorrectRows.add(i);
            }
        }
        for (int j = 0; j < grid[0].length; j++) {
            // Get the ith column, and check it against the corresponding clue.
            int[] column = getColumn(grid, j);
            boolean correctColumn = checkLine(column, columnClues.get(j));
            if (!correctColumn) {
                incorrectColumns.add(j);
            }
        }
        incorrectRowsAndColumns.add(incorrectRows);
        incorrectRowsAndColumns.add(incorrectColumns);
        return incorrectRowsAndColumns;
    }
  
    /**
     * This checks a specific line abides with a specific clue.
     * @param line          The row or column which is being checked.
     * @param clue          The clue which the row or column is being checked against.
     * @param lineNumber    This is needed so the incorrect coordinates can be returned.
     * @param isRow         Since the column has been treated as a row, we need to flip the coordinates if we are checking a column.
     * @return              List of coordinates of incorrect cells.
     */
    public boolean checkLine(int[] line, Clue clue) {
        // For global access within the method
        ArrayList<Integer> counts = clue.getCounts();       // Gets the list of counts for this clue.
        ArrayList<Integer> colours = clue.getColours();     // Gets the list of colours for this clue, given as integers.
        int numCorrect = 0;         // Keeps track of the number of cells which are correct for a given clue.
        int positionInRow = 0;      // Keeps track of the position in the row, i.e. the cell number.
        int numSquares = 0;         // This is the count clue, i.e. how many squares are a given colour.
        int squareColour = 0;       // This is the square colour.

        // Loops through the size of the counts list. The length of the counts and colours lists must be the same.
        // Example counts list: [5, 3, 2] which is 5 squares, a space, 3 squares, a space, 2 squares, a space.
        for (int j = 0; j < counts.size(); j++) {
            numCorrect = 0;
            numSquares = counts.get(j);
            // i.e. No clue for a given row (see unsolvable smiler)
            if (numSquares == 0) {
                break;
            }
            squareColour = colours.get(j);
  
            // Loops through each cell in the line, starting from 0.
            for (int i = positionInRow; i < line.length; i++) {
                // Checks if the number of squares matches the clue count, which signals that that part of the clue is complete.
                if (numCorrect == numSquares) {
                    if (line[i] != squareColour) { // The cell MUST be a different colour, otherwise the clue has not been satisfied (e.g. the clue is 4 but there are 5 filled in)
                        if (j != counts.size() - 1) {
                            break; // Break only if not on the last part of the clue, so clues like (5, 3, 2) will keep going.
                                   // Fixes 0, 0, 0, 2, 2, 0 and clue is 3. Allows for clues with multiple counts.
                        }
                    }
                    else {
                        return false;
                    }
                }
  
                // If the cell matches the square colour given in the clue, then it is correct and one is added to the counter.
                if (line[i] == squareColour) {
                    numCorrect++;
                }
 
                // The cell is incorrect if it is any colour other than the square colour, (except blank or unknown),
                //  or if it is blank or unknown but the clue has not been finished (i.e. 22222 is correct, but 220222 is NOT)
                // e.g. Clue was 5 black squares with no spaces but the player entered 2 black, one space, 2 black.
                else if ((line[i] != 1 && line[i] != 0) || ((line[i] == 1 || line[i] == 0) && numCorrect > 0 && numSquares != numCorrect)) {
                    return false;
                }
  
                // Go to the next position in the line.
                positionInRow++;
            }
        }
        // Once the whole line has been iterated through, if the number of cells are correct for the clue, true is returned.
        if (numCorrect == numSquares) {
            return true;
        }
        else {
            return false;
        }
    }
 
    /**
     * From the 2D array, gets the column as an int array so only one 'checkLine' method is required. This gets all elements
     * for a fixed row number, e.g. grid[i][0], grid[i][1], grid[i][2] where i is every element per column and 0,1,2... is the column number.
     * @param grid          The 2D array player grid
     * @param num           The column number, i.e. get all elements from this column.
     * @return              An integer array which is the ith column from the player grid.
     */
    public int[] getColumn(int[][] grid, int num) {
        int length = grid[0].length;
        int[] column = new int[length];
 
        for (int i = 0; i < grid.length; i++) {
            column[i] = grid[i][num];
        }
 
        return column;
    }

    public String getMessage(ArrayList<ArrayList<Integer>> incorrectLines, int rows, int columns) {
        StringBuilder sb = new StringBuilder();
        ArrayList<Integer> incorrectRows = incorrectLines.get(0);
        ArrayList<Integer> incorrectColumns = incorrectLines.get(1);

        if (incorrectRows.size() == 0) {
            sb.append("All rows are correct.\n");
        }
        if (incorrectColumns.size() == 0) {
            sb.append("All columns are correct.\n");
        }
        if (incorrectColumns.size() == 0 && incorrectRows.size() == 0) {
            double progress = getProgress(incorrectRows.size(), incorrectColumns.size(), rows, columns);
            sb.append("Completion: " + progress + "%");
            String congrats = sb.toString();
            return congrats;
        }

        for (int i : incorrectRows) {
            int rowNum = i+1;
            sb.append("Incorrect row " + rowNum + "\n");
        }
        for (int c : incorrectColumns) {
            int columnNum = c+1;
            sb.append("Incorrect column " + columnNum + "\n");
        }

        double progress = getProgress(incorrectRows.size(), incorrectColumns.size(), rows, columns);
        sb.append("Completion: " + progress + "%");
        String error = sb.toString();
        return error;
    }

    public double getProgress(int incorrectRows, int incorrectColumns, int rows, int columns) {
        int totalCorrectRows = rows - incorrectRows;
        int totalCorrectColumns = columns - incorrectColumns;
        int totalCorrectLines = totalCorrectRows + totalCorrectColumns;
        int totalLines = rows + columns;

        double progress = (double) totalCorrectLines / totalLines;
        double progressPercent = progress * 100;
        // Rounds to 1 decimal place
        progressPercent = Math.round(progressPercent * Math.pow(10, 1)) / Math.pow(10, 1);
        return progressPercent;
    }
  
    // ******************************************************************************************************************************************
    // All methods below will most likely not be in the finished product, they are here to test the class and contain hard-coded grids and clues.
    // The grids below represent the players grid. These values can be changed to test the program to see if they match the clues.
    public int[][] createGrid(String name) {
        int[][] grid = null;
        if (name.equals("blanks_smiler")) {
            grid = new int[][] {
                {0, 0, 2, 2, 2, 2, 2, 0, 0, 0},
                {0, 2, 2, 2, 2, 2, 2, 2, 0, 0},
                {2, 2, 0, 2, 2, 2, 0, 2, 2, 0},
                {2, 2, 0, 2, 2, 2, 0, 2, 2, 0},
                {2, 2, 2, 2, 0, 2, 2, 2, 2, 0},
                {2, 0, 2, 2, 2, 2, 2, 0, 2, 0},
                {2, 2, 0, 2, 2, 2, 0, 2, 2, 0},
                {0, 2, 2, 0, 0, 0, 2, 2, 0, 0},
                {0, 0, 2, 2, 2, 2, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };
        }
        else if (name.equals("colour_wink")) {
            // yellow 3, black 0, empty 2
            grid = new int[][] {
                {0, 0, 3, 3, 3, 3, 3, 0, 0},
                {0, 3, 3, 3, 3, 3, 3, 3, 0},
                {3, 3, 2, 3, 3, 3, 3, 3, 3},
                {3, 3, 2, 3, 3, 3, 2, 3, 3},
                {3, 3, 3, 3, 3, 3, 3, 3, 3},
                {3, 3, 3, 3, 3, 3, 3, 3, 3},
                {3, 3, 2, 3, 3, 3, 2, 3, 3},
                {2, 3, 3, 2, 2, 2, 3, 3, 2},
                {0, 0, 3, 3, 3, 3, 3, 0, 0}
            };
        }
        return grid;
    }
  
    public ArrayList<Clue> createRowClues(String name) {
        ArrayList<Clue> rowClues = new ArrayList<Clue>();
        if (name.equals("blanks_smiler")) {
            Clue r1 = new Clue(new ArrayList<Integer>(Arrays.asList(5)), new ArrayList<Integer>(Arrays.asList(0)));
            Clue r2 = new Clue(new ArrayList<Integer>(Arrays.asList(7)), new ArrayList<Integer>(Arrays.asList(0)));
            Clue r3 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 3, 2)), new ArrayList<Integer>(Arrays.asList(0, 0, 0)));
            Clue r4 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 3, 2)), new ArrayList<Integer>(Arrays.asList(0, 0, 0)));
            Clue r5 = new Clue(new ArrayList<Integer>(Arrays.asList(9)), new ArrayList<Integer>(Arrays.asList(0)));
            Clue r6 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 5, 1)), new ArrayList<Integer>(Arrays.asList(0, 0, 0)));
            Clue r7 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 3, 2)), new ArrayList<Integer>(Arrays.asList(0, 0, 0)));
            Clue r8 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 2)), new ArrayList<Integer>(Arrays.asList(0, 0)));
            Clue r9 = new Clue(new ArrayList<Integer>(Arrays.asList(5)), new ArrayList<Integer>(Arrays.asList(0)));
            Clue r10 = new Clue(new ArrayList<Integer>(Arrays.asList(0)), new ArrayList<Integer>(Arrays.asList(0))); //   !
            rowClues.add(r1);
            rowClues.add(r2);
            rowClues.add(r3);
            rowClues.add(r4);
            rowClues.add(r5);
            rowClues.add(r6);
            rowClues.add(r7);
            rowClues.add(r8);
            rowClues.add(r9);
            rowClues.add(r10);
        }
        else if (name.equals("colour_wink")) {
            Clue r11 = new Clue(new ArrayList<>(Arrays.asList(5)), new ArrayList<>(Arrays.asList(3)));
            Clue r12 = new Clue(new ArrayList<>(Arrays.asList(7)), new ArrayList<>(Arrays.asList(3)));
            Clue r13 = new Clue(new ArrayList<>(Arrays.asList(2, 1, 6)), new ArrayList<>(Arrays.asList(3, 0, 3)));
            Clue r14 = new Clue(new ArrayList<>(Arrays.asList(2, 1, 3, 1, 2)), new ArrayList<>(Arrays.asList(3, 0, 3, 0, 3)));
            Clue r15 = new Clue(new ArrayList<>(Arrays.asList(9)), new ArrayList<>(Arrays.asList(3)));
            Clue r16 = new Clue(new ArrayList<>(Arrays.asList(9)), new ArrayList<>(Arrays.asList(3)));
            Clue r17 = new Clue(new ArrayList<>(Arrays.asList(2, 1, 3, 1, 2)), new ArrayList<>(Arrays.asList(3, 0, 3, 0, 3)));
            Clue r18 = new Clue(new ArrayList<>(Arrays.asList(2, 3, 2)), new ArrayList<>(Arrays.asList(3, 0, 3)));
            Clue r19 = new Clue(new ArrayList<>(Arrays.asList(5)), new ArrayList<>(Arrays.asList(3)));
            rowClues.add(r11);
            rowClues.add(r12);
            rowClues.add(r13);
            rowClues.add(r14);
            rowClues.add(r15);
            rowClues.add(r16);
            rowClues.add(r17);
            rowClues.add(r18);
            rowClues.add(r19);
        }
        return rowClues;
    }

    public ArrayList<Clue> createColumnClues(String name) {
        ArrayList<Clue> columnClues = new ArrayList<Clue>();
        if (name.equals("blanks_smiler")) {
            Clue c1 = new Clue(new ArrayList<>(Arrays.asList(5)), new ArrayList<>(Arrays.asList(0)));
            Clue c2 = new Clue(new ArrayList<>(Arrays.asList(4, 2)), new ArrayList<>(Arrays.asList(0, 0)));
            Clue c3 = new Clue(new ArrayList<>(Arrays.asList(2, 2, 2)), new ArrayList<>(Arrays.asList(0, 0, 0)));
            Clue c4 = new Clue(new ArrayList<>(Arrays.asList(7, 1)), new ArrayList<>(Arrays.asList(0, 0)));
            Clue c5 = new Clue(new ArrayList<>(Arrays.asList(7, 1)), new ArrayList<>(Arrays.asList(0, 0)));
            Clue c6 = new Clue(new ArrayList<>(Arrays.asList(7, 1)), new ArrayList<>(Arrays.asList(0, 0)));
            Clue c7 = new Clue(new ArrayList<>(Arrays.asList(2, 2, 2)), new ArrayList<>(Arrays.asList(0, 0, 0)));
            Clue c8 = new Clue(new ArrayList<>(Arrays.asList(4, 2)), new ArrayList<>(Arrays.asList(0, 0)));
            Clue c9 = new Clue(new ArrayList<>(Arrays.asList(5)), new ArrayList<>(Arrays.asList(0)));
            Clue c10 = new Clue(new ArrayList<>(Arrays.asList(0)), new ArrayList<>(Arrays.asList(0))); //  !
            columnClues.add(c1);
            columnClues.add(c2);
            columnClues.add(c3);
            columnClues.add(c4);
            columnClues.add(c5);
            columnClues.add(c6);
            columnClues.add(c7);
            columnClues.add(c8);
            columnClues.add(c9);
            columnClues.add(c10);
        }
        else if (name.equals("colour_wink")) {
            Clue c10 = new Clue(new ArrayList<>(Arrays.asList(5)), new ArrayList<>(Arrays.asList(3)));
            Clue c11 = new Clue(new ArrayList<>(Arrays.asList(7)), new ArrayList<>(Arrays.asList(3)));
            Clue c12 = new Clue(new ArrayList<>(Arrays.asList(2, 2, 2, 1, 2)), new ArrayList<>(Arrays.asList(3, 0, 3, 0, 3)));
            Clue c13 = new Clue(new ArrayList<>(Arrays.asList(7, 1, 1)), new ArrayList<>(Arrays.asList(3, 0, 3)));
            Clue c14 = new Clue(new ArrayList<>(Arrays.asList(7, 1, 1)), new ArrayList<>(Arrays.asList(3, 0, 3)));
            Clue c15 = new Clue(new ArrayList<>(Arrays.asList(7, 1, 1)), new ArrayList<>(Arrays.asList(3, 0, 3)));
            Clue c16 = new Clue(new ArrayList<>(Arrays.asList(3, 1, 2, 1, 2)), new ArrayList<>(Arrays.asList(3, 0, 3, 0, 3)));
            Clue c17 = new Clue(new ArrayList<>(Arrays.asList(7)), new ArrayList<>(Arrays.asList(3)));
            Clue c18 = new Clue(new ArrayList<>(Arrays.asList(5)), new ArrayList<>(Arrays.asList(3)));
            columnClues.add(c10);
            columnClues.add(c11);
            columnClues.add(c12);
            columnClues.add(c13);
            columnClues.add(c14);
            columnClues.add(c15);
            columnClues.add(c16);
            columnClues.add(c17);
            columnClues.add(c18);
        }
        return columnClues;
    }
}