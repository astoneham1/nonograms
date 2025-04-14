// import javax.json.JsonWriter;

public class Main { 
//     public static Grid currentGridClues; // current grid of clues  
//     public static Grid inProgressClues; // grid that represents the progress of the user in terms of clues
//     public static // These two grids can be passed into the checker and checked against each other
//     public static JsonWriter writer;

    public static void main(String [] args) {
        //load the currentGrid
        //call grid writer to create a temp grid that can be easily modified
        int rows = 3;
        int cols = 4;
        Grid g = new Grid(rows, cols, "Jsons/hi.json");
        g.updateMove(2, 1, 4);
        g.updateMove(2, 2, 3);
        g.updateMove(2, 1, 2);
        g.saveMoves();
        g.updateMove(2, 1, 5);
        for (int i = 0; i<rows; i++) {
            for (int j = 0; j<cols; j++) {
                System.out.print(g.grid[i][j]);
            }
            System.out.println();
        }
        System.out.println(g.moves);
        Grid w = new Grid(rows, cols, "Jsons/hi.json");
        w.loadMoves();
        for (int i = 0; i<rows; i++) {
            for (int j = 0; j<cols; j++) {
                System.out.print(w.grid[i][j]);
            }
            System.out.println();
        }
        // System.out.println(w.moves);
    }


    

//     // Summary of the gameplay:
//     // loads ui
//     // allows the user to select a grid and calls the parser to load the grid objects clues which should be stored in an object with clues
//     // the program checks if the user has started this puzzle before... (this is done by GridLoader)    
//         // YES: retrieves the json file that tracks the grid status and the moves the user has made
//         // NO: creates a json file to track the current grid and another to track the moves the user makes
//     // While the user plays:
//         // when the user makes a move this:
//             // 1. modifies the "currentGrid" and is recorded by the "moves" file
//             // 2. the "currentGrid" is translated into "clues" 
//                 // (eg. row 1 is [[1,1]] and col 3 is [[1,1]] meaning that row one now has one black square and five unknown squares, col three has the same)
//             // 3. the checker cross references the "currentGrid"'s clues with the actual clues provided by the parser and reports whether or not they are satisfied
//             // 4. the GUI responds accordingly and marks the square as incorrect or not


}
