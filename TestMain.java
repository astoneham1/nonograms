// import javax.json.JsonWriter;

public class TestMain { 
    public static Grid currentGrid; // current grid of clues  
    public static Grid inProgressGrid; // grid that represents the progress of the user in terms of clues

    public static void main(String [] args) {
    try {
        Parser p = new Parser();
        Grid g = p.getGrid("Jsons/smiler.json");
        SolverMain s = new SolverMain(g);
        s.solver();
        for (int i = 0; i < s.solverGrid.rows; i++) {
            for (int j = 0; j < s.solverGrid.columns; j++) {
                System.out.print(s.solverGrid.grid[i][j]);
            }
            System.out.println();
        }
    } catch (Exception e) {
        System.out.println(e);
    }

    }

}
        //load the currentGrid from the parser
        //create inProgressGrid that has the same params as the current Grid 
    //     int row = 5;
    //     int col = 5;
    //     Grid g = new Grid(row, col, "Jsons/hello.json");
    //     g.updateMove(1, 2, 2);
    //     g.updateMove(3, 0, 4);
    //     g.updateMove(3, 3, 4);
    //     g.updateMove(3, 2, 4);
    //     g.updateMove(2, 1, 3);
    //     g.updateMove(1, 2, 2);
    //     g.updateMove(1, 3, 4);
    //     g.updateMove(1, 4, 5);
    //     g.updateMove(4, 3, 5);
    //     g.updateMove(4, 2, 5);
    //     g.updateMove(4, 4, 1);
    //     g.updateMove(0, 3, 1);
    //     for (int i = 0; i < g.rows; i++) {
    //         for (int j = 0; j < g.columns; j++) {
    //             System.out.print(g.grid[i][j]);
    //         }
    //         System.out.println();
    //     }
    //     g.saveMoves();
    //     GridManipulation.convertToClues(g);







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

