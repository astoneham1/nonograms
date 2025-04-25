import java.util.ArrayList;
import java.util.Arrays;

public class TestChecker {
    public static void main(String[] args) {
        Checker c = new Checker();
        TestChecker tc = new TestChecker();

        // Get the grid, clues and message for blanks smiler where the user has successfully completed it and all other cells are 'unknown' and print the test.
        int[][] blanksSmiler = tc.createGrid("blanks_smiler");
        ArrayList<Clue> rowCluesBlankSmiler = tc.createRowClues("blanks_smiler");
        ArrayList<Clue> columnCluesBlankSmiler = tc.createColumnClues("blanks_smiler");
        ArrayList<ArrayList<Integer>> blankSmilerCheck = c.checkNonogram(blanksSmiler, rowCluesBlankSmiler, columnCluesBlankSmiler);
        String blanksSmilerMessage = c.getMessage(blankSmilerCheck, rowCluesBlankSmiler.size(), columnCluesBlankSmiler.size());
        String testOneStatus = tc.checkTestStatus(1, blankSmilerCheck, blanksSmilerMessage);
        System.out.println("\u001B[94mTEST ONE:\u001B[0m Player has the correct blanks_smiler grid (All constraints met) and the rest of the cells are marked as 'unknown'.");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m\n" + blankSmilerCheck.get(0) + blankSmilerCheck.get(1) + "\n" + blanksSmilerMessage);
        System.out.println("\u001B[94mTEST STATUS:" + testOneStatus + "\n");

        // Get the grid, clues and message for blanks smiler where the user has successfully completed it and all other cells are 'empty' and print the test.
        int[][] blanksSmilerEmpty = tc.createGrid("blanks_smiler_empty");
        ArrayList<Clue> rowCluesBlanksSmilerEmpty = tc.createRowClues("blanks_smiler_empty");
        ArrayList<Clue> columnCluesBlanksSmilerEmpty = tc.createColumnClues("blanks_smiler_empty");
        ArrayList<ArrayList<Integer>> blanksSmilerEmptyCheck = c.checkNonogram(blanksSmilerEmpty, rowCluesBlanksSmilerEmpty, columnCluesBlanksSmilerEmpty);
        String blanksSmilerEmptyMessage = c.getMessage(blanksSmilerEmptyCheck, rowCluesBlanksSmilerEmpty.size(), columnCluesBlanksSmilerEmpty.size());
        String testTwoStatus = tc.checkTestStatus(2, blanksSmilerEmptyCheck, blanksSmilerEmptyMessage);
        System.out.println("\u001B[94mTEST TWO:\u001B[0m Player has the correct blanks_smiler grid (All constraints met) and the rest of the cells are marked as 'empty'. ");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m\n" + blanksSmilerEmptyCheck.get(0) + blanksSmilerEmptyCheck.get(1) + "\n" + blanksSmilerEmptyMessage);
        System.out.println("\u001B[94mTEST STATUS:" + testTwoStatus + "\n");

        // Get the grid, clues and message for unsolvable smiler where the user has successfully completed it and print the test.
        int[][] unsolvableSmiler = tc.createGrid("unsolvable_smiler");
        ArrayList<Clue> rowCluesUnsolvableSmiler = tc.createRowClues("unsolvable_smiler");
        ArrayList<Clue> columnCluesUnsolvableSmiler = tc.createColumnClues("unsolvable_smiler");
        ArrayList<ArrayList<Integer>> unsolvableSmilerCheck = c.checkNonogram(unsolvableSmiler, rowCluesUnsolvableSmiler, columnCluesUnsolvableSmiler);
        String unsolvableSmilerMessage = c.getMessage(unsolvableSmilerCheck, rowCluesUnsolvableSmiler.size(), columnCluesUnsolvableSmiler.size());
        String testThreeStatus = tc.checkTestStatus(3, unsolvableSmilerCheck, unsolvableSmilerMessage);
        System.out.println("\u001B[94mTEST THREE:\u001B[0m Player has attempted to solve the unsolvable_smiler.");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m\n" + unsolvableSmilerCheck.get(0) + unsolvableSmilerCheck.get(1) + "\n" + unsolvableSmilerMessage);
        System.out.println("\u001B[94mTEST STATUS:" + testThreeStatus + "\n");

        // Get the grid, clues and message for the unsolvable smiler where the user has attempted to fill the final row by colouring the bottom-right cell
        // (the row has no constraint), and print the test.
        int[][] unsolvableSmiler2 = tc.createGrid("unsolvable_smiler_2");
        ArrayList<Clue> rowCluesUnsolvableSmiler2 = tc.createRowClues("unsolvable_smiler_2");
        ArrayList<Clue> columnCluesUnsolvableSmiler2 = tc.createColumnClues("unsolvable_smiler_2");
        ArrayList<ArrayList<Integer>> unsolvableSmiler2Check = c.checkNonogram(unsolvableSmiler2, rowCluesUnsolvableSmiler2, columnCluesUnsolvableSmiler2);
        String unsolvableSmiler2Message = c.getMessage(unsolvableSmiler2Check, rowCluesUnsolvableSmiler2.size(), columnCluesUnsolvableSmiler2.size());
        String testFourStatus = tc.checkTestStatus(4, unsolvableSmiler2Check, unsolvableSmiler2Message);
        System.out.println("\u001B[94mTEST FOUR:\u001B[0m Player has attempted to solve the unsolvable_smiler and has (row 10, column 10) filled.");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m\n" + unsolvableSmiler2Check.get(0) + unsolvableSmiler2Check.get(1) + "\n" + unsolvableSmiler2Message);
        System.out.println("\u001B[94mTEST STATUS:" + testFourStatus + "\n");

        // Get the grid, clues and message for the colour wink where the user has completed it and print the test.
        int[][] colourWink = tc.createGrid("colour_wink");
        ArrayList<Clue> rowCluesColourWink = tc.createRowClues("colour_wink");
        ArrayList<Clue> columnCluesColourWink = tc.createColumnClues("colour_wink");
        ArrayList<ArrayList<Integer>> colourWinkCheck = c.checkNonogram(colourWink, rowCluesColourWink, columnCluesColourWink);
        String colourWinkMessage = c.getMessage(colourWinkCheck, rowCluesColourWink.size(), columnCluesColourWink.size());
        String testFiveStatus = tc.checkTestStatus(5, colourWinkCheck, colourWinkMessage);
        System.out.println("\u001B[94mTEST FIVE:\u001B[0m Player has the correct colour_wink grid (All constraints met).");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m\n" + colourWinkCheck.get(0) + colourWinkCheck.get(1) + "\n" + colourWinkMessage);
        System.out.println("\u001B[94mTEST STATUS:" + testFiveStatus + "\n");

        // Get the grid, clues and message for the multi checks where the user has successfully completed it and print the test.
        int[][] multiChecks1 = tc.createGrid("multi_checks_1");
        ArrayList<Clue> rowCluesMultiChecks1 = tc.createRowClues("multi_checks_1");
        ArrayList<Clue> columnCluesMultiChecks1 = tc.createColumnClues("multi_checks_1");
        ArrayList<ArrayList<Integer>> multiChecks1Check = c.checkNonogram(multiChecks1, rowCluesMultiChecks1, columnCluesMultiChecks1);
        String multiChecks1Message = c.getMessage(multiChecks1Check, rowCluesMultiChecks1.size(), columnCluesMultiChecks1.size());
        String testSixStatus = tc.checkTestStatus(6, multiChecks1Check, multiChecks1Message);
        System.out.println("\u001B[94mTEST SIX:\u001B[0m Player has the correct multi_checks grid (All constraints met).");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m\n" + multiChecks1Check.get(0) + multiChecks1Check.get(1) + "\n" + multiChecks1Message);
        System.out.println("\u001B[94mTEST STATUS:" + testSixStatus + "\n");

        // Get the grid, clue and message for multi checks where the user has successfully completed it and print the test.
        // Since the constraints are the same but the solution is different this tests the checker can handle multiple correct solutions.
        int[][] multiChecks2 = tc.createGrid("multi_checks_2");
        ArrayList<Clue> rowCluesMultiChecks2 = tc.createRowClues("multi_checks_2");
        ArrayList<Clue> columnCluesMultiChecks2 = tc.createColumnClues("multi_checks_2");
        ArrayList<ArrayList<Integer>> multiChecks2Check = c.checkNonogram(multiChecks2, rowCluesMultiChecks2, columnCluesMultiChecks2);
        String multiChecks2Message = c.getMessage(multiChecks2Check, rowCluesMultiChecks2.size(), columnCluesMultiChecks2.size());
        String testSevenStatus = tc.checkTestStatus(7, multiChecks2Check, multiChecks2Message);
        System.out.println("\u001B[94mTEST SEVEN:\u001B[0m Player has the correct multi_checks grid (All constraints met).");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m\n" + multiChecks2Check.get(0) + multiChecks2Check.get(1) + "\n" + multiChecks2Message);
        System.out.println("\u001B[94mTEST STATUS:" + testSevenStatus + "\n");

        // Get the grid, clues and message for colour cat where the user has successfully completed it and print the test.
        int[][] colourCat = tc.createGrid("colour_cat");
        ArrayList<Clue> rowCluesColourCat = tc.createRowClues("colour_cat");
        ArrayList<Clue> columnCluesColourCat = tc.createColumnClues("colour_cat");
        ArrayList<ArrayList<Integer>> colourCatCheck = c.checkNonogram(colourCat, rowCluesColourCat, columnCluesColourCat);
        String colourCatMessage = c.getMessage(colourCatCheck, rowCluesColourCat.size(), columnCluesColourCat.size());
        String testEightStatus = tc.checkTestStatus(8, colourCatCheck, colourCatMessage);
        System.out.println("\u001B[94mTEST EIGHT:\u001B[0m Player has the correct colour_cat grid (All constraints met).");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m\n" + colourCatCheck.get(0) + colourCatCheck.get(1) + "\n" + colourCatMessage);
        System.out.println("\u001B[94mTEST STATUS:" + testEightStatus + "\n");

        // Get the grid, clues and message for cat where the user has made one error, and print the test.
        int[][] incorrectCat = tc.createGrid("incorrect_cat");
        ArrayList<Clue> rowCluesIncorrectCat = tc.createRowClues("incorrect_cat");
        ArrayList<Clue> columnCluesIncorrectCat = tc.createColumnClues("incorrect_cat");
        ArrayList<ArrayList<Integer>> incorrectCatCheck = c.checkNonogram(incorrectCat, rowCluesIncorrectCat, columnCluesIncorrectCat);
        String incorrectCatMessage = c.getMessage(incorrectCatCheck, rowCluesIncorrectCat.size(), columnCluesIncorrectCat.size());
        String testNineStatus = tc.checkTestStatus(9, incorrectCatCheck, incorrectCatMessage);
        System.out.println("\u001B[94mTEST NINE:\u001B[0m Player has an incorrect cat grid with one error in row 2, column 7.");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m\n" + incorrectCatCheck.get(0) + incorrectCatCheck.get(1) + "\n" + incorrectCatMessage);
        System.out.println("\u001B[94mTEST STATUS:" + testNineStatus + "\n");

        // Get the grid, clues and message for cat where the user has made multiple different errors, and print the test.
        int[][] multipleIncorrectCat = tc.createGrid("multiple_incorrect_cat");
        ArrayList<Clue> rowCluesMultipleIncorrectCat = tc.createRowClues("multiple_incorrect_cat");
        ArrayList<Clue> columnCluesMultipleIncorrectCat = tc.createColumnClues("multiple_incorrect_cat");
        ArrayList<ArrayList<Integer>> multipleIncorrectCatCheck = c.checkNonogram(multipleIncorrectCat, rowCluesMultipleIncorrectCat, columnCluesMultipleIncorrectCat);
        String multipleIncorrectCatMessage = c.getMessage(multipleIncorrectCatCheck, rowCluesMultipleIncorrectCat.size(), columnCluesMultipleIncorrectCat.size());
        String testTenStatus = tc.checkTestStatus(10, multipleIncorrectCatCheck, multipleIncorrectCatMessage);
        System.out.println("\u001B[94mTEST TEN:\u001B[0m Player has an incorrect cat grid with multiple errors in rows 1, 4, 8, 10 and columns 3, 5, 6, 9.");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m\n" + multipleIncorrectCatCheck.get(0) + multipleIncorrectCatCheck.get(1) + "\n" + multipleIncorrectCatMessage);
        System.out.println("\u001B[94mTEST STATUS:" + testTenStatus + "\n");
    
        // Get the grid, clues and message for cat where the user has not filled any of the grid in correctly, and print the test.
        int[][] blankGridCat = tc.createGrid("blank_grid_cat");
        ArrayList<Clue> rowCluesBlankGridCat = tc.createRowClues("blank_grid_cat");
        ArrayList<Clue> columnCluesBlankGridCat = tc.createColumnClues("blank_grid_cat");
        ArrayList<ArrayList<Integer>> blankGridCatCheck = c.checkNonogram(blankGridCat, rowCluesBlankGridCat, columnCluesBlankGridCat);
        String blankGridCatMessage = c.getMessage(blankGridCatCheck, rowCluesBlankGridCat.size(), columnCluesBlankGridCat.size());
        String testElevenStatus = tc.checkTestStatus(11, blankGridCatCheck, blankGridCatMessage);
        System.out.println("\u001B[94mTEST ELEVEN:\u001B[0m Player has an incorrect cat grid which is completely blank (filled with unknown and empty cells only). All constraints failed.");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m\n" + blankGridCatCheck.get(0) + blankGridCatCheck.get(1) + "\n" + blankGridCatMessage);
        System.out.println("\u001B[94mTEST STATUS:" + testElevenStatus + "\n");

        // Create a line and a clue, and get the message, where the user has entered too many cells (i.e. the counts are wrong), and print the test.
        int[] errorType1 = {2, 2, 2, 2, 2, 2}; // Clue is 5 but found 6
        Clue clueErrorType1 = new Clue(new ArrayList<Integer>(Arrays.asList(5)), new ArrayList<Integer>(Arrays.asList(2)));
        Boolean errorType1Status = c.checkLine(errorType1, clueErrorType1);
        String testTwelveStatus = tc.checkTestStatus(12, errorType1Status);
        System.out.println("\u001B[94mTEST TWELVE:\u001B[0m The line has 6 squares instead of 5 like the clue states. False should be returned since the line is wrong.");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m " + errorType1Status + "\n\u001B[94mTEST STATUS:" + testTwelveStatus + "\n");
    
        // Create a line and a clue, and get the message, where the user has the correct quantity of cells but in the wrong arrangement, and print the test.
        int[] errorType2 = {2, 2, 0, 2, 2, 2}; // Clue is 5 but found 2, 3
        Clue clueErrorType2 = new Clue(new ArrayList<Integer>(Arrays.asList(5)), new ArrayList<Integer>(Arrays.asList(2)));
        Boolean errorType2Status = c.checkLine(errorType2, clueErrorType2);
        String testThirteenStatus = tc.checkTestStatus(13, errorType2Status);
        System.out.println("\u001B[94mTEST THIRTEEN:\u001B[0m The line has 2 squares, a space (unknown), and 3 squares instead of 5 like the clue states.");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m " + errorType2Status + "\n\u001B[94mTEST STATUS:" + testThirteenStatus + "\n");
    
        // Create a line and a clue, and get the message, where the user has the correct count but the wrong colour, and print the test.
        int[] errorType3 = {3, 3, 3, 3, 3}; // Clue is 5 in colour 2 but found 5 in colour 3
        Clue clueErrorType3 = new Clue(new ArrayList<Integer>(Arrays.asList(5)), new ArrayList<Integer>(Arrays.asList(2)));
        Boolean errorType3Status = c.checkLine(errorType3, clueErrorType3);
        String testFourteenStatus = tc.checkTestStatus(14, errorType3Status);
        System.out.println("\u001B[94mTEST FOURTEEN:\u001B[0m The line has 5 squares in colour 3 instead of in colour 2 like the clue states.");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m " + errorType3Status + "\n\u001B[94mTEST STATUS:" + testFourteenStatus + "\n");
    
        // Create a line and a clue, and get the message, where the user has the incorrect count and colour, and print the test.
        int[] errorType4 = {3, 3, 0, 3, 3, 3}; // Clue is 5 in colour 2 but found 2, 3 in colour 3
        Clue clueErrorType4 = new Clue(new ArrayList<Integer>(Arrays.asList(5)), new ArrayList<Integer>(Arrays.asList(2)));
        Boolean errorType4Status = c.checkLine(errorType4, clueErrorType4);
        String testFifteenStatus = tc.checkTestStatus(15, errorType4Status);
        System.out.println("\u001B[94mTEST FIFTEEN:\u001B[0m The line has 2 squares in colour 3, a space (unknown), and 3 squares in colour 3 instead of 5 squares in colour 2 like the clue states.");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m " + errorType4Status + "\n\u001B[94mTEST STATUS:" + testFifteenStatus + "\n");
    
        // Create a line and a clue, and get the message where the user has the correct quantity but did not include a space (unknown/empty), and print the test.
        int[] errorType5 = {2, 2, 2, 2, 2, 2}; // Clue is 4, 2 but found 6
        Clue clueErrorType5 = new Clue(new ArrayList<Integer>(Arrays.asList(4, 2)), new ArrayList<Integer>(Arrays.asList(2, 2)));
        Boolean errorType5Status = c.checkLine(errorType5, clueErrorType5);
        String testSixteenStatus = tc.checkTestStatus(16, errorType5Status);
        System.out.println("\u001B[94mTEST SIXTEEN:\u001B[0m The line has 6 squares instead of 4 squares, a space, 2 squares like the clue states.");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m " + errorType5Status + "\n\u001B[94mTEST STATUS:" + testSixteenStatus + "\n");
    
        // Create a line and a clue, and get the message where the user has the met the constraints for a clue with one count, and print the test.
        int[] correctLine1 = {2, 2, 2, 2};
        Clue clueCorrectLine1 = new Clue(new ArrayList<Integer>(Arrays.asList(4)), new ArrayList<Integer>(Arrays.asList(2)));
        Boolean correctLine1Status = c.checkLine(correctLine1, clueCorrectLine1);
        String testSeventeenStatus = tc.checkTestStatus(17, correctLine1Status);
        System.out.println("\u001B[94mTEST SEVENTEEN:\u001B[0m The line has 4 squares in colour 2, which meets the constraint.");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m " + correctLine1Status + "\n\u001B[94mTEST STATUS:" + testSeventeenStatus + "\n");

        // Create a line and a clue, and get the message, where the user has met the constraints for a clue with multiple counts and colours.
        int[] correctLine2 = {2, 2, 2, 3, 3};
        Clue clueCorrectLine2 = new Clue(new ArrayList<Integer>(Arrays.asList(3, 2)), new ArrayList<Integer>(Arrays.asList(2, 3)));
        Boolean correctLine2Status = c.checkLine(correctLine2, clueCorrectLine2);
        String testEighteenStatus = tc.checkTestStatus(18, correctLine2Status);
        System.out.println("\u001B[94mTEST EIGHTEEN:\u001B[0m The line has 3 squares in colour 2 and 2 squares in colour 3, which meets the constraint.");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m " + correctLine2Status + "\n\u001B[94mTEST STATUS:" + testEighteenStatus + "\n");
    
        // Create a line and a clue, and get the message, where the user has met the constraints for a clue with an (optional) empty cell in between,
        // since the colours are different there does not need to be a space here.
        int[] correctLine3 = {2, 2, 2, 1, 3, 3};
        Clue clueCorrectLine3 = new Clue(new ArrayList<Integer>(Arrays.asList(3, 2)), new ArrayList<Integer>(Arrays.asList(2, 3)));
        Boolean correctLine3Status = c.checkLine(correctLine3, clueCorrectLine3);
        String testNineteenStatus = tc.checkTestStatus(19, correctLine3Status);
        System.out.println("\u001B[94mTEST NINETEEN:\u001B[0m The line has 3 squares in colour 2, a space (empty) and 2 squares in colour 3, which meets the constraint.");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m " + correctLine3Status + "\n\u001B[94mTEST STATUS:" + testNineteenStatus + "\n");
    
        // Create a line and a clue, and get the message, where the user has met the constraints for a clue with multiple (optional) unknown/empty cells in between.
        int[] correctLine4 = {2, 2, 2, 0, 1, 0, 3, 3};
        Clue clueCorrectLine4 = new Clue(new ArrayList<Integer>(Arrays.asList(3, 2)), new ArrayList<Integer>(Arrays.asList(2, 3)));
        Boolean correctLine4Status = c.checkLine(correctLine4, clueCorrectLine4);
        String testTwentyStatus = tc.checkTestStatus(20, correctLine4Status);
        System.out.println("\u001B[94mTEST TWENTY:\u001B[0m The line has 3 squares in colour 2, 3 spaces (unknown and empty cells only) and 2 squares in colour 3, which meets the constraint.");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m " + correctLine4Status + "\n\u001B[94mTEST STATUS:" + testTwentyStatus + "\n");

        // Create a line and a clue, and get the message, where the user has met the constraints for a clue with multiple counts but the same colour, i.e.
        // there must be atleast one unknown/empty cell in between them, and print the test.
        int[] correctLine5 = {2, 2, 0, 2, 2, 2};
        Clue clueCorrectLine5 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 3)), new ArrayList<Integer>(Arrays.asList(2, 2)));
        Boolean correctLine5Status = c.checkLine(correctLine5, clueCorrectLine5);
        String testTwentyOneStatus = tc.checkTestStatus(21, correctLine5Status);
        System.out.println("\u001B[94mTEST TWENTY ONE:\u001B[0m The line has 2 squares in colour 2, 1 unknown, and 1 square in colour 2, which meets the constraint.");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m " + correctLine5Status + "\n\u001B[94mTEST STATUS:" + testTwentyOneStatus + "\n");
    
        // Create a grid and get the columns, and check if they are the expected columns, and print the test.
        int[][] testGetColumn = tc.createGrid("test_get_column");
        int[] column1 = c.getColumn(testGetColumn, 0);
        int[] column2 = c.getColumn(testGetColumn, 1);
        int[] column3 = c.getColumn(testGetColumn, 2);
        String testTwentyTwoStatus = tc.checkTestStatus(column1, column2, column3);
        System.out.println("\u001B[94mTEST TWENTY TWO:\u001B[0m Tests the columns are successfully extracted from the 2D array grid.");
        System.out.println("\u001B[94mTEST OUTPUT:\n\u001B[0m" + tc.getArrayString(column1) + "\n" + tc.getArrayString(column2) + "\n" + tc.getArrayString(column3));
        System.out.println("\u001B[94mTEST STATUS:" + testTwentyTwoStatus + "\n");

        // Create the number of lines and incorrect lines and check if the progress calculation is correct, and print the test.
        int incorrectRowsWhole = 4;
        int incorrectColumnsWhole = 4;
        int totalRowsWhole = 8;
        int totalColumnsWhole = 8;
        double progressWhole = c.getProgress(incorrectRowsWhole, incorrectRowsWhole, totalRowsWhole, totalColumnsWhole);
        String testTwentyThreeStatus = tc.checkTestStatus(1, progressWhole);
        System.out.println("\u001B[94mTEST TWENTY THREE:\u001B[0m The progress for 16 total lines where 8 are incorrect (and so 8 are correct).");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m " + progressWhole + "\n\u001B[94mTEST STATUS:" + testTwentyThreeStatus + "\n");

        // Create the number of lines and incorrect lines and check if the progress calculation is correct, and print the test.
        int incorrectRowsDecimal = 2;
        int incorrectColumnsDecimal = 3;
        int totalRowsDecimal = 14;
        int totalColumnsDecimal = 12;
        double progressDecimal = c.getProgress(incorrectRowsDecimal, incorrectColumnsDecimal, totalRowsDecimal, totalColumnsDecimal);
        String testTwentyFourStatus = tc.checkTestStatus(2, progressDecimal);
        System.out.println("\u001B[94mTEST TWENTY FOUR:\u001B[0m The progress for 26 total lines where 5 are incorrect (and so 21 are correct).");
        System.out.println("\u001B[94mTEST OUTPUT:\u001B[0m " + progressDecimal + "\n\u001B[94mTEST STATUS:" + testTwentyFourStatus + "\u001B[0m");
    }

    // Retrieve the required grid depending on the test case.
    public int[][] createGrid(String name) {
        int[][] grid = null;
        if (name.equals("blanks_smiler")) {
            grid = new int[][] { // can also do 1 instead of 0 to show doesnt matter if unkown or empty
                {0, 0, 2, 2, 2, 2, 2, 0, 0, 0},
                {0, 2, 2, 2, 2, 2, 2, 2, 0, 0},
                {2, 2, 0, 2, 2, 2, 0, 2, 2, 0},
                {2, 2, 0, 2, 2, 2, 0, 2, 2, 0},
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 0},
                {2, 0, 2, 2, 2, 2, 2, 0, 2, 0},
                {2, 2, 0, 2, 2, 2, 0, 2, 2, 0},
                {0, 2, 2, 0, 0, 0, 2, 2, 0, 0},
                {0, 0, 2, 2, 2, 2, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };
        }
        else if (name.equals("blanks_smiler_empty")) {
            grid = new int[][] {
                {1, 1, 2, 2, 2, 2, 2, 1, 1, 1},
                {1, 2, 2, 2, 2, 2, 2, 2, 1, 1},
                {2, 2, 1, 2, 2, 2, 1, 2, 2, 1},
                {2, 2, 1, 2, 2, 2, 1, 2, 2, 1},
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
                {2, 1, 2, 2, 2, 2, 2, 1, 2, 1},
                {2, 2, 1, 2, 2, 2, 1, 2, 2, 1},
                {1, 2, 2, 1, 1, 1, 2, 2, 1, 1},
                {1, 1, 2, 2, 2, 2, 2, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
            };
        }
        else if (name.equals("unsolvable_smiler")) {
            grid = new int[][] {
                {0, 0, 2, 2, 2, 2, 2, 0, 0, 0},
                {0, 2, 2, 2, 2, 2, 2, 2, 0, 0},
                {2, 2, 0, 2, 2, 2, 0, 2, 2, 0},
                {2, 2, 0, 2, 2, 2, 0, 2, 2, 0},
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 0},
                {2, 0, 2, 2, 2, 2, 2, 0, 2, 0},
                {2, 2, 0, 2, 2, 2, 0, 2, 2, 0},
                {0, 2, 2, 0, 0, 0, 2, 2, 0, 0},
                {0, 0, 2, 2, 2, 2, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };
        }
        else if (name.equals("unsolvable_smiler_2")) {
            grid = new int[][] {
                {0, 0, 2, 2, 2, 2, 2, 0, 0, 0},
                {0, 2, 2, 2, 2, 2, 2, 2, 0, 0},
                {2, 2, 0, 2, 2, 2, 0, 2, 2, 0},
                {2, 2, 0, 2, 2, 2, 0, 2, 2, 0},
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 0},
                {2, 0, 2, 2, 2, 2, 2, 0, 2, 0},
                {2, 2, 0, 2, 2, 2, 0, 2, 2, 0},
                {0, 2, 2, 0, 0, 0, 2, 2, 0, 0},
                {0, 0, 2, 2, 2, 2, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 2}
            };
        }
        else if (name.equals("colour_wink")) {
            grid = new int[][] {
                {0, 0, 3, 3, 3, 3, 3, 0, 0},
                {0, 3, 3, 3, 3, 3, 3, 3, 0},
                {3, 3, 2, 3, 3, 3, 3, 3, 3},
                {3, 3, 2, 3, 3, 3, 2, 3, 3},
                {3, 3, 3, 3, 3, 3, 3, 3, 3},
                {3, 3, 3, 3, 3, 3, 3, 3, 3},
                {3, 3, 2, 3, 3, 3, 2, 3, 3},
                {0, 3, 3, 2, 2, 2, 3, 3, 0},
                {0, 0, 3, 3, 3, 3, 3, 0, 0}
            };
        }
        else if (name.equals("multi_checks_1")) {
            grid = new int[][] {
                {2, 0, 2, 0, 2, 0},
                {0, 2, 0, 2, 0, 2},
                {2, 0, 2, 0, 2, 0},
                {0, 2, 0, 2, 0, 2},
                {2, 0, 2, 0, 2, 0},
                {0, 2, 0, 2, 0, 2}
            };
        }
        else if (name.equals("multi_checks_2")) {
            grid = new int[][] {
                {0, 2, 0, 2, 0, 2},
                {2, 0, 2, 0, 2, 0},
                {0, 2, 0, 2, 0, 2},
                {2, 0, 2, 0, 2, 0},
                {0, 2, 0, 2, 0, 2},
                {2, 0, 2, 0, 2, 0}
            };
        }
        else if (name.equals("colour_cat")) {
            grid = new int[][] {
                {0, 4, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 4, 4, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 4, 4, 5, 5, 5, 5, 5, 4, 4, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 4, 4, 5, 5, 5, 5, 5, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0},
                {0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 4, 4, 4, 5, 5, 0, 0, 0, 0, 0, 0},
                {4, 4, 3, 5, 5, 5, 5, 5, 3, 4, 4, 5, 5, 5, 4, 4, 4, 4, 4, 5, 5, 0, 0, 0, 0, 0},
                {0, 4, 3, 5, 5, 5, 5, 5, 3, 4, 5, 5, 5, 5, 4, 4, 4, 4, 4, 4, 5, 5, 5, 0, 0, 0},
                {4, 4, 4, 4, 5, 2, 5, 4, 4, 4, 4, 5, 5, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 0, 0},
                {0, 5, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 4, 4, 4, 4, 4, 4, 4, 0, 0, 5, 5, 5, 0},
                {0, 5, 5, 4, 4, 4, 4, 4, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 5, 5, 5},
                {0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 5, 5},
                {4, 4, 4, 4, 5, 5, 5, 5, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5},
                {4, 4, 4, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5},
                {0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4},
                {0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4}
            };
        }
        else if (name.equals("incorrect_cat")) {
            grid = new int[][] {
                {0, 2, 0, 0, 0, 2, 0, 0, 0, 0},
                {0, 2, 2, 0, 2, 2, 2, 0, 0, 0}, // Incorrect cell at (row 2, column 7) (taking the starting index as 1, not 0) it should be 0220220000
                {0, 2, 2, 2, 2, 2, 0, 0, 0, 0},
                {0, 2, 0, 2, 0, 2, 0, 0, 0, 0},
                {2, 2, 2, 2, 2, 2, 2, 0, 0, 0},
                {0, 2, 2, 2, 2, 2, 0, 0, 2, 2},
                {0, 0, 2, 2, 2, 0, 0, 0, 0, 2},
                {0, 0, 2, 2, 2, 0, 0, 0, 0, 2},
                {0, 0, 2, 2, 2, 2, 0, 0, 2, 2},
                {0, 0, 2, 2, 2, 2, 2, 2, 2, 0}
            };
        }
        else if (name.equals("multiple_incorrect_cat")) {
            grid = new int[][] {
                {0, 2, 0, 0, 0, 2, 0, 0, 2, 0}, // Incorrect cell at (row 1, column 9)
                {0, 2, 2, 0, 2, 2, 0, 0, 0, 0},
                {0, 2, 2, 2, 2, 2, 0, 0, 0, 0},
                {0, 2, 2, 2, 0, 2, 0, 0, 0, 0}, // Incorrect cell at (row 4, column 3)
                {2, 2, 2, 2, 2, 2, 2, 0, 0, 0},
                {0, 2, 2, 2, 2, 2, 0, 0, 2, 2},
                {0, 0, 2, 2, 2, 0, 0, 0, 0, 2},
                {0, 0, 2, 2, 0, 0, 0, 0, 0, 2}, // Incorrect cell at (row 8, column 5)
                {0, 0, 2, 2, 2, 2, 0, 0, 2, 2},
                {0, 0, 2, 2, 2, 0, 2, 2, 2, 0}  // Incorrect cell at (row 10, column 6)
            };
        }
        else if (name.equals("blank_grid_cat")) {
            grid = new int[][] {
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
                {0, 1, 0, 0, 1, 0, 0, 1, 0, 0},
                {1, 0, 1, 0, 0, 1, 0, 1, 0, 0},
                {0, 1, 0, 0, 1, 0, 1, 0, 1, 0},
                {1, 0, 0, 1, 1, 0, 1, 1, 0, 1},
                {0, 1, 0, 1, 0, 0, 1, 0, 1, 0},
                {1, 0, 1, 0, 0, 1, 0, 1, 0, 1},
                {0, 1, 0, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 1, 0, 0, 0, 1, 0, 1, 0},
                {1, 1, 0, 0, 0, 1, 1, 0, 0, 1}
            };
        }
        else if (name.equals("test_get_column")) {
            grid = new int[][] {
                {0, 1, 5},
                {2, 3, 4},
                {4, 5, 3},
                {6, 7, 2},
                {8, 9, 1}
            };
        }
        return grid;
    }
  
    // Gets the row clues depending on the test case.
    public ArrayList<Clue> createRowClues(String name) {
        ArrayList<Clue> rowClues = new ArrayList<Clue>();
        if (name.equals("blanks_smiler") || name.equals("unsolvable_smiler") || name.equals("unsolvable_smiler_2") || name.equals("blanks_smiler_empty")) {
            Clue r1 = new Clue(new ArrayList<Integer>(Arrays.asList(5)), new ArrayList<Integer>(Arrays.asList(2)));
            Clue r2 = new Clue(new ArrayList<Integer>(Arrays.asList(7)), new ArrayList<Integer>(Arrays.asList(2)));
            Clue r3 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 3, 2)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            Clue r4 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 3, 2)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            Clue r5 = new Clue(new ArrayList<Integer>(Arrays.asList(9)), new ArrayList<Integer>(Arrays.asList(2)));
            Clue r6 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 5, 1)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            Clue r7 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 3, 2)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            Clue r8 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 2)), new ArrayList<Integer>(Arrays.asList(2, 2)));
            Clue r9 = new Clue(new ArrayList<Integer>(Arrays.asList(5)), new ArrayList<Integer>(Arrays.asList(2)));
            Clue r10 = new Clue(new ArrayList<Integer>(), new ArrayList<Integer>());
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
            Clue r1 = new Clue(new ArrayList<>(Arrays.asList(5)), new ArrayList<>(Arrays.asList(3)));
            Clue r2 = new Clue(new ArrayList<>(Arrays.asList(7)), new ArrayList<>(Arrays.asList(3)));
            Clue r3 = new Clue(new ArrayList<>(Arrays.asList(2, 1, 6)), new ArrayList<>(Arrays.asList(3, 2, 3)));
            Clue r4 = new Clue(new ArrayList<>(Arrays.asList(2, 1, 3, 1, 2)), new ArrayList<>(Arrays.asList(3, 2, 3, 2, 3)));
            Clue r5 = new Clue(new ArrayList<>(Arrays.asList(9)), new ArrayList<>(Arrays.asList(3)));
            Clue r6 = new Clue(new ArrayList<>(Arrays.asList(9)), new ArrayList<>(Arrays.asList(3)));
            Clue r7 = new Clue(new ArrayList<>(Arrays.asList(2, 1, 3, 1, 2)), new ArrayList<>(Arrays.asList(3, 2, 3, 2, 3)));
            Clue r8 = new Clue(new ArrayList<>(Arrays.asList(2, 3, 2)), new ArrayList<>(Arrays.asList(3, 2, 3)));
            Clue r9 = new Clue(new ArrayList<>(Arrays.asList(5)), new ArrayList<>(Arrays.asList(3)));
            rowClues.add(r1);
            rowClues.add(r2);
            rowClues.add(r3);
            rowClues.add(r4);
            rowClues.add(r5);
            rowClues.add(r6);
            rowClues.add(r7);
            rowClues.add(r8);
            rowClues.add(r9);
        }
        else if (name.equals("multi_checks_1") || name.equals("multi_checks_2")) {
            Clue r1 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1, 1)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            Clue r2 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1, 1)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            Clue r3 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1, 1)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            Clue r4 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1, 1)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            Clue r5 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1, 1)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            Clue r6 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1, 1)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            rowClues.add(r1);
            rowClues.add(r2);
            rowClues.add(r3);
            rowClues.add(r4);
            rowClues.add(r5);
            rowClues.add(r6);
        }
        else if (name.equals("colour_cat")) {
            Clue r1 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1)), new ArrayList<Integer>(Arrays.asList(4, 4)));
            Clue r2 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 2)), new ArrayList<Integer>(Arrays.asList(4, 4)));
            Clue r3 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 5, 2, 7)), new ArrayList<Integer>(Arrays.asList(4, 5, 4, 5)));
            Clue r4 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 5, 2, 8)), new ArrayList<Integer>(Arrays.asList(4, 5, 4, 5)));
            Clue r5 = new Clue(new ArrayList<Integer>(Arrays.asList(9, 9)), new ArrayList<Integer>(Arrays.asList(4, 5)));
            Clue r6 = new Clue(new ArrayList<Integer>(Arrays.asList(9, 5, 3, 2)), new ArrayList<Integer>(Arrays.asList(4, 5, 4, 5)));
            Clue r7 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 1, 5, 1, 2, 3, 5, 2)), new ArrayList<Integer>(Arrays.asList(4, 3, 5, 3, 4, 5, 4, 5)));
            Clue r8 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1, 5, 1, 1, 4, 6, 3)), new ArrayList<Integer>(Arrays.asList(4, 3, 5, 3, 4, 5, 4, 5)));
            Clue r9 = new Clue(new ArrayList<Integer>(Arrays.asList(4, 1, 1, 1, 4, 2, 7, 4)), new ArrayList<Integer>(Arrays.asList(4, 5, 2, 5, 4, 5, 4, 5)));
            Clue r10 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 7, 4, 7, 3)), new ArrayList<Integer>(Arrays.asList(5, 4, 5, 4, 5)));
            Clue r11 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 5, 2, 9, 3)), new ArrayList<Integer>(Arrays.asList(5, 4, 5, 4, 5)));
            Clue r12 = new Clue(new ArrayList<Integer>(Arrays.asList(9, 8, 2)), new ArrayList<Integer>(Arrays.asList(5, 4, 5)));
            Clue r13 = new Clue(new ArrayList<Integer>(Arrays.asList(4, 4, 1, 1, 2)), new ArrayList<Integer>(Arrays.asList(4, 5, 4, 5, 5)));
            Clue r14 = new Clue(new ArrayList<Integer>(Arrays.asList(3, 2, 2)), new ArrayList<Integer>(Arrays.asList(4, 4, 5)));
            Clue r15 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 2)), new ArrayList<Integer>(Arrays.asList(4, 4)));
            Clue r16 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 2)), new ArrayList<Integer>(Arrays.asList(4, 4)));
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
            rowClues.add(r11);
            rowClues.add(r12);
            rowClues.add(r13);
            rowClues.add(r14);
            rowClues.add(r15);
            rowClues.add(r16);
        }
        else if (name.equals("incorrect_cat") || name.equals("multiple_incorrect_cat") || name.equals("blank_grid_cat")) {
            Clue r1 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1)), new ArrayList<Integer>(Arrays.asList(2, 2)));
            Clue r2 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 2)), new ArrayList<Integer>(Arrays.asList(2, 2)));
            Clue r3 = new Clue(new ArrayList<Integer>(Arrays.asList(5)), new ArrayList<Integer>(Arrays.asList(2)));
            Clue r4 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1, 1)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            Clue r5 = new Clue(new ArrayList<Integer>(Arrays.asList(7)), new ArrayList<Integer>(Arrays.asList(2)));
            Clue r6 = new Clue(new ArrayList<Integer>(Arrays.asList(5, 2)), new ArrayList<Integer>(Arrays.asList(2, 2)));
            Clue r7 = new Clue(new ArrayList<Integer>(Arrays.asList(3, 1)), new ArrayList<Integer>(Arrays.asList(2, 2)));
            Clue r8 = new Clue(new ArrayList<Integer>(Arrays.asList(3, 1)), new ArrayList<Integer>(Arrays.asList(2, 2)));
            Clue r9 = new Clue(new ArrayList<Integer>(Arrays.asList(4, 2)), new ArrayList<Integer>(Arrays.asList(2, 2)));
            Clue r10 = new Clue(new ArrayList<Integer>(Arrays.asList(7)), new ArrayList<Integer>(Arrays.asList(2)));
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
        else if (name.equals("errorType1") || name.equals("errorType2") || name.equals("errorType3") || name.equals("errorType4")) {
            Clue r1 = new Clue(new ArrayList<Integer>(Arrays.asList(5)), new ArrayList<Integer>(Arrays.asList(2)));
            rowClues.add(r1);
        }
        else if (name.equals("errorType5")) {
            Clue r1 = new Clue(new ArrayList<Integer>(Arrays.asList(4, 2)), new ArrayList<Integer>(Arrays.asList(2, 2)));
            rowClues.add(r1);
        }
        else if (name.equals("correctLine1") || name.equals("correctLine2") || name.equals("correctLine3")) {
            Clue r1 = new Clue(new ArrayList<Integer>(Arrays.asList(3, 1)), new ArrayList<Integer>(Arrays.asList(2, 3)));
            rowClues.add(r1);
        }
        return rowClues;
    }

    // Gets the column clues depending on the test case.
    public ArrayList<Clue> createColumnClues(String name) {
        ArrayList<Clue> columnClues = new ArrayList<Clue>();
        if (name.equals("blanks_smiler") || name.equals("blanks_smiler_empty")) {
            Clue c1 = new Clue(new ArrayList<>(Arrays.asList(5)), new ArrayList<>(Arrays.asList(2)));
            Clue c2 = new Clue(new ArrayList<>(Arrays.asList(4, 2)), new ArrayList<>(Arrays.asList(2, 2)));
            Clue c3 = new Clue(new ArrayList<>(Arrays.asList(2, 2, 2)), new ArrayList<>(Arrays.asList(2, 2, 2)));
            Clue c4 = new Clue(new ArrayList<>(Arrays.asList(7, 1)), new ArrayList<>(Arrays.asList(2, 2)));
            Clue c5 = new Clue(new ArrayList<>(Arrays.asList(7, 1)), new ArrayList<>(Arrays.asList(2, 2)));
            Clue c6 = new Clue(new ArrayList<>(Arrays.asList(7, 1)), new ArrayList<>(Arrays.asList(2, 2)));
            Clue c7 = new Clue(new ArrayList<>(Arrays.asList(2, 2, 2)), new ArrayList<>(Arrays.asList(2, 2, 2)));
            Clue c8 = new Clue(new ArrayList<>(Arrays.asList(4, 2)), new ArrayList<>(Arrays.asList(2, 2)));
            Clue c9 = new Clue(new ArrayList<>(Arrays.asList(5)), new ArrayList<>(Arrays.asList(2)));
            Clue c10 = new Clue(new ArrayList<>(), new ArrayList<>());
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
        else if (name.equals("unsolvable_smiler") || name.equals("unsolvable_smiler_2")) {
            Clue c1 = new Clue(new ArrayList<>(Arrays.asList(5)), new ArrayList<>(Arrays.asList(2)));
            Clue c2 = new Clue(new ArrayList<>(Arrays.asList(4, 2)), new ArrayList<>(Arrays.asList(2, 2)));
            Clue c3 = new Clue(new ArrayList<>(Arrays.asList(2, 2, 2)), new ArrayList<>(Arrays.asList(2, 2, 2)));
            Clue c4 = new Clue(new ArrayList<>(Arrays.asList(7, 1)), new ArrayList<>(Arrays.asList(2, 2)));
            Clue c5 = new Clue(new ArrayList<>(Arrays.asList(7, 1)), new ArrayList<>(Arrays.asList(2, 2)));
            Clue c6 = new Clue(new ArrayList<>(Arrays.asList(7, 1)), new ArrayList<>(Arrays.asList(2, 2)));
            Clue c7 = new Clue(new ArrayList<>(Arrays.asList(2, 2, 2)), new ArrayList<>(Arrays.asList(2, 2, 2)));
            Clue c8 = new Clue(new ArrayList<>(Arrays.asList(4, 2)), new ArrayList<>(Arrays.asList(2, 2)));
            Clue c9 = new Clue(new ArrayList<>(Arrays.asList(5)), new ArrayList<>(Arrays.asList(2)));
            Clue c10 = new Clue(new ArrayList<>(Arrays.asList(1)), new ArrayList<>(Arrays.asList(2)));
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
            Clue c1 = new Clue(new ArrayList<>(Arrays.asList(5)), new ArrayList<>(Arrays.asList(3)));
            Clue c2 = new Clue(new ArrayList<>(Arrays.asList(7)), new ArrayList<>(Arrays.asList(3)));
            Clue c3 = new Clue(new ArrayList<>(Arrays.asList(2, 2, 2, 1, 2)), new ArrayList<>(Arrays.asList(3, 2, 3, 2, 3)));
            Clue c4 = new Clue(new ArrayList<>(Arrays.asList(7, 1, 1)), new ArrayList<>(Arrays.asList(3, 2, 3)));
            Clue c5 = new Clue(new ArrayList<>(Arrays.asList(7, 1, 1)), new ArrayList<>(Arrays.asList(3, 2, 3)));
            Clue c6 = new Clue(new ArrayList<>(Arrays.asList(7, 1, 1)), new ArrayList<>(Arrays.asList(3, 2, 3)));
            Clue c7 = new Clue(new ArrayList<>(Arrays.asList(3, 1, 2, 1, 2)), new ArrayList<>(Arrays.asList(3, 2, 3, 2, 3)));
            Clue c8 = new Clue(new ArrayList<>(Arrays.asList(7)), new ArrayList<>(Arrays.asList(3)));
            Clue c9 = new Clue(new ArrayList<>(Arrays.asList(5)), new ArrayList<>(Arrays.asList(3)));
            columnClues.add(c1);
            columnClues.add(c2);
            columnClues.add(c3);
            columnClues.add(c4);
            columnClues.add(c5);
            columnClues.add(c6);
            columnClues.add(c7);
            columnClues.add(c8);
            columnClues.add(c9);
        }
        else if (name.equals("multi_checks_1") || name.equals("multi_checks_2")) {
            Clue c1 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1, 1)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            Clue c2 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1, 1)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            Clue c3 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1, 1)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            Clue c4 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1, 1)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            Clue c5 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1, 1)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            Clue c6 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1, 1)), new ArrayList<Integer>(Arrays.asList(2, 2, 2)));
            columnClues.add(c1);
            columnClues.add(c2);
            columnClues.add(c3);
            columnClues.add(c4);
            columnClues.add(c5);
            columnClues.add(c6);
        }
        else if (name.equals("colour_cat")) {
            Clue c1 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1, 2)), new ArrayList<Integer>(Arrays.asList(4, 4, 4)));
            Clue c2 = new Clue(new ArrayList<Integer>(Arrays.asList(9, 3, 2)), new ArrayList<Integer>(Arrays.asList(4, 5, 4)));
            Clue c3 = new Clue(new ArrayList<Integer>(Arrays.asList(5, 2, 2, 2, 2)), new ArrayList<Integer>(Arrays.asList(4, 3, 4, 5, 4)));
            Clue c4 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 2, 2, 3, 1, 1)), new ArrayList<Integer>(Arrays.asList(5, 4, 5, 4, 5, 4)));
            Clue c5 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 2, 3, 2, 2)), new ArrayList<Integer>(Arrays.asList(5, 4, 5, 4, 5)));
            Clue c6 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 2, 2, 1, 2, 2)), new ArrayList<Integer>(Arrays.asList(5, 4, 5, 2, 4, 5)));
            Clue c7 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 2, 3, 2, 2)), new ArrayList<Integer>(Arrays.asList(5, 4, 5, 4, 5)));
            Clue c8 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 2, 2, 3, 2)), new ArrayList<Integer>(Arrays.asList(5, 4, 5, 4, 5)));
            Clue c9 = new Clue(new ArrayList<Integer>(Arrays.asList(5, 2, 2, 2, 4)), new ArrayList<Integer>(Arrays.asList(4, 3, 4, 5, 4)));
            Clue c10 = new Clue(new ArrayList<Integer>(Arrays.asList(9, 4, 3)), new ArrayList<Integer>(Arrays.asList(4, 5, 4)));
            Clue c11 = new Clue(new ArrayList<Integer>(Arrays.asList(4, 1, 1, 1, 1, 2)), new ArrayList<Integer>(Arrays.asList(5, 4, 5, 4, 5, 4)));
            Clue c12 = new Clue(new ArrayList<Integer>(Arrays.asList(8, 2)), new ArrayList<Integer>(Arrays.asList(5, 4)));
            Clue c13 = new Clue(new ArrayList<Integer>(Arrays.asList(8, 2)), new ArrayList<Integer>(Arrays.asList(5, 4)));
            Clue c14 = new Clue(new ArrayList<Integer>(Arrays.asList(6, 4)), new ArrayList<Integer>(Arrays.asList(5, 4)));
            Clue c15 = new Clue(new ArrayList<Integer>(Arrays.asList(4, 6)), new ArrayList<Integer>(Arrays.asList(5, 4)));
            Clue c16 = new Clue(new ArrayList<Integer>(Arrays.asList(3, 7)), new ArrayList<Integer>(Arrays.asList(5, 4)));
            Clue c17 = new Clue(new ArrayList<Integer>(Arrays.asList(3, 7)), new ArrayList<Integer>(Arrays.asList(5, 4)));
            Clue c18 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 7)), new ArrayList<Integer>(Arrays.asList(5, 4)));
            Clue c19 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 5)), new ArrayList<Integer>(Arrays.asList(5, 4)));
            Clue c20 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 3)), new ArrayList<Integer>(Arrays.asList(5, 4)));
            Clue c21 = new Clue(new ArrayList<Integer>(Arrays.asList(3)), new ArrayList<Integer>(Arrays.asList(5)));
            Clue c22 = new Clue(new ArrayList<Integer>(Arrays.asList(2)), new ArrayList<Integer>(Arrays.asList(5)));
            Clue c23 = new Clue(new ArrayList<Integer>(Arrays.asList(3)), new ArrayList<Integer>(Arrays.asList(5)));
            Clue c24 = new Clue(new ArrayList<Integer>(Arrays.asList(3)), new ArrayList<Integer>(Arrays.asList(5)));
            Clue c25 = new Clue(new ArrayList<Integer>(Arrays.asList(5, 2)), new ArrayList<Integer>(Arrays.asList(5, 4)));
            Clue c26 = new Clue(new ArrayList<Integer>(Arrays.asList(4, 2)), new ArrayList<Integer>(Arrays.asList(5, 4)));
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
            columnClues.add(c11);
            columnClues.add(c12);
            columnClues.add(c13);
            columnClues.add(c14);
            columnClues.add(c15);
            columnClues.add(c16);
            columnClues.add(c17);
            columnClues.add(c18);
            columnClues.add(c19);
            columnClues.add(c20);
            columnClues.add(c21);
            columnClues.add(c22);
            columnClues.add(c23);
            columnClues.add(c24);
            columnClues.add(c25);
            columnClues.add(c26);
        }
        else if (name.equals("incorrect_cat") || name.equals("multiple_incorrect_cat") || name.equals("blank_grid_cat")) {
            Clue c1 = new Clue(new ArrayList<Integer>(Arrays.asList(1)), new ArrayList<Integer>(Arrays.asList(2)));
            Clue c2 = new Clue(new ArrayList<Integer>(Arrays.asList(6)), new ArrayList<Integer>(Arrays.asList(2)));
            Clue c3 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 6)), new ArrayList<Integer>(Arrays.asList(2, 2)));
            Clue c4 = new Clue(new ArrayList<Integer>(Arrays.asList(8)), new ArrayList<Integer>(Arrays.asList(2)));
            Clue c5 = new Clue(new ArrayList<Integer>(Arrays.asList(2, 6)), new ArrayList<Integer>(Arrays.asList(2, 2)));
            Clue c6 = new Clue(new ArrayList<Integer>(Arrays.asList(6, 2)), new ArrayList<Integer>(Arrays.asList(2, 2)));
            Clue c7 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 1)), new ArrayList<Integer>(Arrays.asList(2, 2)));
            Clue c8 = new Clue(new ArrayList<Integer>(Arrays.asList(1)), new ArrayList<Integer>(Arrays.asList(2)));
            Clue c9 = new Clue(new ArrayList<Integer>(Arrays.asList(1, 2)), new ArrayList<Integer>(Arrays.asList(2, 2)));
            Clue c10 = new Clue(new ArrayList<Integer>(Arrays.asList(4)), new ArrayList<Integer>(Arrays.asList(2)));
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
        return columnClues;
    }

    // Check the tests which test the list of lists of incorrect lines and the corresponding output message.
    public String checkTestStatus(int testCaseNumber, ArrayList<ArrayList<Integer>> incorrectLines, String message) {
        String status = "";

        switch(testCaseNumber) {
            case 1:
            case 2:
            case 5:
            case 6:
            case 7:
            case 8:
                if (incorrectLines.get(0).size() == 0 && incorrectLines.get(1).size() == 0 && incorrectLines.size() == 2
                    && message.equals("All rows are correct.\nAll columns are correct.\nCompletion: 100.0%")) {
                        status = "\u001B[32m Pass";
                }
                else {
                    status = "\u001B[31m Fail";
                }
                break;
            case 3:
                if (incorrectLines.get(0).size() == 0 && incorrectLines.get(1).size() == 1 && incorrectLines.size() == 2
                    && message.equals("All rows are correct.\nIncorrect column 10\nCompletion: 95.0%")) {
                        status = "\u001B[32m Pass";
                }
                else {
                    status = "\u001B[31m Fail";
                }
                break;
            case 4:
                if (incorrectLines.get(0).size() == 1 && incorrectLines.get(1).size() == 0 && incorrectLines.size() == 2
                    && message.equals("All columns are correct.\nIncorrect row 10\nCompletion: 95.0%")) {
                        status = "\u001B[32m Pass";
                }
                else {
                    status = "\u001B[31m Fail";
                }
                break;
            case 9:
                if (incorrectLines.get(0).size() == 1 && incorrectLines.get(1).size() == 1 && incorrectLines.size() == 2
                    && message.equals("Incorrect row 2\nIncorrect column 7\nCompletion: 90.0%")) {
                        status = "\u001B[32m Pass";
                }
                else {
                    status = "\u001B[31m Fail";
                }
                break;
            case 10:
                if (incorrectLines.get(0).size() == 4 && incorrectLines.get(1).size() == 4 && incorrectLines.size() == 2
                    && message.equals("Incorrect row 1\nIncorrect row 4\nIncorrect row 8\nIncorrect row 10\nIncorrect column 3\nIncorrect column 5\nIncorrect column 6\nIncorrect column 9\nCompletion: 60.0%")) {
                        status = "\u001B[32m Pass";
                }
                else {
                    status = "\u001B[31m Fail";
                }
                break;
            case 11:
                if (incorrectLines.get(0).size() == 10 && incorrectLines.get(1).size() == 10 && incorrectLines.size() == 2
                    && message.equals("Incorrect row 1\nIncorrect row 2\nIncorrect row 3\nIncorrect row 4\nIncorrect row 5\nIncorrect row 6\nIncorrect row 7\nIncorrect row 8\nIncorrect row 9\nIncorrect row 10\nIncorrect column 1\nIncorrect column 2\nIncorrect column 3\nIncorrect column 4\nIncorrect column 5\nIncorrect column 6\nIncorrect column 7\nIncorrect column 8\nIncorrect column 9\nIncorrect column 10\nCompletion: 0.0%")) {
                        status = "\u001B[32m Pass";
                }
                else {
                    status = "\u001B[31m Fail";
                }
                break;
        }
        return status;
    }

    //Check the tests which test if a given row or column is valid/correct or not.
    public String checkTestStatus(int testCaseNumber, Boolean isLineCorrect) {
        String status = "";

        switch(testCaseNumber) {
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
                if (!isLineCorrect) {
                    status = "\u001B[32m Pass";
                }
                else {
                    status = "\u001B[31m Fail";
                }
                break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
                if (isLineCorrect) {
                    status = "\u001B[32m Pass";
                }
                else {
                    status = "\u001B[31m Fail";
                }
                break;
        }
        return status;
    }

    // Checks the test which tests if the getColumn() method successfully returns the correct columns from a 2D int array.
    public String checkTestStatus(int[] column1, int[] column2, int[] column3) {
        String status = "";
        int[] correctColumn1 = new int[] {0, 2, 4, 6, 8};
        int[] correctColumn2 = new int[] {1, 3, 5, 7, 9};
        int[] correctColumn3 = new int[] {5, 4, 3, 2, 1};
        
        if (Arrays.equals(column1, correctColumn1) && Arrays.equals(column2, correctColumn2) && Arrays.equals(column3, correctColumn3)) {
            status = "\u001B[32m Pass";
        }
        else {
            status = "\u001B[31m Fail";
        }
        
        return status;
    }

    // Checks the tests wich test if the getProgress() method successfully returns the correct progress given the number of incorrect and total rows/columns.
    public String checkTestStatus(int testCaseNumber, double progress) {
        String status = "";

        switch(testCaseNumber) {
            case 1:
                if (progress == 50.0) {
                    status = "\u001B[32m Pass";
                }
                else {
                    status = "\u001B[31m Fail";
                }
                break;
            case 2:
                if (progress == 80.8) {
                    status = "\u001B[32m Pass";
                }
                else {
                    status = "\u001B[31m Fail";
                }
                break;
        }

        return status;
    }

    // Converts the array to a nice String so it can be printed.
    public String getArrayString(int[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i != array.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        String arrayString = sb.toString();
        return arrayString;
    }
}