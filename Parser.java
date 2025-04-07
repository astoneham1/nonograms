import javax.json.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    //2d arraylist containing an arraylist of number of cells and there colour for row and columns
    public static ArrayList<ArrayList<ArrayList<String>>> rows = new ArrayList<>();
    public static ArrayList<ArrayList<ArrayList<String>>> columns = new ArrayList<>();
    //hashmap of the colours and there hex codes
    public static HashMap<String, String> colours = new HashMap<>();

    //used for parsing the json
    public static String currentArray = "";
    public static Integer rowOrColumnNum = 0;
    public static Integer cellNum = 0;

    public static void main(String[] args) {
        String fileName = "./Jsons/cat.json";
        try {
            getArrayLists(fileName);
            if (colours.size() == 0) {
                colours.put("COLOUR_1", "0x000000");
            }
            System.out.println(rows.toString());
            System.out.println(columns.toString());
            System.out.println(colours.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getArrayLists(String fileName) throws FileNotFoundException {
        JsonReader reader = Json.createReader(new FileReader(fileName));
        JsonStructure jsonst = reader.read();
        getArrayListsFromTree(jsonst, null);
    }

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
                        rows.add(new ArrayList<ArrayList<String>>());
                        rowOrColumnNum++;
                        cellNum = 0;
                    }
                } else if (currentArray.equals("columns")) {
                    if (key == null && tree.getValueType() == JsonValue.ValueType.ARRAY) {
                        columns.add(new ArrayList<ArrayList<String>>());
                        rowOrColumnNum++;
                        cellNum = 0;
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
                    rows.get(rowOrColumnNum).get(cellNum).add(st.toString());
                    cellNum++;
                } else if (currentArray.equals("columns")) {
                    columns.get(rowOrColumnNum).get(cellNum).add(st.toString());
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
                    if (currentArray.equals("columns")) {
                        columns.get(rowOrColumnNum).add(new ArrayList<>());
                        columns.get(rowOrColumnNum).get(cellNum).add(num.toString());
                        columns.get(rowOrColumnNum).get(cellNum).add("COLOUR_1");
                        cellNum++;
                    }
                } else {
                    if (currentArray.equals("rows")) {
                        rows.get(rowOrColumnNum).add(new ArrayList<>());
                        rows.get(rowOrColumnNum).get(cellNum).add(num.toString());
                    }
                    if (currentArray.equals("columns")) {
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
}