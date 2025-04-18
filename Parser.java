import javax.json.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Parser {
    //2d arraylist containing an arraylist of number of cells and there colour for row and columns
    public ArrayList<ArrayList<Integer>> rows = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> rowColour = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> columns = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> columnColour = new ArrayList<>();
    //hashmap of the colours and there hex codes
    public LinkedHashMap<String, String> colours = new LinkedHashMap<>();
    
    //used when parsing
    public int rowOrColumnNum = 0;
    public String currentArray = "";

     /**
     * Clears arraylists to ensure empty after every load
     */
    public void clearLists(){
        rows.clear();
        rows.trimToSize();
        columns.clear();
        columns.trimToSize();
        rowOrColumnNum = 0;
        currentArray = "";
        Colours.colours.clear();
    }

    /**
     * gets arraylists and info
     */
    public void getArrayLists(String fileName) throws FileNotFoundException {
        clearLists();
        JsonReader reader = Json.createReader(new FileReader(fileName));
        JsonStructure jsonst = reader.read();
        getArrayListsFromTree(jsonst, null);
        if (colours.size() == 0) {
            colours.put("UNKOWN", "0xECECEC");
            colours.put("EMPTY", "0xffffff");
            colours.put("COLOUR_1", "0x000000");
        }
    }

     
    /**
     * Parses Through arraylists
     */
    public void getArrayListsFromTree(JsonValue tree, String key) {
        if (key != null) {
            if (key.equals("states")) {
                currentArray = "colours";
            } else if (key.equals("rows")) {
                currentArray = "rows";
                rowOrColumnNum = -1;
            } else if (key.equals("columns")) {
                currentArray = "columns";
                rowOrColumnNum = -1;
            }
        }
        switch (tree.getValueType()) {
            case OBJECT:
                JsonObject object = (JsonObject) tree;
                for (String name : object.keySet())
                    getArrayListsFromTree(object.get(name), name);
                break;
            case ARRAY:
                JsonArray array = (JsonArray) tree;
                if (currentArray.equals("rows")) {
                    if (key == null && tree.getValueType() == JsonValue.ValueType.ARRAY) {
                        rows.add(new ArrayList<Integer>());
                        rowColour.add(new ArrayList<>());
                        rowOrColumnNum++;
                    }
                } else if (currentArray.equals("columns")) {
                    if (key == null && tree.getValueType() == JsonValue.ValueType.ARRAY) {
                        columns.add(new ArrayList<Integer>());
                        columnColour.add(new ArrayList<Integer>());
                        rowOrColumnNum++;
                    }
                }
                for (JsonValue val : array)
                    getArrayListsFromTree(val, null);
                break;
            case STRING:
                JsonString st = (JsonString) tree;
                if (currentArray.equals("colours")) {
                    colours.put(key, st.toString());
                } else if (currentArray.equals("rows")) {
                    if (key.equals("count")) {
                        int i = Integer.parseInt(st.toString());
                        rows.get(rowOrColumnNum).add(i);
                    }
                    else{
                        int colourIndex = 0;
                        int i = 0;
                        for(String colour : colours.keySet()){
                            if(colour.equals(st.toString())){
                                i = colourIndex;
                            }
                            colourIndex++;
                        }
                        rowColour.get(rowOrColumnNum).add(i);
                    }
                } else if (currentArray.equals("columns")) {
                    if (key.equals("count")) {
                        int i = Integer.parseInt(st.toString());
                        columns.get(rowOrColumnNum).add(i);
                    }
                    else{
                        int colourIndex = 0;
                        int i = 0;
                        for(String colour : colours.keySet()){
                            if(colour.equals(st.toString())){
                                i = colourIndex;
                            }
                            colourIndex++;
                        }
                        columnColour.get(rowOrColumnNum).add(i);
                    }
                }
                break;
            case NUMBER:
                JsonNumber num = (JsonNumber) tree;
                if (colours.size() == 0) {
                    if (currentArray.equals("rows")) {
                        rows.get(rowOrColumnNum).add(num.intValue());
                    }
                    if (currentArray.equals("columns")) {
                        columns.get(rowOrColumnNum).add(num.intValue());
                    }
                } else {
                    if (currentArray.equals("rows")) {
                        rows.get(rowOrColumnNum).add(num.intValue());
                    }
                    if (currentArray.equals("columns")) {
                        columns.get(rowOrColumnNum).add(num.intValue());
                    }
                }
                break;
            case TRUE:
                break;
            case FALSE:
                break;
            case NULL:
                break;
        }
    }

    //generates a clue from two arraylists
    public Clue getClue(ArrayList<Integer> counts, ArrayList<Integer> colours)throws JsonFormatException{
        Clue clue;
        if (colours.size() == 0) {
            for(int i = 0; i < counts.size(); i++){
                colours.add(2);
            }
            clue = new Clue(counts, colours);
        }else if(counts.size() != colours.size()){
            throw new JsonFormatException();
        } else {
            clue = new Clue(counts, colours);
        }
        return clue;
        //could throw an invalid json error if information is wrong here
        //instead of else use elif and check arrays are the same size
    }

    //generates an arraylist of clues
    public ArrayList<Clue> getClues(ArrayList<ArrayList<Integer>> lines, ArrayList<ArrayList<Integer>> colours) throws JsonFormatException{
        ArrayList<Clue> clues = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            clues.add(getClue(lines.get(i), colours.get(i)));
        }
        return clues;
    }

    //add colours to the colours.java hashmap
    public static void createColourHashmap(LinkedHashMap<String, String> colours){
        int i = 0;
        for(String colour : colours.keySet()){
            Colours.colours.put(i, colours.get(colour));
            i++;
        }
    }

    //generates the grid
    public Grid getGrid(String filePath) throws JsonFormatException, FileNotFoundException{
        getArrayLists(filePath);
        createColourHashmap(colours);
        Grid grid = new Grid(getClues(rows, rowColour), getClues(columns, columnColour), rows.size(), columns.size());
        return grid;
    }
}