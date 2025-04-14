// import java.io.FileWriter;
// import java.io.Writer;
// import java.util.ArrayList;

// import javax.json.Json;
// import javax.json.JsonArrayBuilder;
// import javax.json.JsonObjectBuilder;
// import javax.json.JsonWriter;

// public class GridWriter {

//     public static void main(String args[]) {
//         JsonWriter writer;
//         ArrayList<ArrayList<ArrayList<String>>> r = new ArrayList<>();
//         ArrayList<ArrayList<ArrayList<String>>> c = new ArrayList<>();
//         Grid g = new Grid(r, c, 2, 2);
//         writeNewGrid(g, writer);

//     }


//     //generates an empty grid of cells, each cell contains a number corresponding to a color
//     //generates a json file that will record moves by recording the old cell
//     //generates a 2d array of numbers that will be modified during the game play and can be saved to the JSON file to preserve progress
//     public static void writeNewGrid(Grid originalGrid, JsonWriter writer) { //
//         // int[][] grid = new int[originalGrid.rows][originalGrid.columns];
//         // for (int i: originalGrid.rows) {
//         //     for (int j: originalGrid.columns) {

//         //     }
//         // }
//         try {
//             String path = "/currentGrid" + original.title;
//             Writer w = new FileWriter(path);
//             writer = Json.createWriter(w);
//             //creates a json file to store the users grid data
//             JsonArrayBuilder gridBuilder = Json.createArrayBuilder();
//             JsonObjectBuilder rowBuilder = Json.createObjectBuilder();
//             JsonArrayBuilder columnBuilder = Json.createArrayBuilder();
//             for (int i : originalGrid.rows) {
//                 gridBuilder.add(rowBuilder);
//                 rowBuilder.add("Row "+i, columnBuilder);
//                 for (int j: originalGrid.columns) {
//                     columnBuilder.add(0);
//                 }
//             }
//         } catch (Exception e) {
//             System.out.println(e);
//         }
//         // return grid;
//     }

//     // whenever the user wants to save their progress, this will read the 2-D array into the original grid that will persist
//     public void saveGrid(int[][] currentGrid) {

//     }

//     // method to be called to modify the 
//     public int[][] modifyGrid(int row, int col, int color, int[][] currentGrid) {
//         currentGrid[row][col] = color;
//         return currentGrid;
//     }

// }

// //for the json grid: it'll prob be a set of rows with row amounts of object
// //for the json instructions: they'll prob contain an "order", new and old attributes