// // package src;

// import java.util.LinkedHashMap;
// import java.util.Map;

// import javax.swing.JFrame;
// import javax.swing.SwingUtilities;

// public class Main {

//     // Global grid variables
//     private Grid puzzleGrid;
//     private Grid userGrid;

//     // Global variables that can be used as trackers
//     boolean saved = false;

//     public static void main(String[] args) {
//         // Create default colors for when the puzzle is loaded
//         Colours colours = new Colours();

//         // call method to have user select a puzzle file, return a String that can be used as the path for the first puzzle
//         this.puzzleGrid = Parser.getGrid(path)
//         // call method to then load the UI for the puzzle

//         // have a button to listen 
//     }
// }



// public static void main(String[] args) {
//     SwingUtilities.invokeLater(() -> {
//         // create default colors (will be replaced when a puzzle is loaded)
//         Map<String, Color> defaultColors = new LinkedHashMap<>();
//         defaultColors.put("UNKNOWN", Color.decode("#ECECEC"));
//         defaultColors.put("EMPTY", Color.decode("#FFFFFF"));

//         JFrame frame = new JFrame("Nonograms");
//         frame.setContentPane(new app(defaultColors).mainPanel);
//         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         frame.setSize(800, 600);
//         frame.setLocationRelativeTo(null);
//         frame.setVisible(true);
//     });
// }
