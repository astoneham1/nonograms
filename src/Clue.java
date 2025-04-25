import java.util.ArrayList;

public class Clue{
    ArrayList<Integer> counts = new ArrayList<>();
    ArrayList<Integer> colour = new ArrayList<>();

    Clue(ArrayList<Integer> counts, ArrayList<Integer> colour){
        this.counts = counts;
        this.colour = colour;
    }  

    public ArrayList<Integer> getCounts(){
        return this.counts;
    }

    public ArrayList<Integer> getColours(){
        return this.colour;
    }
}