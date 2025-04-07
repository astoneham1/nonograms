import javax.json.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    public static HashMap<String, String> colours = new HashMap<>();

    //used for parsing the json
    public static String currentArray = "";
    public static Integer rowOrColumnNum = 0;
    public static Integer cellNum = 0;

    public static ArrayList<ArrayList<ArrayList<String>>> getRows(String fileName) throws FileNotFoundException {
        ArrayList<ArrayList<ArrayList<String>>> rows = new ArrayList<>();
        JsonReader reader = Json.createReader(new FileReader(fileName));
        JsonStructure jsonst = reader.read();
        getRowsFromTree(jsonst, null, rows);
        return rows;
    }

    public static void getRowsFromTree(JsonValue tree, String key, ArrayList<ArrayList<ArrayList<String>>> rows) {
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
                    getRowsFromTree(object.get(name), name, rows);
                break;
            case ARRAY:
                JsonArray array = (JsonArray) tree;
                if (currentArray.equals("rows")) {
                    if (key == null && tree.getValueType() == JsonValue.ValueType.ARRAY) {
                        rows.add(new ArrayList<ArrayList<String>>());
                        rowOrColumnNum++;
                        cellNum = 0;
                    }
                }
                for (JsonValue val : array)
                    getRowsFromTree(val, null, rows);
                break;
            case STRING:
                JsonString st = (JsonString) tree;
                if (currentArray.equals("rows")) {
                    rows.get(rowOrColumnNum).get(cellNum).add(st.toString());
                    cellNum++;
                }
                break;
            case NUMBER:
                JsonNumber num = (JsonNumber) tree;
                if (colours.size() == 0) {
                    if (currentArray.equals("rows")) {
                        rows.get(rowOrColumnNum).add(new ArrayList<>());
                        rows.get(rowOrColumnNum).get(cellNum).add(num.toString());
                        rows.get(rowOrColumnNum).get(cellNum).add("COLOUR_1");
                        cellNum++;
                    }
                } else {
                    if (currentArray.equals("rows")) {
                        rows.get(rowOrColumnNum).add(new ArrayList<>());
                        rows.get(rowOrColumnNum).get(cellNum).add(num.toString());
                    }
                }
                break;
            case TRUE:
            case FALSE:
            case NULL:
                break;
        }
    }

    public static ArrayList<ArrayList<ArrayList<String>>> getColumns(String fileName) throws FileNotFoundException {
        ArrayList<ArrayList<ArrayList<String>>> columns = new ArrayList<>();
        JsonReader reader = Json.createReader(new FileReader(fileName));
        JsonStructure jsonst = reader.read();
        getColumnsFromTree(jsonst, null, columns);
        return columns;
    }

    public static void getColumnsFromTree(JsonValue tree, String key, ArrayList<ArrayList<ArrayList<String>>> columns) {
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
                    getRowsFromTree(object.get(name), name, columns);
                break;
            case ARRAY:
                JsonArray array = (JsonArray) tree;
                if (currentArray.equals("columns")) {
                    if (key == null && tree.getValueType() == JsonValue.ValueType.ARRAY) {
                        columns.add(new ArrayList<ArrayList<String>>());
                        rowOrColumnNum++;
                        cellNum = -1;
                    }
                }
                for (JsonValue val : array)
                    getRowsFromTree(val, null, columns);
                break;
            case STRING:
                JsonString st = (JsonString) tree;
                if (currentArray.equals("columns")) {
                    columns.get(rowOrColumnNum).get(cellNum).add(st.toString());
                    cellNum++;
                }
                break;
            case NUMBER:
                JsonNumber num = (JsonNumber) tree;
                if (colours.size() == 0) {
                    if (currentArray.equals("columns")) {
                        cellNum++;
                        columns.get(rowOrColumnNum).add(new ArrayList<>());
                        columns.get(rowOrColumnNum).get(cellNum).add(num.toString());
                        columns.get(rowOrColumnNum).get(cellNum).add("COLOUR_1");
                        
                    }
                } else {
                    if (currentArray.equals("columns")) {
                        cellNum++;
                        columns.get(rowOrColumnNum).add(new ArrayList<>());
                        columns.get(rowOrColumnNum).get(cellNum).add(num.toString());
                    }
                }
                break;
            case TRUE:
            case FALSE:
            case NULL:
                break;
        }
    }

    public static HashMap<String, String> getColours(String fileName) throws FileNotFoundException {
        HashMap<String, String> colours = new HashMap<>();
        JsonReader reader = Json.createReader(new FileReader(fileName));
        JsonStructure jsonst = reader.read();
        getColoursFromTree(jsonst, null, colours);
        if (colours.size() == 0) {
            colours.put("COLOUR_1", "0x000000");
        }
        return colours;
    }

    public static void getColoursFromTree(JsonValue tree, String key, HashMap<String, String> colours) {
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
                    getColoursFromTree(object.get(name), name, colours);
                break;
            case ARRAY:
                JsonArray array = (JsonArray) tree;
                for (JsonValue val : array)
                    getColoursFromTree(val, null, colours);
                break;
            case STRING:
                JsonString st = (JsonString) tree;
                if (currentArray.equals("colours")) {
                    colours.put(key, st.toString());
                    cellNum++;
                }
                break;
            case NUMBER:
                break;
            case TRUE:
            case FALSE:
            case NULL:
                break;
        }
    }
}