import java.util.ArrayList;

public class Arrangements {
    ArrayList<Node> arrangement = new ArrayList<>();
    int lineLength;
    boolean solved;


    public Arrangements(int lineLength, Clue c) {
        this.lineLength = lineLength;
        this.arrangement = populateArrangement(lineLength, c);
    }


    public ArrayList<Node> populateArrangement(int lineLength, Clue c) {
        ArrayList<Node> arrangement = new ArrayList<>();
        // first calculate how much space each clue will take up if placed right next to each other
        ArrayList<Integer> blocks = findBlocks(c);
        // System.out.println(blocks);
        // calculate the primary arrangement and add it to the arrangement as the first node
        ArrayList<Integer> originalPlace = firstPlacement(blocks);
        // arrangement.add(new Node(c, originalPlace));
        int index = blocks.size() - 1;
        int length = lineLength;
        int place = 0;
        // System.out.println(blocks.toString());
        // System.out.println("original place" + originalPlace.toString());
        // System.out.println("--------------------------------------------------------");
        ArrayList<ArrayList<Integer>> total = getplaces(blocks, index, length, originalPlace, place);
        for (ArrayList<Integer> i: total) {
            arrangement.add(new Node(c, i));
        }
        // each node will contain the Clue c and then an integer array for their placement
        return arrangement;
    }

    public ArrayList<ArrayList<Integer>> getplaces(ArrayList<Integer> blocks, int index, int length, ArrayList<Integer> originalPlace, int place){
        ArrayList<ArrayList<Integer>> total = new ArrayList<>();
        while (index >= 0) {
            // gets the first possible place of the current block
            place = originalPlace.get(index);
            ArrayList<Integer> places = new ArrayList<>();
            // will loop through the potential placement for the blocks
            while (place + blocks.get(index) <= length ) {
                places.add(place);
                place++;
            }
            // takes the last clue and populates an arraylist with all the possible placements for it
            if (total.size() == 0) {
                for (int i : places) {
                    ArrayList<Integer> temp = new ArrayList<>();
                    temp.add(i);
                    total.add(temp);
                }
            } 
            else {
                ArrayList<ArrayList<Integer>> tempTotal = new ArrayList<>();
                for (ArrayList<Integer> combo : total) {
                    for (Integer i : places) {
                        if (i + blocks.get(index) <= combo.get(0)) {
                            ArrayList<Integer> temp = new ArrayList<>(combo);
                            temp.addFirst(i);
                            tempTotal.add(temp);
                        }
                    }
                }
                total = new ArrayList<>(tempTotal);
            }
            length = places.getLast();
            index --;
        }
        return total;
    }

    // calculates the first, most compact placement of the blocks from the right/top
    public ArrayList<Integer> firstPlacement(ArrayList<Integer> blocks) {
        ArrayList<Integer> primaryPlacement = new ArrayList<>();
        primaryPlacement.add(0);
        int currentPlace = 0;
        for (int i = 0; i < blocks.size() - 1; i++) {
            currentPlace += blocks.get(i);
            primaryPlacement.add(currentPlace);
        }
        return primaryPlacement;
    }

    // takes in the clues and converts them into blocks that take account for the 
    public ArrayList<Integer> findBlocks(Clue c) {
        ArrayList<Integer> blocks = new ArrayList<>();
        for (int i = 0; i < c.counts.size(); i++) {
            if (i < c.counts.size() - 1 && c.colour.get(i) == c.colour.get(i + 1)) {
                blocks.add(c.counts.get(i) + 1);
            }
            else {
                blocks.add(c.counts.get(i));
            }
        }
        return blocks;
    }
}