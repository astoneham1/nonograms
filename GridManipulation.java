//this class contains methods that the main can call to manipulate the grids prior to checking or parsing

public class GridManipulation {

    // Prior to checking a grid, it needs to have it's progress be converted to equivalent 'clues' and have these loaded into it's clues
    // for now b/w
    public static void convertToClues(Grid g) {
        int currentColor = 0;
        int currentCount = 0;
        for (int i = 0; i < g.rows; i ++) {
            for (int j = 0; j < g.columns; j++) {
                if (g.grid[i][j] != 0 && g.grid[i][j] != currentColor) { 
                    //create a clue obj that had the current color, current count
                    // g.rowClues.add(clue object)
                    currentColor = g.grid[i][j]; 
                }
                if (g.grid[i][j] == currentColor) { currentCount++; }
                else { continue; }
            }
        }
        for (int i = 0; i < g.columns; i ++) {
            for (int j = 0; j < g.rows; j++) {
                if (g.grid[i][j] != 0 && g.grid[i][j] != currentColor) { 
                    //create a clue obj that had the current color, current count
                    // g.columnClues.add(clue object)
                    currentColor = g.grid[i][j]; 
                }
                if (g.grid[i][j] == currentColor) { currentCount++; }
                else { continue; }
            }
        }
    
    }
}
