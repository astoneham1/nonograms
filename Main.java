// import javax.json.JsonWriter;

public class Main { 
    public static Grid currentGridClues; // current grid of clues  
    public static Grid inProgressClues; // grid that represents the progress of the user in terms of clues

    public static void main(String [] args) {
        //load the currentGrid
        //call grid writer to create a temp grid that can be easily modified

    }


    public static void load(Grid current, Grid inProgress) {
        

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
