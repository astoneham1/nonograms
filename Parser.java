import javax.json.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Parser {
    //2d arraylist containing an arraylist of number of cells and there colour for row and columns
    public static ArrayList<ArrayList<Integer>> rows = new ArrayList<>();
    public static ArrayList<ArrayList<Integer>> rowColour = new ArrayList<>();
    public static ArrayList<ArrayList<Integer>> columns = new ArrayList<>();
    public static ArrayList<ArrayList<Integer>> columnColour = new ArrayList<>();
    //hashmap of the colours and there hex codes
    public static LinkedHashMap<String, String> colours = new LinkedHashMap<>();
    
    public static int rowOrColumnNum = 0;
    public static String currentArray = "";
    public static void main(String[] args) {
        String fileName = "./Jsons/colour_cat.json";
        try {
            Parser.getArrayLists(fileName);
            System.out.println(rows.toString());
            System.out.println(columns.toString());
            System.out.println(colours.toString());
            System.out.println(rowColour.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

     /**
     * Clears arraylists to ensure empty after every load
     */
    public static void clearLists(){
        rows.clear();
        rows.trimToSize();
        columns.clear();
        columns.trimToSize();
    }

    /**
     * gets arraylists and info
     */
    public static void getArrayLists(String fileName) throws FileNotFoundException {
        clearLists();
        JsonReader reader = Json.createReader(new FileReader(fileName));
        JsonStructure jsonst = reader.read();
        getArrayListsFromTree(jsonst, null);
        if (colours.size() == 0) {
            colours.put("COLOUR_1", "0x000000");
            colours.put("UNKOWN", "0xECECEC");
            colours.put("EMPTY", "0xffffff");
        }
    }

     
    /**
     * Parses Through arraylists
     */
    public static void getArrayListsFromTree(JsonValue tree, String key) {
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
            case FALSE:
            case NULL:
                break;
        }
    }
}