import java.util.ArrayList;

public class Checker {
    /**
     * Loops through the number of rows and columns, and calls the checkLine() method for that given row or column, and for the corresponding clue.
     * Any incorrect row is added to an ArrayList (and same for a column), and these are added to another ArrayList and is returned.
     * @param grid              The player's nonogram.
     * @param rowClues          List of row clues.
     * @param columnClues       List of column clues.
     * @return                  List of lists of all incorrect rows and columns (or empty otherwise)
     */
    public ArrayList<ArrayList<Integer>> checkNonogram(int[][] grid, ArrayList<Clue> rowClues, ArrayList<Clue> columnClues) {
        // Where incorrect rows and columns are stored.
        ArrayList<Integer> incorrectRows = new ArrayList<Integer>();
        ArrayList<Integer> incorrectColumns = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> incorrectRowsAndColumns = new ArrayList<ArrayList<Integer>>();

        // For each row in the 2D array grid, check the ith row against the ith clue. If it is not correct, add to the corresponding list.
        for (int i = 0; i < grid.length; i++) {
            boolean correctRow = checkLine(grid[i], rowClues.get(i));
            if (!correctRow) {
                incorrectRows.add(i);
            }
        }
        for (int j = 0; j < grid[0].length; j++) {
            // Get the ith column, and check it against the ith clue.
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
     * @return              True or false, depending on whether the line met the constraints of the clue.
     */
    public boolean checkLine(int[] line, Clue clue) {
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
        for (int j = 0; j < counts.size(); j++) {
            numCorrect = 0;
            numSquares = counts.get(j);
            squareColour = colours.get(j);
  
            // Loops through each cell in the line, starting from 0.
            for (int i = positionInLine; i < line.length; i++) {
                // Checks if the number of squares matches the clue count, which signals that that part of the clue is complete.
                if (numCorrect == numSquares) {
                    if (line[i] != squareColour) { // The cell MUST be a different colour, otherwise the clue has not been satisfied.
                        if (j != counts.size() - 1) {
                            break; // Break only if there are still parts of the clue left to be checked.
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
                //  or if it is blank or unknown but the part of the clue has not been finished yet.
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
     * for a passed in column number by looping through the length of the column (i.e. the number of rows, or the length of the 2D array)
     * @param grid          The 2D array player grid
     * @param num           The column number
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

    /**
     * Creates the String which has the number of rows and columns which are incorrect (if any) and the user's current progress for their grid.
     * @param incorrectLines        The list of lists of incorrect rows and columns, obtained from checkNonogram().
     * @param rows                  The number of rows the grid has (also the length of the row clues list) which is used for calculating the progress.
     * @param columns               The number of columns the grid has (also the length of the column clues list) which is used for calculating the progress.
     * @return                      The String which is output to the user when they have checked their puzzle.
     */
    public String getMessage(ArrayList<ArrayList<Integer>> incorrectLines, int rows, int columns) {
        StringBuilder sb = new StringBuilder();
        ArrayList<Integer> incorrectRows = incorrectLines.get(0);
        ArrayList<Integer> incorrectColumns = incorrectLines.get(1);

        // If there are no incorrect rows or lines, add the appropriate message/s.
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

        // Loop through every incorrect row and column and add this to the message.
        for (int rowIndex : incorrectRows) {
            int rowNum = rowIndex + 1;
            sb.append("Incorrect row " + rowNum + "\n");
        }
        for (int columnIndex : incorrectColumns) {
            int columnNum = columnIndex + 1;
            sb.append("Incorrect column " + columnNum + "\n");
        }

        // Calculate the progress and add this to the message.
        double progress = getProgress(incorrectRows.size(), incorrectColumns.size(), rows, columns);
        sb.append("Completion: " + progress + "%");
        String error = sb.toString();
        return error;
    }

    /**
     * Calculates how many rows and columns the user has correct for a given grid.
     * @param incorrectRows             The number of incorrect rows.
     * @param incorrectColumns          The number of incorrect columns.
     * @param rows                      The total number of rows.
     * @param columns                   The total number of columns.
     * @return                          The percentage of completion of the user's grid.
     */
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