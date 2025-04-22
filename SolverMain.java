import java.util.stream.IntStream;

public class SolverMain {
    Grid puzzleGrid;
    Grid solverGrid;
    int rows;
    int cols;

    public SolverMain(Grid puzzleGrid) {
        this.puzzleGrid = puzzleGrid;
        this.solverGrid = new Grid(puzzleGrid.rows, puzzleGrid.columns, "Moves/solverMoves.json");
        this.rows = puzzleGrid.columns;
        this.cols = puzzleGrid.rows;
    }

    public void solver() {
        System.out.println(puzzleGrid.columnClues.size());
        optimization();
    }

    public void optimization() {
        int sum = 0;
        int place = 0;
        for (int i = 0; i < puzzleGrid.columnClues.size(); i++) {
            sum = 0;
            place = 0;
            for (Integer j : puzzleGrid.columnClues.get(i).counts) {
                if (j == cols) {
                    System.out.println("we senses the full row" + cols);
                    updateRangeCol(0, cols, puzzleGrid.columnClues.get(i).colour.get(0), i);
                }
                if (j == cols - 1) {
                    System.out.println("we at least enter here");
                    updateRangeCol(1, cols - 1, puzzleGrid.columnClues.get(i).colour.get(0), i);
                }
                sum += j;
            }
            System.out.println(sum);
            if (sum + puzzleGrid.columnClues.get(i).counts.size() - 1 == cols) {
                for (Integer j : puzzleGrid.columnClues.get(i).counts) {
                    updateRangeCol(place, j + place, puzzleGrid.columnClues.get(i).colour.get(0), i);
                    place += j + 1;
                }
            }
        }
        for (int i = 0; i < puzzleGrid.rowClues.size(); i++) {
            for (Integer j : puzzleGrid.rowClues.get(i).counts) {
                if (j == rows) {
                    System.out.println("we senses the full row");
                    updateRangeRow(0, rows, puzzleGrid.columnClues.get(i).colour.get(0), i);
                }
                if (j == rows - 1) {
                    updateRangeRow(1, rows - 1, puzzleGrid.columnClues.get(i).colour.get(0), i);
                }
                sum += j;
            }
        }
    }

    public void updateRangeRow(int start, int end, int color, int row) {
        for (int i = start; i < end; i++) {
            solverGrid.updateMove(row -1, i, color);
        }   
    }


    public void updateRangeCol(int start, int end, int color, int col) {
        for (int i = start; i < end; i++) {
            solverGrid.updateMove(i, col-1, color);
        }   
    }
    
}