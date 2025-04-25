//this class contains methods that the main can call to manipulate the grids prior to checking or parsing
import java.util.ArrayList;


public class GridManipulation {
    public static void convertToClues(Grid g) {
        for (int i = 0; i < g.rows; i ++) {
            int currentCount = 0;
            ArrayList<Integer> color = new ArrayList<>();
            ArrayList<Integer> count = new ArrayList<>();
            for (int j = 0; j < g.columns; j++) {
                if (g.grid[i][j] != 0) {
                    currentCount++;
                    if ((j!= g.columns - 1  && g.grid[i][j] != g.grid[i][j+1]) || (j == g.columns-1 && g.grid[i][j] != g.grid[i][j-1])) {
                        count.add(currentCount);
                        color.add(g.grid[i][j]);
                        currentCount = 0;
                    }
                }
            }
            Clue c = new Clue(count, color);
            g.rowClues.add(c);
        }
        for (int i = 0; i < g.columns; i ++) {
            int currentCount = 0;
            ArrayList<Integer> color = new ArrayList<>();
            ArrayList<Integer> count = new ArrayList<>();
            for (int j = 0; j < g.rows; j++) {
                if (g.grid[j][i] != 0) {
                    currentCount++;
                    if ((j!= g.columns - 1  && g.grid[j][i] != g.grid[j+1][i]) || (j == g.columns-1 && g.grid[j][i] != g.grid[j-1][i])) {
                        count.add(currentCount);
                        color.add(g.grid[j][i]);
                        currentCount = 0;
                    }
                }
            }
            Clue c = new Clue(count, color);
            g.columnClues.add(c);
        }
    }
}