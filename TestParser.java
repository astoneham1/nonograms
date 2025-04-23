import java.util.ArrayList;

public class TestParser {
    static Parser parser = new Parser();

    public static void main(String[] args) {
        TestParser.testGetClue();
    }

    public static void testGetClue(){
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
            if (parser.getClue(countsTest2, coloursTest2) == correcttest2) {
                System.out.println("Test 2 Failed");
            } else {
                System.out.println("Test 2 Failed");
            }
        } catch (JsonFormatException e) {
            System.out.println("Test 2 Passed");
        }
    }
}
