//this class contains methods that the main can call to manipulate the grids prior to checking or parsing
import java.util.ArrayList;
import java.util.HashMap;


public class GridManipulation {

    // Prior to checking a grid, it needs to have it's progress be converted to equivalent 'clues' and have these loaded into it's clues
    // for now b/w
    public static void convertToClues(Grid g) {
        for (int i = 0; i < g.rows; i ++) {
            // int currentColor = 0;
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
        
        // for (int i = 0; i < g.columns; i ++) {
        //     int currentColor = 0;
        //     int currentCount = 0;
        //     ArrayList<Integer> color = new ArrayList<>();
        //     ArrayList<Integer> count = new ArrayList<>();
        //     for (int j = 0; j < g.rows; j++) {
        //         if (g.grid[i][j] != 0 && g.grid[i][j] != currentColor) { 
        //             //create a clue obj that had the current color, current count
        //             count.add(currentCount);
        //             color.add(currentColor);
        //             currentColor = g.grid[i][j]; 
        //         }
        //         if (g.grid[i][j] == currentColor) { currentCount++; }
        //         else { continue; }
        //     }
        //     Clue c = new Clue(count, color);
        //     g.rowClues.add(c);
        // }
    }
}


//  || (j == 0 && g.grid[i][j] != g.grid[i][j+1] )
                // if (g.grid[i][j] != 0) {
                //     currentColor = g.grid[i][j];
                // }
                // if (g.grid[i][j] == currentColor && currentColor != 0) {
                //     currentCount++;
                // }
                // if (g.grid[i][j] != currentColor || (j>0 && g.grid[i][j-1] != 0 && g.grid[i][j] != g.grid[i][j-1])) {
                //     count.add(currentCount);
                //     color.add(currentColor);
                //     currentCount = 0;
                //     currentColor = 0;
                // }


                // if (g.grid[i][j] != currentColor && g.grid[i][j] != 0 ||  currentColor != 0 && g.grid[i][j] == 0) {
                //     count.add(currentCount);
                //     color.add(currentColor);
                //     currentCount = 1;
                // }
                // if (g.grid[i][j] != 0) {
                //     currentColor = g.grid[i][j]; 
                //     currentCount++;
            //         count.add(currentCount);
            //         color.add(currentColor);
            //         currentCount = 1;
                // }

            //     if (g.grid[i][j] == currentColor && g.grid[i][j] != 0) { currentCount++; }
                // else { continue; }
