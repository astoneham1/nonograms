import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
        // calculate the primary arrangement and add it to the arrangement as the first node
        ArrayList<Integer> primaryPlacement = firstPlacement(blocks);
        arrangement.add(new Node(c, primaryPlacement));
        int trailingBlock = 0;
        int iterator = 0;
        ArrayList<Integer> placement = new ArrayList<>(primaryPlacement);
        // for (int i = blocks.size() - 1; i >= 0; i--) { //get rightmost block and incrementally decreases
        //     while (placement.get(i) + blocks.get(i) + trailingBlock < lineLength) {

        //         // modify placement for all the blocks
        //         iterator = i;
        //         for (int j = i; j < blocks.size(); j++) {
        //             placement.set(j, placement.get(j) + 1);
        //         }
        //         System.out.print(new Node(c, new ArrayList<Integer>(placement)).places);
        //         // arrangement.add(new Node(c, new ArrayList<Integer>(placement)));
        //     }
        //     System.out.println();
        //     placement = new ArrayList<Integer>(primaryPlacement);
        //     trailingBlock += blocks.get(i);
        // }
        // trailingBlock = 0;
        getArrangments(arrangement, c, 0, blocks, placement, trailingBlock, primaryPlacement, iterator);
        // each node will contain the Clue c and then an integer array for their placement
        return arrangement;
    }

    public void getArrangments(ArrayList<Node> arrangement, Clue c, int blockIndex, ArrayList<Integer> blocks, ArrayList<Integer> placement, int trailingBlock, ArrayList<Integer> primaryPlacement, int iterator){
        ArrayList<Integer> originalPlacement = new ArrayList<>(primaryPlacement);
        int i = blocks.size() -1;
        while (i>=0) {
            

            placement = new ArrayList<>(originalPlacement);
            for(int j = i; j<blocks.size(); j++){
                placement.set(j, placement.get(j) + iterator);
            }
            primaryPlacement = new ArrayList<>(placement);
            System.out.println(primaryPlacement.toString());
            moveRightMost(arrangement, c, i, blocks, placement, trailingBlock, primaryPlacement, iterator);
            iterator ++;
            i--;
        }
        // if (placement.get(blockIndex) + blocks.get(blockIndex) + trailingBlock == lineLength  || blockIndex == blocks.size() - 1) {
        //     if (blockIndex == blocks.size() - 1) {
        //         moveRightMost(arrangement, c, blockIndex, blocks, placement, trailingBlock, primaryPlacement, iterator);
        //         placement = new ArrayList<>(primaryPlacement);
        //         blockIndex--;
        //         return;
        //     }
        //     return;
        // }
        // else{
        //     blockIndex ++; //getting index towards last block 
        //     getArrangments(arrangement,c, blockIndex, blocks, placement, trailingBlock, primaryPlacement, iterator); //going into next index
        //     iterator++; //adds
        //     blockIndex--;
        //     for (int i = blockIndex; i < blocks.size() - 1; i++) {
        //         placement.set(i, placement.get(i) + iterator);
        //         // System.out.println(placement.get(i));
        //     }
        //     primaryPlacement = new ArrayList<>(placement);
        
        

        // }
    }

    public void moveRightMost(ArrayList<Node> arrangement, Clue c, int blockIndex, ArrayList<Integer> blocks, ArrayList<Integer> placement, int trailingBlock, ArrayList<Integer> primaryPlacement, int iterator){
        while (placement.get(blockIndex) + blocks.get(blockIndex) + trailingBlock < lineLength) {
            // modify placement for all the blocks
            for (int j = blockIndex; j < blocks.size(); j++) {
                placement.set(j, placement.get(j) + 1);
            }
            System.out.print(new Node(c, new ArrayList<Integer>(placement)).places);
            arrangement.add(new Node(c, new ArrayList<Integer>(placement)));
        }
        System.out.println();
        placement = new ArrayList<Integer>(primaryPlacement);
        trailingBlock += blocks.get(blockIndex);
    }
    // public void addVariations(ArrayList<Integer> placement, ArrayList<Integer> blocks, Clue c, int trailingBlock, int sumBlocks, int lineLength) {
    //     if (/*the current block + trailing == linelength && i > 0 */) {
    //         i--;
    //     }
    //     else {
    //         // current block placement + 1
    //     }
    //     if (sumBlocks + placement.get(0) == lineLength) {
    //         arrangement.add(new Node(c, placement));
    //         return;
    //     }
    //     else if () {

    //     } 
    // }

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