import java.util.ArrayList;

public class Checker {
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
     * @return              List of coordinates of incorrect cells.
     */
    public boolean checkLine(int[] line, Clue clue) {
        // For global access within the method
        ArrayList<Integer> counts = clue.getCounts();       // Gets the list of counts for this clue.
        ArrayList<Integer> colours = clue.getColours();     // Gets the list of colours for this clue, given as integers.
        int numCorrect = 0;         // Keeps track of the number of cells which are correct for a given clue.
        int positionInLine = 0;      // Keeps track of the position in the row, i.e. the cell number.
        int numSquares = 0;         // This is the count clue, i.e. how many squares are a given colour.
        int squareColour = 0;       // This is the square colour.

        // If line is blank (i.e. count is 0) then check each cell and make sure they are unknown or empty.
        if (counts.size() == 0) {
            for (int k = 0; k < line.length; k++) {
                if (line[k] != 1 && line[k] != 0) {
                    return false;
                }
            }
            return true;
        }

        // Loops through the size of the counts list. The length of the counts and colours lists must be the same.
        // Example counts list: [5, 3, 2] which is 5 squares, a space, 3 squares, a space, 2 squares, a space.
        for (int j = 0; j < counts.size(); j++) {
            numCorrect = 0;
            numSquares = counts.get(j);
            squareColour = colours.get(j);
  
            // Loops through each cell in the line, starting from 0.
            for (int i = positionInLine; i < line.length; i++) {
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
                positionInLine++;
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
        int length = grid.length;
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
}