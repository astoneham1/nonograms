import java.util.ArrayList;

// contains already an arraylist of the colors and the counts
// each node will contain the array of the clue placement
public class Node {
    ArrayList<Integer> places;
    Clue c;
    
    public Node (Clue c, ArrayList<Integer> places) {
        this.c = c;
        this.places = places;
    }
}