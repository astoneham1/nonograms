import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Arrangements {
    ArrayList<Node> arrangement;
    int lineLength;


    public Arrangements(Grid inProgress, int lineLength, Clue c) {
        this.lineLength = lineLength;
        this.arrangement = populateArrangement(inProgress, lineLength, c);
    }


    public ArrayList<Node> populateArrangement(Grid inProgress, int lineLength, Clue c) {
        ArrayList<Node> arrangement = new ArrayList<>();
        // first calculate how much space each clue will take up if placed right next to each other
        ArrayList<Integer> blocks = findBlocks(c);
        // calculate the primary arrangement and add it to the arrangement as the first node
        ArrayList<Integer> primaryPlacement = firstPlacement(blocks);
        arrangement.add(new Node(c, primaryPlacement));
        int trailingBlock = 0;
        ArrayList<Integer> placement = new ArrayList<>(primaryPlacement);
        for (int i = blocks.size() - 1; i >= 0; i--) {
            while (placement.get(i) + blocks.get(i) + trailingBlock < lineLength) {
                // modify placement for all the blocks
                for (int j = i; j < blocks.size(); j++) {
                    placement.set(j, placement.get(j) + 1);
                }
                arrangement.add(new Node(c, new ArrayList<Integer>(placement)));
            }
            placement = new ArrayList<Integer>(primaryPlacement);
            trailingBlock += blocks.get(i);
        }
        // each node will contain the Clue c and then an integer array for their placement
        return arrangement;
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

    public ArrayList<Node> eliminateNodes(ArrayList<Node> prelimArrangement, Grid inProgress) {
        return prelimArrangement;
    }

}