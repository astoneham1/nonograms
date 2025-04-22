import java.util.stream.IntStream;

public class SolverMain {
    Grid puzzleGrid;
    Grid solverGrid;
    int rows;
    int cols;
    boolean color;

    public SolverMain(Grid puzzleGrid, boolean color) {
        this.puzzleGrid = puzzleGrid;
        this.solverGrid = new Grid(puzzleGrid.rows, puzzleGrid.columns, "Moves/solverMoves.json");
        this.rows = puzzleGrid.columns;
        this.cols = puzzleGrid.rows;
        this.color = color;
    }

    public void solver() {
        System.out.println(puzzleGrid.columnClues.size());
        optimization();
    }

    public void optimization() {
        int sum = 0;
        int place = 0;
        for (int i = 0; i < puzzleGrid.columnClues.size(); i++) {
            sum = place = 0;
            // place = 0;
            for (Integer j : puzzleGrid.columnClues.get(i).counts) {
                if (j == cols) {
                    updateRangeCol(0, cols, puzzleGrid.columnClues.get(i).colour.get(0), i);
                }
                if (j == cols - 1) {
                    updateRangeCol(1, cols - 1, puzzleGrid.columnClues.get(i).colour.get(0), i);
                }
                sum += j;
            }
            if (sum + puzzleGrid.columnClues.get(i).counts.size() - 1 == cols && color == false) {
                for (int j = 0; j < puzzleGrid.columnClues.get(i).counts.size(); j++) {
                    updateRangeCol(place, puzzleGrid.columnClues.get(i).counts.get(j) + place, puzzleGrid.columnClues.get(i).colour.get(j), i);
                    place += puzzleGrid.columnClues.get(i).counts.get(j) + 1;
                }
            }
            if (sum == cols && color == true) {
                for (int j = 0; j < puzzleGrid.columnClues.get(i).counts.size(); j++) {
                    updateRangeCol(place, puzzleGrid.columnClues.get(i).counts.get(j) + place, puzzleGrid.columnClues.get(i).colour.get(j), i);
                    place += puzzleGrid.columnClues.get(i).counts.get(j);
                }
            }
        }
        for (int i = 0; i < puzzleGrid.rowClues.size(); i++) {
            sum = place = 0;
            for (Integer j : puzzleGrid.rowClues.get(i).counts) {
                if (j == rows) {
                    updateRangeRow(0, rows, puzzleGrid.columnClues.get(i).colour.get(0), i);
                }
                if (j == rows - 1) {
                    updateRangeRow(1, rows - 1, puzzleGrid.columnClues.get(i).colour.get(0), i);
                }
                sum += j;
            }
            if (sum + puzzleGrid.rowClues.get(i).counts.size() - 1 == rows && color == false) {
                for (int j = 0; j < puzzleGrid.rowClues.get(i).counts.size(); j++) {
                    updateRangeRow(place, puzzleGrid.rowClues.get(i).counts.get(j) + place, puzzleGrid.rowClues.get(i).colour.get(j), i);
                    place += puzzleGrid.rowClues.get(i).counts.get(j) + 1;
                }
            }
            if (sum == rows && color == true) {
                // sum = place = 0;
                System.out.println("here we are!!" + i);
                for (int j = 0; j < puzzleGrid.rowClues.get(i).counts.size(); j++) {
                    updateRangeRow(place, puzzleGrid.rowClues.get(i).counts.get(j) + place, puzzleGrid.rowClues.get(i).colour.get(j), i);
                    place += puzzleGrid.rowClues.get(i).counts.get(j);
                }
            }
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