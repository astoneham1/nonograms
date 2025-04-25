// import javax.json.JsonWriter;

public class TestMain { 
    public static Grid currentGrid; // current grid of clues  
    public static Grid inProgressGrid; // grid that represents the progress of the user in terms of clues
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_GREY = "\u001B[90m";
    public static final String ANSI_YELLOW = "\u001B[93m";

    public static void main(String [] args) {
    try {
        Parser p = new Parser();
        Grid g = p.getGrid("Jsons/house.json");
        SolverMain s = new SolverMain(g);
        s.solver();
        for (int i = 0; i < s.solverGrid.rows; i++) {
            for (int j = 0; j < s.solverGrid.columns; j++) {
                System.out.print(prettyPrint(s.solverGrid.grid[i][j]));
            }
            System.out.println();
        }
       


    } catch (Exception e) {
        System.out.println(e);
    }

    }

    public static String prettyPrint(int i) {
        switch(i) {
            case 0:
                return Integer.toString(i);
            case 3:
                String j = ANSI_YELLOW + Integer.toString(i) + ANSI_RESET;
                return j;
            case 2:
                j = ANSI_GREY + Integer.toString(i) + ANSI_RESET;
                return j;
            case 4: 
                j = ANSI_GREEN + Integer.toString(i) + ANSI_RESET;
                return j;
        }
        return Integer.toString(i);
    }

}
