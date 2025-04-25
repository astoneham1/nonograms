import java.util.ArrayList;

public class TestParser {
    static Parser parser = new Parser();

    public static void main(String[] args) {
        testGetClue();
        testGetClues();
    }

    public static void testGetClue(){
        System.out.println("Testing the getClue method of parser:");
        ArrayList<Integer> countsTest1 = new ArrayList<>();
        ArrayList<Integer> coloursTest1 = new ArrayList<>();
        countsTest1.add(1);
        countsTest1.add(1);
        countsTest1.add(14);
        coloursTest1.add(2);
        coloursTest1.add(5);
        coloursTest1.add(1);
        Clue correcttest1 = new Clue(countsTest1, coloursTest1);
        try {
            if ((parser.getClue(countsTest1, coloursTest1).colour == correcttest1.colour) && (parser.getClue(countsTest1, coloursTest1).counts == correcttest1.counts) ) {
                System.out.println("Test 1 Passed");
            } else {
                System.out.println("Test 1 Failed");
                System.out.println(correcttest1);
                System.out.println(parser.getClue(countsTest1, coloursTest1));
            }
        } catch (JsonFormatException e) {
            System.out.println("Test 1 Failed");
        }

        ArrayList<Integer> countsTest2 = new ArrayList<>();
        ArrayList<Integer> coloursTest2 = new ArrayList<>();
        countsTest2.add(1);
        countsTest2.add(1);
        coloursTest2.add(2);
        coloursTest2.add(5);
        coloursTest2.add(1);
        Clue correcttest2 = new Clue(countsTest2, coloursTest2);
        try {
            if ((parser.getClue(countsTest2, coloursTest2).colour == correcttest2.colour) && (parser.getClue(countsTest2, coloursTest2).counts == correcttest2.counts)) {
                System.out.println("Test 2 Failed");
            } else {
                System.out.println("Test 2 Failed");
            }
        } catch (JsonFormatException e) {
            System.out.println("Test 2 Passed");
        }

        ArrayList<Integer> countsTest3 = new ArrayList<>();
        ArrayList<Integer> coloursTest3 = new ArrayList<>();
        Clue correcttest3 = new Clue(countsTest3, coloursTest3);
        try {
            if ((parser.getClue(countsTest3, coloursTest3).colour == correcttest3.colour) && (parser.getClue(countsTest3, coloursTest3).counts == correcttest3.counts) ) {
                System.out.println("Test 3 Passed");
            } else {
                System.out.println("Test 3 Failed");
            }
        } catch (JsonFormatException e) {
            System.out.println("Test 3 Faield");
        }
    }

    public static void testGetClues(){
        System.out.println("Testing the getClues mathod of parser:");
        ArrayList<ArrayList<Integer>> lines = new ArrayList<>();
        ArrayList<ArrayList<Integer>> colours = new ArrayList<>();
        ArrayList<Clue> cluesCorrect = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            lines.add(new ArrayList<>());
            colours.add(new ArrayList<>());
            for (int j = 0; j < i; j++) {
                lines.get(i).add(j);
                colours.get(i).add(j%3);
            }
            cluesCorrect.add(new Clue(lines.get(i), colours.get(i)));
        }
        
        try {
            boolean passed = true;
            ArrayList<Clue> result = parser.getClues(lines, colours);
            for (int i = 0; i < 10; i++) {
                if(!(cluesCorrect.get(i).colour == result.get(i).colour) || !(cluesCorrect.get(i).counts == result.get(i).counts)){
                    passed = false;
                }
            }
            if (passed) {
                System.out.println("Test 1 Passed");
            }
        } catch (Exception e) {
            System.out.println("Test 1 Failed");
        }

        ArrayList<Clue> cluesCorrect1 = new ArrayList<>();
        
        try {
            boolean passed = true;
            ArrayList<Clue> result = parser.getClues(lines, colours);
            for (int i = 0; i < 10; i++) {
                if(!(cluesCorrect.get(i).colour == result.get(i).colour) || !(cluesCorrect.get(i).counts == result.get(i).counts)){
                    passed = false;
                }
            }
            if (passed) {
                System.out.println("Test 2 Passed");
            }
        } catch (Exception e) {
            System.out.println("Test 2 Failed");
        }
        
    }
}
